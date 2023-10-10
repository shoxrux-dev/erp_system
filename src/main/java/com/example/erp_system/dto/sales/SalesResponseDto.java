package com.example.erp_system.dto.sales;

import com.example.erp_system.dto.BaseResponseDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SalesResponseDto extends BaseResponseDto implements Comparable<SalesResponseDto> {
    int productId;
    String productName;
    int companyId;
    String companyName;
    Integer count;
    BigDecimal revenue;

    @Override
    public int compareTo(SalesResponseDto o) {
        return this.getCreatedAt().compareTo(o.getCreatedAt());
    }
}
