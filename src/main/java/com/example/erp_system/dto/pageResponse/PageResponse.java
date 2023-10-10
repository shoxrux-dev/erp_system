package com.example.erp_system.dto.pageResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Collections;
import java.util.List;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
public class PageResponse<T> {
    List<T> content;
    int pageNum;
    int pageSize;
    long totalElements;
    int totalPages;
    boolean last;
}
