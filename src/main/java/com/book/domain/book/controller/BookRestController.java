package com.book.domain.book.controller;

import com.book.domain.book.entity.Book;
import com.book.domain.book.service.BookService;
import com.book.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookRestController {

    private final BookService bookService;

    @GetMapping
    public ApiResponse<List<Book>> getBooks(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "new") String sort
    ) {

        List<Book> books = bookService.findAll(page, size, sort);
        return ApiResponse.success("올바르게 책 정보들을 찾는 것에 성공했습니다.", books);
    }

    @GetMapping("/{bookId}")
    public ApiResponse<Book> getBookById(
            @PathVariable long bookId
    ) {
        Book book = bookService.findById(bookId);

        return ApiResponse.success("올바르게 책을 조회했습니다.", book);
    }

    @GetMapping("/search")
    public ApiResponse<List<Book>> search(
            @RequestParam("query") String query,
            @RequestParam("category") String category,
            @RequestParam("page") int page,
            @RequestParam("size") int size
    ) {
        List<Book> books = bookService.search(query, category, page, size);
        return ApiResponse.success(books);
    }

    @GetMapping("/external")
    public ApiResponse<String> fetchBooks(
            @RequestParam(value = "query", defaultValue = "코딩") String query
    ) {
        bookService.fetchAndSaveNewBooks(query);
        return ApiResponse.success("올바르게 책을 가져왔습니다.");
    }

}
