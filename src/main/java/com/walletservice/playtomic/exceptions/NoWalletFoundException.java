package com.walletservice.playtomic.exceptions;

public class NoWalletFoundException extends RuntimeException {
    public NoWalletFoundException(String message) {
        super(message);
    }
}
