package com.example.erp_system.dto.product_in_inventory;

import com.example.erp_system.dto.BaseResponseDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductInInventoryResponseDto extends BaseResponseDto implements Comparable<ProductInInventoryResponseDto> {
    Integer count;
    int inventoryId;
    String inventoryName;
    int productId;
    String productName;

    @Override
    public int compareTo(ProductInInventoryResponseDto o) {
        return this.getCreatedAt().compareTo(o.getCreatedAt());
    }
}
