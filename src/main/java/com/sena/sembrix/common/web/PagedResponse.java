package com.sena.sembrix.common.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Generic Paginated Response DTO
 * Wraps paginated data with pagination metadata
 *
 * @param <T> The type of content elements
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagedResponse<T> {

    /**
     * The list of elements in the current page
     */
    private List<T> content;

    /**
     * Current page number (0-indexed)
     */
    private int page;

    /**
     * Number of elements per page
     */
    private int size;

    /**
     * Total number of elements in the database
     */
    private long totalElements;

    /**
     * Total number of pages
     */
    private int totalPages;

    /**
     * Whether this is the last page
     */
    private boolean last;

    /**
     * Whether this is the first page
     */
    private boolean first;

    /**
     * Number of elements in the current page
     */
    private int numberOfElements;

    /**
     * Whether there are more pages after this one
     */
    private boolean hasNext;

    /**
     * Whether there are pages before this one
     */
    private boolean hasPrevious;

    /**
     * Static factory method to create a PagedResponse from Spring's Page object
     *
     * @param page Spring Data Page object
     * @param <T> The type of content
     * @return PagedResponse populated from the Page object
     */
    public static <T> PagedResponse<T> from(Page<T> page) {
        return PagedResponse.<T>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .first(page.isFirst())
                .numberOfElements(page.getNumberOfElements())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }
}
