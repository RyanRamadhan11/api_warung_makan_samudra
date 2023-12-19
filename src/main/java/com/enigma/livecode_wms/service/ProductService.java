package com.enigma.livecode_wms.service;

import com.enigma.livecode_wms.dto.request.ProductRequest;
import com.enigma.livecode_wms.dto.response.ProductResponse;
import com.enigma.livecode_wms.entity.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    Product create(Product product);

    List<Product> getAll();

    Product getById(String id);

    Product update(Product product);

    void delete(String id);

    ProductResponse createProductAndProductPrice(ProductRequest productRequest);

    ProductResponse updateProductAndProductPrice(ProductRequest productRequest);

    ProductResponse getProductAndProductPriceByBranchId(String id);

    void deleteProductAndProductPrice(String id);

    Page<ProductResponse> getAllProductByCodeNamePrice(String productCode, String productName, Long minPrice, Long maxPrice, Integer page, Integer size);
}
