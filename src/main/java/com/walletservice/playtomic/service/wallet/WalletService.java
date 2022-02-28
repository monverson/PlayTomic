package com.walletservice.playtomic.service.wallet;

import com.walletservice.playtomic.dto.ChargeRequest;
import com.walletservice.playtomic.dto.WalletDto;
import com.walletservice.playtomic.service.stripe.StripeServiceException;

import java.util.concurrent.CompletableFuture;


public interface WalletService {
    /**
     * Create Wallet
     */
    CompletableFuture<WalletDto> create() throws StripeServiceException, InterruptedException;

    /**
     * Search a Wallet by ID
     *
     * @param id creditCardId for charge
     */
    CompletableFuture<WalletDto> findById(String id) throws StripeServiceException, InterruptedException;

    /**
     * Top Up Balance
     *
     * @param chargeRequest requestBody
     */
    CompletableFuture<WalletDto> topUp(ChargeRequest chargeRequest) throws InterruptedException;
}
