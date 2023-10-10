package com.example.erp_system.dto.outgoing_product;

import com.example.erp_system.constant.DebtStatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseOutgoingProductRequestDto {
    @NotNull int productId;
    @NotNull int companyId;
    @NotNull Integer count;
    @NotNull BigDecimal price;
    @NotNull int inventoryId;
    @NotNull DebtStatusEnum status;
}