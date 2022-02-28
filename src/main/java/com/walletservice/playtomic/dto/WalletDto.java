package com.walletservice.playtomic.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.sun.istack.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class WalletDto {

    @JsonProperty
    @Id
    private String creditCardId;

    @JsonProperty
    @NotNull
    private BigDecimal currentBalance;

    public WalletDto() {
        this.currentBalance = BigDecimal.ZERO;
        creditCardId =  UUID.randomUUID().toString();
    }
}
