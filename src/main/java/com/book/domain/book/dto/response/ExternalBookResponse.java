package com.book.domain.book.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record ExternalBookResponse(
        Meta meta,
        List<Document> documents
) {
    public record Meta(
            int total_count,
            int pageable_count,
            boolean is_end
    ) {
        public boolean isEnd() {
            return is_end;
        }
    }

    public record Document(
            String title,
            String contents,
            String url,
            String isbn,
            String datetime,
            List<String> authors,
            String publisher,
            List<String> translators,
            BigDecimal price,
            BigDecimal sale_price,
            String thumbnail,
            String status
    ) {
    }
}
