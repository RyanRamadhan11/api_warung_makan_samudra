package com.enigma.livecode_wms.controller;

import com.enigma.livecode_wms.constant.AppPath;
import com.enigma.livecode_wms.dto.request.ProductRequest;
import com.enigma.livecode_wms.dto.request.TransactionRequest;
import com.enigma.livecode_wms.dto.response.CommonResponse;
import com.enigma.livecode_wms.dto.response.ProductResponse;
import com.enigma.livecode_wms.dto.response.TransactionDetailResponse;
import com.enigma.livecode_wms.dto.response.TransactionResponse;
import com.enigma.livecode_wms.entity.Transaction;
import com.enigma.livecode_wms.entity.TransactionDetail;
import com.enigma.livecode_wms.repository.TransactionDetailRepository;
import com.enigma.livecode_wms.repository.TransactionRepository;
import com.enigma.livecode_wms.service.ProductService;
import com.enigma.livecode_wms.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(AppPath.TRANSACTION)
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createTransaction(@RequestBody TransactionRequest transactionRequest){
        TransactionResponse transactionResponse  = transactionService.createNewTransaction(transactionRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.<TransactionResponse>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Successfully created new Transaction")
                        .data(transactionResponse)
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> gettransactionById(@PathVariable String id) {
        TransactionResponse transactionResponse = transactionService.getTransactionById(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.<TransactionResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully get transaction by id")
                        .data(transactionResponse)
                        .build());
    }

    @GetMapping
    public List<TransactionResponse> getAllTransaction() {
        return transactionService.getAllTransaction();
    }

}

