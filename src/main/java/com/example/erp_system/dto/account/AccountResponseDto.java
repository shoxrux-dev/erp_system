package com.example.erp_system.dto.account;

import com.example.erp_system.dto.BaseResponseDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountResponseDto extends BaseResponseDto {
    BigDecimal balance;
}
