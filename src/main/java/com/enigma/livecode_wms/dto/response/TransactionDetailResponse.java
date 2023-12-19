package com.enigma.livecode_wms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class TransactionDetailResponse {
    private String orderDetailId;
    private ProductResponse productResponse;

    private Integer quantity;
    private Integer totalSales;
}
