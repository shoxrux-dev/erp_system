package com.example.erp_system.dto.incoming_product;


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
public class IncomingProductResponseDto extends BaseResponseDto implements Comparable<IncomingProductResponseDto> {
    int productId;
    String productName;
    int companyId;
    String companyName;
    Integer count;
    BigDecimal price;
    int inventoryId;
    String inventoryName;

    @Override
    public int compareTo(IncomingProductResponseDto o) {
        return this.getCreatedAt().compareTo(o.getCreatedAt());
    }
}
