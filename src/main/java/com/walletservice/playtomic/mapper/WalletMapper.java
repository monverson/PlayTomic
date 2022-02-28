package com.walletservice.playtomic.mapper;

import com.walletservice.playtomic.dto.WalletDto;
import com.walletservice.playtomic.entity.Wallet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WalletMapper {

    WalletDto map(Wallet wallet);

    Wallet map(WalletDto walletDto);
}
