package com.example.erp_system.dto.debt;

import com.example.erp_system.constant.DebtStatusEnum;
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
public class DebtResponseDto extends BaseResponseDto implements Comparable<DebtResponseDto> {
    int companyId;
    String companyName;
    BigDecimal amount;
    DebtStatusEnum status;

    @Override
    public int compareTo(DebtResponseDto o) {
        return this.getCreatedAt().compareTo(o.getCreatedAt());
    }
}
