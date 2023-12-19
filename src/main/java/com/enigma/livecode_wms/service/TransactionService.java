package com.enigma.livecode_wms.service;

import com.enigma.livecode_wms.dto.request.TransactionRequest;
import com.enigma.livecode_wms.dto.response.TransactionResponse;

import java.time.LocalDate;
import java.util.List;

public interface TransactionService {
    TransactionResponse createNewTransaction(TransactionRequest transactionRequest);
    TransactionResponse getTransactionById(String id);
    List<TransactionResponse> getAllTransaction();

    TransactionResponse getTotalSales(LocalDate startDate, LocalDate endDate) ;
}
