package com.enigma.livecode_wms.repository;

import com.enigma.livecode_wms.entity.TransactionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionDetailRepository extends JpaRepository<TransactionDetail, String> {
    List<TransactionDetail> findByTransactionId(String transactionId);

}

