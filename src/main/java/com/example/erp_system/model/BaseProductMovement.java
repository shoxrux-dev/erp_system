package com.example.erp_system.model;

import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseProductMovement extends BaseModel {
    int productId;
    int companyId;
    int count;
    BigDecimal price;
    int inventoryId;
}
