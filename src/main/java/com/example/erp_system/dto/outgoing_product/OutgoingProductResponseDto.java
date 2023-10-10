package com.example.erp_system.dto.outgoing_product;

import com.example.erp_system.constant.DebtStatusEnum;
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
public class OutgoingProductResponseDto extends BaseResponseDto implements Comparable<OutgoingProductResponseDto> {
    int productId;
    String productName;
    int companyId;
    String companyName;
    Integer count;
    BigDecimal price;
    int inventoryId;
    String inventoryName;
    DebtStatusEnum status;

    @Override
    public int compareTo(OutgoingProductResponseDto o) {
        return this.getCreatedAt().compareTo(o.getCreatedAt());
    }
}
