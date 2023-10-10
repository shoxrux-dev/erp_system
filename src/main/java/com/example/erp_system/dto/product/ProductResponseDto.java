package com.example.erp_system.dto.product;

import com.example.erp_system.dto.BaseResponseDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponseDto extends BaseResponseDto implements Comparable<ProductResponseDto> {
    String name;
    String image;
    int categoryId;
    String categoryName;

    @Override
    public int compareTo(ProductResponseDto o) {
        return this.getCreatedAt().compareTo(o.getCreatedAt());
    }
}
