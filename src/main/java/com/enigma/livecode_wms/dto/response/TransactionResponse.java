package com.enigma.livecode_wms.dto.response;

import com.enigma.livecode_wms.constant.ETransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class TransactionResponse {

        private String billId;
        private String receiptNumber;
        private LocalDateTime transDate;
        private ETransactionType transactionType;
        private List<TransactionDetailResponse> transactionDetails;
}
