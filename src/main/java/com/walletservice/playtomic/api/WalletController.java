package com.walletservice.playtomic.api;

import com.sun.istack.NotNull;
import com.walletservice.playtomic.dto.ChargeRequest;
import com.walletservice.playtomic.dto.WalletDto;
import com.walletservice.playtomic.service.wallet.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
public class WalletController {

    private Logger log = LoggerFactory.getLogger(WalletController.class);
    private final WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    /**
     * Create a wallet
     *
     * @return wallet
     */
    @PostMapping(value = "/create")
    public ResponseEntity<CompletableFuture<WalletDto>> createWallet() throws InterruptedException {
        CompletableFuture<WalletDto> wallet= walletService.create();
        return new ResponseEntity<>(wallet,
                HttpStatus.OK);
    }

    /**
     * Get a wallet using its identifier.
     *
     * @param creditCardId
     * @return wallet
     */
    @GetMapping(value = "/{creditCardId}")
    public ResponseEntity<CompletableFuture<WalletDto>> getWalletCreditCardId(@PathVariable @NotNull String creditCardId) throws InterruptedException {
        final CompletableFuture<WalletDto> wallet = walletService.findById(creditCardId);
        if (wallet == null) {
            log.info("No Wallet Information Available");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(wallet, HttpStatus.OK);
    }

    /**
     * TopUp wallet using requestBody.
     *
     * @param chargeRequest
     * @return wallet
     */
    @PostMapping("/top-up")
    public ResponseEntity<CompletableFuture<WalletDto>> topUpWallet(@RequestBody ChargeRequest chargeRequest) throws InterruptedException {
        CompletableFuture<WalletDto> wallet = walletService.topUp(chargeRequest);
        log.info("Successfully topped up into wallet");
        return new ResponseEntity<>(wallet, HttpStatus.OK);
    }

}
