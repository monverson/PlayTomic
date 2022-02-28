package com.walletservice.playtomic.repository;

import com.walletservice.playtomic.entity.Wallet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends CrudRepository<Wallet,String> {
}
