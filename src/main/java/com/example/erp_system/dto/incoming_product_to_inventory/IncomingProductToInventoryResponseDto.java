package com.example.erp_system.dto.incoming_product_to_inventory;

import com.example.erp_system.dto.BaseResponseDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IncomingProductToInventoryResponseDto extends BaseResponseDto implements Comparable<IncomingProductToInventoryResponseDto> {
    Integer count;
    int productId;
    String productName;
    int inventoryId;
    String inventoryName;

    @Override
    public int compareTo(IncomingProductToInventoryResponseDto o) {
        return this.getCreatedAt().compareTo(o.getCreatedAt());
    }
}
