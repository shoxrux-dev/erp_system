package com.example.erp_system.dto.outgoing_product_from_inventory;

import com.example.erp_system.dto.BaseResponseDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OutgoingProductFromInventoryResponseDto extends BaseResponseDto implements Comparable<OutgoingProductFromInventoryResponseDto> {
    Integer count;
    int productId;
    String productName;
    int inventoryId;
    String inventoryName;

    @Override
    public int compareTo(OutgoingProductFromInventoryResponseDto o) {
        return this.getCreatedAt().compareTo(o.getCreatedAt());
    }
}
