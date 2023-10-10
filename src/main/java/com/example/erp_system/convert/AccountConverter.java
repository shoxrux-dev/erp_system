package com.example.erp_system.convert;

import com.example.erp_system.dto.account.AccountCreateRequestDto;
import com.example.erp_system.dto.account.AccountResponseDto;
import com.example.erp_system.model.Account;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AccountConverter {
    public Account converterToDomain(AccountCreateRequestDto accountCreateRequestDto){
        return Account.builder()
                .balance(accountCreateRequestDto.getBalance())
                .build();
    }

    public AccountResponseDto from(Account account){
        return AccountResponseDto.builder()
                .id(account.getId())
                .balance(account.getBalance())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
    }

}
