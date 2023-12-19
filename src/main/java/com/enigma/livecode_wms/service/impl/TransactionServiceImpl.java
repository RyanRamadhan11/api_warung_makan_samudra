package com.enigma.livecode_wms.service.impl;

import com.enigma.livecode_wms.constant.ETransactionType;
import com.enigma.livecode_wms.dto.request.TransactionRequest;
import com.enigma.livecode_wms.dto.response.BranchResponse;
import com.enigma.livecode_wms.dto.response.ProductResponse;
import com.enigma.livecode_wms.dto.response.TransactionDetailResponse;
import com.enigma.livecode_wms.dto.response.TransactionResponse;
import com.enigma.livecode_wms.entity.*;
import com.enigma.livecode_wms.repository.BranchRepository;
import com.enigma.livecode_wms.repository.ProductRepository;
import com.enigma.livecode_wms.repository.TransactionDetailRepository;
import com.enigma.livecode_wms.repository.TransactionRepository;
import com.enigma.livecode_wms.service.BranchService;
import com.enigma.livecode_wms.service.ProductPriceService;
import com.enigma.livecode_wms.service.ProductService;
import com.enigma.livecode_wms.service.TransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(rollbackOn = Exception.class)
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionDetailRepository transactionDetailRepository;
    private final ProductPriceService productPriceService;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final BranchRepository branchRepository;
    private final BranchService branchService;

    public static String generateReceiptNumber(String branchCode, int year, int sequenceNumber) {
        return String.format("%s-%d-%d", branchCode, year, sequenceNumber);
    }

    // Metode untuk mendapatkan nilai enum dari string
