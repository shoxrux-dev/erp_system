package com.example.erp_system.dto.category;

import com.example.erp_system.dto.BaseResponseDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryResponseDto extends BaseResponseDto implements Comparable<CategoryResponseDto> {
    String name;

    @Override
    public int compareTo(CategoryResponseDto o) {
        return this.getCreatedAt().compareTo(o.getCreatedAt());
    }
}
