package com.example.erp_system.dto.incoming_and_outgoing_to_account;

import com.example.erp_system.constant.AccountStatusEnum;
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
public class IncomingAndOutgoingToAccountResponseDto extends BaseResponseDto implements Comparable<IncomingAndOutgoingToAccountResponseDto> {
    int companyId;
    String companyName;
    BigDecimal price;
    AccountStatusEnum status;

    @Override
    public int compareTo(IncomingAndOutgoingToAccountResponseDto o) {
        return this.getCreatedAt().compareTo(o.getCreatedAt());
    }
}
