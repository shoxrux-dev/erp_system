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
public class OutgoingProductFromInventory extends BaseModel {
    int productId;
    Integer count;
    int inventoryId;
}
