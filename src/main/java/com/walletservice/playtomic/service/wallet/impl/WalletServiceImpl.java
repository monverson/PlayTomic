package com.walletservice.playtomic.service.wallet.impl;

import com.walletservice.playtomic.dto.ChargeRequest;
import com.walletservice.playtomic.dto.WalletDto;
import com.walletservice.playtomic.entity.Wallet;
import com.walletservice.playtomic.exceptions.NoWalletFoundException;
import com.walletservice.playtomic.mapper.WalletMapper;
import com.walletservice.playtomic.repository.WalletRepository;
import com.walletservice.playtomic.service.stripe.StripeAmountTooSmallException;
import com.walletservice.playtomic.service.stripe.StripeService;
import com.walletservice.playtomic.service.stripe.StripeServiceException;
import com.walletservice.playtomic.service.wallet.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

@Service
public class WalletServiceImpl implements WalletService {

    private final Logger log = LoggerFactory.getLogger(WalletService.class);
    private static final BigDecimal MINIMUM_VALUE = new BigDecimal(5);
    private static final String LESS_THAN_MINIMUM_AMOUNT = "The entered amount is not enough";
    private final WalletRepository walletRepository;
    private final StripeService stripeService;
    private final WalletMapper walletMapper;

    public WalletServiceImpl(WalletRepository walletRepository,
                             StripeService stripeService,
                             WalletMapper walletMapper) {
        this.walletRepository = walletRepository;
        this.stripeService = stripeService;
        this.walletMapper = walletMapper;
    }

    @Async("threadPoolTaskExecutor")
    @Override
    public CompletableFuture<WalletDto> create() throws StripeServiceException, InterruptedException {
        Wallet wallet = walletMapper.map(new WalletDto());
        walletRepository.save(wallet);
        Thread.sleep(1000L);
        log.info("Wallet created {}", wallet);
        WalletDto walletDto = walletMapper.map(wallet);
        return CompletableFuture.completedFuture(walletDto);
    }

    @Async("threadPoolTaskExecutor")
    @Override
    public CompletableFuture<WalletDto> findById(String id) throws StripeServiceException, InterruptedException {
        Wallet wallet = walletRepository.findById(id).orElse(null);
        Thread.sleep(1000L);
        log.info("Wallet  {}", wallet);
        WalletDto walletDto = walletMapper.map(wallet);
        return CompletableFuture.completedFuture(walletDto);
    }

    @Async("threadPoolTaskExecutor")
    @Override
    public CompletableFuture<WalletDto> topUp(ChargeRequest chargeRequest) throws InterruptedException {
        checkMinimumAmount(chargeRequest);
        Wallet wallet = walletRepository.findById(chargeRequest.getCardID()).orElseThrow(() ->
                new NoWalletFoundException("There is no wallet information available"));
        stripeService.charge(chargeRequest.getCardID(), chargeRequest.getAmount());
        Thread.sleep(1000L);
        wallet.setCurrentBalance(wallet.getCurrentBalance().add(chargeRequest.getAmount()));
        walletRepository.save(wallet);
        Thread.sleep(1000L);
        log.info("Top up completed");
        WalletDto walletDto = walletMapper.map(wallet);
        return CompletableFuture.completedFuture(walletDto);
    }


    private void checkMinimumAmount(ChargeRequest chargeRequest) {
        if (MINIMUM_VALUE.compareTo(chargeRequest.getAmount()) >= 0) {
            log.error(LESS_THAN_MINIMUM_AMOUNT);
            throw new StripeAmountTooSmallException();
        }
    }
}