//    private ETransactionType getTransactionTypeFromString(String transactionType) {
//        return ETransactionType.fromString(transactionType)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid transaction type: " + transactionType));
//    }

    @Override
    public TransactionResponse createNewTransaction(TransactionRequest transactionRequest) {

        //Todo 2: convert transactionDetailRequest to transactionDetail
        List<TransactionDetail> transactionDetails = transactionRequest.getBillDetails().stream().map(transactionDetailRequest -> {

            //Todo 3: validasi product price
            ProductPrice productPrice = productPriceService.getById(transactionDetailRequest.getProductPriceId());
            return TransactionDetail.builder()
                    .productPrice(productPrice)
                    .quantity(transactionDetailRequest.getQuantity())
                    .build();
        }).toList();

        String branchCode = "BC";
        int currentYear = LocalDateTime.now().getYear();
        int sequenceNumber = 1;

        // Menggunakan nilai transactionType langsung dari TransactionRequest transaction type
        ETransactionType transactionType = transactionRequest.getTransactionType();

        String receiptNumber = generateReceiptNumber(branchCode, currentYear, sequenceNumber);

        // Todo 4: Create New transaction
        Transaction transaction = Transaction.builder()
                .receiptNumber(receiptNumber)
                .transDate(LocalDateTime.now())
                .transactionType(transactionType)
                .transactionDetails(transactionDetails)
                .build();
        transactionRepository.saveAndFlush(transaction);

        List<TransactionDetailResponse> transactionDetailResponses = transaction.getTransactionDetails().stream().map(transactionDetail -> {
            //Todo 5: set transaction dari transactionDetail setelah membuat transaction baru
            transactionDetail.setTransaction(transaction);
            System.out.println(transaction);

            //Todo 6: respon trancsationdetail
            ProductPrice currentProductPrice = transactionDetail.getProductPrice();
            return TransactionDetailResponse.builder()
                    .orderDetailId(transactionDetail.getId())
                    .quantity(transactionDetail.getQuantity())


                    //Todo 7: convert product ke productResponse(productPrice)
                    .productResponse(ProductResponse.builder()
                            .productId(currentProductPrice.getProduct().getId())
                            .productPriceId(currentProductPrice.getId())
                            .productCode(currentProductPrice.getProduct().getProductCode())
                            .productName(currentProductPrice.getProduct().getProductName())
                            .price(currentProductPrice.getPrice())
                            //Todo 8: convert store ke storeResponse(productPrice)
                            .branch(BranchResponse.builder()
                                    .id(currentProductPrice.getBranch().getId())
                                    .branchCode(currentProductPrice.getBranch().getBranchCode())
                                    .branchName(currentProductPrice.getBranch().getBranchName())
                                    .address(currentProductPrice.getBranch().getAddress())
                                    .phoneNumber(currentProductPrice.getBranch().getPhoneNumber())
                                    .build())
                            .build())
                    .build();
        }).toList();


        //todo: 10. return orderResponse
        return TransactionResponse.builder()
                .billId(transaction.getId())
                .receiptNumber(transaction.getReceiptNumber())
                .transDate(transaction.getTransDate())
                .transactionType(transaction.getTransactionType())
                .transactionDetails(transactionDetailResponses)
                .build();

    }

    @Override
    public TransactionResponse getTransactionById(String id) {
        // todo 1 : Mendapatkan transaksi dari repository berdasarkan ID
        Transaction transaction = transactionRepository.findById(id).orElse(null);

        if (transaction != null) {
            //todo 2:  Operasi pemetaan untuk mengonversi transaksi dan detail transaksi ke Response
            List<TransactionDetailResponse> transactionDetailResponses = transaction.getTransactionDetails().stream()
                    .map(transactionDetail -> {
                        ProductPrice currentProductPrice = transactionDetail.getProductPrice();
                        return TransactionDetailResponse.builder()
                                .orderDetailId(transactionDetail.getId())
                                .quantity(transactionDetail.getQuantity())
                                .productResponse(ProductResponse.builder()
                                        .productId(currentProductPrice.getProduct().getId())
                                        .productPriceId(currentProductPrice.getId())
                                        .productCode(currentProductPrice.getProduct().getProductCode())
                                        .productName(currentProductPrice.getProduct().getProductName())
                                        .price(currentProductPrice.getPrice())
                                        .branch(BranchResponse.builder()
                                                .id(currentProductPrice.getBranch().getId())
                                                .branchCode(currentProductPrice.getBranch().getBranchCode())
                                                .branchName(currentProductPrice.getBranch().getBranchName())
                                                .address(currentProductPrice.getBranch().getAddress())
                                                .phoneNumber(currentProductPrice.getBranch().getPhoneNumber())
                                                .build())
                                        .build())
                                .build();
                    })
                    .toList();

            //todo 3 : Operasi pemetaan untuk mengonversi transaksi ke Response
            return TransactionResponse.builder()
                    .billId(transaction.getId())
                    .receiptNumber(transaction.getReceiptNumber())
                    .transDate(transaction.getTransDate())
                    .transactionType(transaction.getTransactionType())
                    .transactionDetails(transactionDetailResponses)
                    .build();
        } else {
            return null;
        }
    }



    @Override
    public List<TransactionResponse> getAllTransaction() {

        return transactionRepository.findAll().stream()
                .map(transaction -> {
                    List<TransactionDetailResponse> transactionDetailResponses = transaction.getTransactionDetails().stream()
                            .map(transactionDetail -> {
                                //Todo 6: response transaction detail
                                ProductPrice currentProductPrice = transactionDetail.getProductPrice();
                                return TransactionDetailResponse.builder()
                                        .orderDetailId(transactionDetail.getId())
                                        .quantity(transactionDetail.getQuantity())
                                        //Todo 7: convert product ke productResponse(productPrice)
                                        .productResponse(ProductResponse.builder()
                                                .productId(currentProductPrice.getProduct().getId())
                                                .productPriceId(currentProductPrice.getId())
                                                .productCode(currentProductPrice.getProduct().getProductCode())
                                                .productName(currentProductPrice.getProduct().getProductName())
                                                .price(currentProductPrice.getPrice())
                                                //Todo 8: convert store ke storeResponse(productPrice)
                                                .branch(BranchResponse.builder()
                                                        .id(currentProductPrice.getBranch().getId())
                                                        .branchCode(currentProductPrice.getBranch().getBranchCode())
                                                        .branchName(currentProductPrice.getBranch().getBranchName())
                                                        .address(currentProductPrice.getBranch().getAddress())
                                                        .phoneNumber(currentProductPrice.getBranch().getPhoneNumber())
                                                        .build())
                                                .build())
                                        .build();
                            })
                            .toList();

                    return TransactionResponse.builder()
                            .billId(transaction.getId())
                            .receiptNumber(transaction.getReceiptNumber())
                            .transDate(transaction.getTransDate())
                            .transactionType(transaction.getTransactionType())
                            .transactionDetails(transactionDetailResponses)
                            .build();
                })
                .collect(Collectors.toList());
    }


    @Override
    public TransactionResponse getTotalSales(LocalDate startDate, LocalDate endDate) {
        return null;
    }
}
