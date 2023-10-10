package com.example.erp_system.model;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductInInventory extends BaseModel {
    int productId;
    int inventoryId;
    int count;
}
