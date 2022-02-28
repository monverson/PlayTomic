package com.walletservice.playtomic.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;


@Entity
@Table(name = "WALLET")
@Data
public class Wallet {
    @Id
    @Column(name = "CARD_ID")
    private String creditCardId;

    @Column(name = "CURRENT_BALANCE")
    private BigDecimal currentBalance;
}
