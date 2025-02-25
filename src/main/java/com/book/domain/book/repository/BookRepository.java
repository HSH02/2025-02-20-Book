package com.book.domain.book.repository;

import com.book.domain.book.entity.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByIsbn(String isbn);

    List<Book> findAll(Specification<Book> spec, Pageable pageable);
}


