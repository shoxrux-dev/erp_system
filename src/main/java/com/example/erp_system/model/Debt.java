package com.example.erp_system.model;

import com.example.erp_system.constant.DebtStatusEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Debt extends BaseModel {
    int companyId;
    BigDecimal amount;
    @Enumerated(EnumType.STRING)
    DebtStatusEnum status;
}
