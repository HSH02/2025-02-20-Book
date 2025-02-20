package com.book.domain.book.service;


import com.book.domain.book.dto.response.ExternalBookResponse;
import com.book.domain.book.entity.Book;
import com.book.domain.book.repository.BookRepository;
import com.google.gson.Gson;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// TODO : HttpHeaders, restTemplate, QueryDSL
@RequiredArgsConstructor
@Service
public class BookService {

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    @Value("${kakao.api.url}")
    private String kakaoApiUrl;

    private final BookRepository bookRepository;
    private final RestTemplate restTemplate;

    public void fetchAndSaveNewBooks(String query) {
        int size = 50;  // 한 페이지당 최대 50건
        int page = 1;
        boolean isEnd = false;

        while (page <= 100 && !isEnd) {
            // 카카오 책 검색 API 호출 URL
            String url = String.format("%s?query=%s&page=%d&size=%d",
                    kakaoApiUrl, query, page, size);

            // 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + kakaoApiKey);
            HttpEntity<?> requestEntity = new HttpEntity<>(headers);

            // API 호출
            ResponseEntity<ExternalBookResponse> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    ExternalBookResponse.class
            );
            ExternalBookResponse response = responseEntity.getBody();

            if (response == null) {
                break; // 혹은 예외 처리
            }

            // documents(도서 목록) 순회하며 Book 엔티티로 매핑
            List<Book> newBooks = new ArrayList<>();
            for (ExternalBookResponse.Document doc : response.documents()) {
                String isbn = doc.isbn();

                // ISBN이 없거나 공백이면 저장 불가
                if (isbn == null || isbn.trim().isEmpty()) {
                    continue;
                }

                // 중복 여부 체크 (ISBN 기준)
                if (!bookRepository.existsByIsbn(isbn)) {
                    Book book = new Book();
                    book.setTitle(doc.title());
                    List<String> authors = doc.authors();
                    if (authors != null && !authors.isEmpty()) {
                        book.setAuthors(String.join(", ", authors));
                    }
                    book.setPublisher(doc.publisher());
                    book.setPrice(doc.price());
                    book.setThumbnail(doc.thumbnail());
                    book.setContents(doc.contents());
                    book.setSalePrice(doc.sale_price());
                    book.setStatus(doc.status());
                    // 날짜 형식 처리
                    if (doc.datetime() != null && !doc.datetime().isEmpty()) {
                        String datePart = doc.datetime().substring(0, 10);
                        book.setDatetime(LocalDate.parse(datePart));
                    }
                    book.setIsbn(doc.isbn());
                    book.setUrl(doc.url());
                    // metadata에 JSON 통째로 저장
                    book.setMetadata(new Gson().toJson(doc));

                    newBooks.add(book);
                }
            }

            // 새로 수집된 도서가 있다면 DB에 저장
            if (!newBooks.isEmpty()) {
                bookRepository.saveAll(newBooks);
            }

            // Kakao Book API의 meta 정보를 통해 다음 페이지 요청 여부 결정
            ExternalBookResponse.Meta meta = response.meta();

            if (meta != null && meta.isEnd()) {
                // 더 이상 가져올 페이지 없음
                isEnd = true;
            } else {
                // 다음 페이지 호출
                page++;
            }
        }

    }

    public List<Book> findAll(int page, int size, String sort) {
        Sort sortOrder;

        if ("NEW".equalsIgnoreCase(sort)) {
            sortOrder = Sort.by("datetime").descending();
        } else {
            sortOrder = Sort.by("datetime").ascending();
        }

        Pageable pageable = PageRequest.of(page, size, sortOrder);
        return bookRepository.findAll(pageable).getContent();
    }

    public List<Book> search(String query, String category, int page, int size) {
        // TODO
        return null;
    }

    public Book findById(long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("책을 찾을 수 없습니다 :" + bookId));
    }
}

