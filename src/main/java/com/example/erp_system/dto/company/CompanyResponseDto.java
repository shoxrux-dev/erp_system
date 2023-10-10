package com.example.erp_system.dto.company;

import com.example.erp_system.dto.BaseResponseDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompanyResponseDto extends BaseResponseDto implements Comparable<CompanyResponseDto> {
    String name;

    @Override
    public int compareTo(CompanyResponseDto o) {
        return this.getCreatedAt().compareTo(o.getCreatedAt());
    }
}
