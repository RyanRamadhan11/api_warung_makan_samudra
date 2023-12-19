package com.enigma.livecode_wms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ProductResponse {
    private String productId;
    private String productPriceId;
    private String productCode;
    private String productName;
    private Long price;
    private BranchResponse branch;





    public ProductResponse(List<ProductResponse> productResponses) {

    }

}
