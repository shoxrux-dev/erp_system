package com.example.erp_system.dto.inventory;

import com.example.erp_system.dto.BaseResponseDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryResponseDto extends BaseResponseDto implements Comparable<InventoryResponseDto> {
    String name;

    @Override
    public int compareTo(InventoryResponseDto o) {
        return this.getCreatedAt().compareTo(o.getCreatedAt());
    }
}
