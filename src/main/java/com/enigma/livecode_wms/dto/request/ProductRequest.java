package com.enigma.livecode_wms.dto.request;

import com.enigma.livecode_wms.dto.response.BranchResponse;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ProductRequest {
    private String id;

    @NotBlank(message = "product code is required")
    private String productCode;

    @NotBlank(message = "product name is required")
    private String productName;

    @NotBlank(message = "product price is required")
    @Min(value = 0,message = "product price must be greater than 0")
    private Long price;

    @NotBlank(message = "branchId is required")
    private BranchResponse branchId;
}
