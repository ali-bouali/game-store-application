package com.alibou.store.utils;

import com.alibou.store.common.PageResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PaginationUtils {

    private PaginationUtils() { }

    public static <T, R> PageResponse<R> buildPageResponse(Page<T> page, Function<T, R> mapper) {
        List<R> mappedContent = page.getContent()
                .stream()
                .map(mapper)
                .collect(Collectors.toList());

        return PageResponse.<R>builder()
                .number(page.getNumber())
                .size(page.getSize())
                .totalElements((int) page.getTotalElements())
                .totalPages(page.getTotalPages())
                .isFirst(page.isFirst())
                .isLast(page.isLast())
                .content(mappedContent)
                .build();
    }
}
