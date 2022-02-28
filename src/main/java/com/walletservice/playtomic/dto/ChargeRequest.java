package com.walletservice.playtomic.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
public class ChargeRequest {

    @NonNull
    @JsonProperty("credit_card_id")
    String cardID;

    @NonNull
    @JsonProperty("amount")
    BigDecimal amount;

}
