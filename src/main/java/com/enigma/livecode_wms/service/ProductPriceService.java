package com.enigma.livecode_wms.service;

import com.enigma.livecode_wms.entity.ProductPrice;

public interface ProductPriceService {
    ProductPrice create(ProductPrice productPrice);

    ProductPrice createOrUpdate(ProductPrice productPrice);

    void delete(String id);

    ProductPrice getById(String id);

//    ProductPrice findProductPriceIsActive(String productId, Boolean active);
}