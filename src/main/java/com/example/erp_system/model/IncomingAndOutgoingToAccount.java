package com.example.erp_system.model;

import com.example.erp_system.constant.AccountStatusEnum;
import jakarta.persistence.Entity;
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
public class IncomingAndOutgoingToAccount extends BaseModel {
    int companyId;
    BigDecimal price;
    AccountStatusEnum status;
}
