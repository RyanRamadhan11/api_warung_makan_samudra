package com.enigma.livecode_wms.controller;

import com.enigma.livecode_wms.constant.AppPath;
import com.enigma.livecode_wms.dto.request.ProductRequest;
import com.enigma.livecode_wms.dto.response.CommonResponse;
import com.enigma.livecode_wms.dto.response.PagingResponse;
import com.enigma.livecode_wms.dto.response.ProductResponse;
import com.enigma.livecode_wms.entity.Product;
import com.enigma.livecode_wms.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(AppPath.PRODUCT)
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createProduct(@RequestBody ProductRequest productRequest){
        ProductResponse productResponse = productService.createProductAndProductPrice(productRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.<ProductResponse>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Successfully created new product")
                        .data(productResponse)
                        .build());
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    public ResponseEntity<?> updateProduct(@RequestBody ProductRequest productRequest){
        ProductResponse productResponse = productService.updateProductAndProductPrice(productRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.<ProductResponse>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Successfully update product")
                        .data(productResponse)
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductByBranchId(@PathVariable String id) {
        ProductResponse productResponse = productService.getProductAndProductPriceByBranchId(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.<ProductResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully get Product by branch id")
                        .data(productResponse)
                        .build());

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductAndPrice(@PathVariable String id) {
        productService.delete(id);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Successfully Delete Product and Price")
                        .data(HttpStatus.OK)
                        .build());
    }

    @GetMapping
    public ResponseEntity<?> getAllProductPage(
            @RequestParam(name = "productCode", required = false) String productCode,
            @RequestParam(name = "productName", required = false) String productName,
            @RequestParam(name = "minPrice", required = false) Long minPrice,
            @RequestParam(name = "maxPrice", required = false) Long maxPrice,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "5") Integer size
    ){

        Page<ProductResponse> productResponses = productService.getAllProductByCodeNamePrice(productCode, productName, minPrice, maxPrice, page, size);
        PagingResponse pagingResponse = PagingResponse.builder()
                .currentPage(page)
                .totalPage(productResponses.getTotalPages())
                .size(size)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Successfully get all product")
                        .data(productResponses.getContent())
                        .paging(pagingResponse)
                        .build());
    }
}
