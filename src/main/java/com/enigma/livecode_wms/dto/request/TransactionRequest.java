package com.enigma.livecode_wms.dto.request;


import com.enigma.livecode_wms.constant.ETransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class TransactionRequest {
    private ETransactionType transactionType;
    private List<TransactionDetailRequest> billDetails;
}
