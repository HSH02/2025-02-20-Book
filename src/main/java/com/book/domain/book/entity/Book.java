package com.book.domain.book.entity;

import com.book.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "book")
@Getter
@Setter
public class Book extends BaseEntity {

    @Column(length = 2000)
    private String contents;

    private String authors;
    private LocalDate datetime;
    private String isbn;
    private BigDecimal price;
    private String publisher;
    private BigDecimal salePrice;
    private String status;
    private String thumbnail;
    private String title;
    private String translators;

    @Column(columnDefinition = "TEXT")
    private String url;

    @Column(columnDefinition = "TEXT")
    private String metadata;
}
