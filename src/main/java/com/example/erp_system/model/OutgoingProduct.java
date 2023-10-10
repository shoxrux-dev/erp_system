package com.example.erp_system.model;

import com.example.erp_system.constant.DebtStatusEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OutgoingProduct extends BaseProductMovement {
    @Enumerated(EnumType.STRING)
    DebtStatusEnum status;
}
