package com.enigma.livecode_wms.service.impl;

import com.enigma.livecode_wms.entity.ProductPrice;
import com.enigma.livecode_wms.repository.ProductPriceRepository;
import com.enigma.livecode_wms.service.ProductPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ProductPriceServiceImpl implements ProductPriceService {
    private final ProductPriceRepository productPriceRepository;

    @Override
    public ProductPrice create(ProductPrice productPrice) {
        return productPriceRepository.save(productPrice);
    }

    @Override
    public ProductPrice createOrUpdate(ProductPrice productPrice) {
        if (productPrice.getId() == null) {
            // Jika ID belum ada, itu adalah produk harga baru yang perlu dibuat
            return productPriceRepository.save(productPrice);
        } else {
            // Jika ID sudah ada, itu adalah produk harga yang perlu diperbarui
            return productPriceRepository.saveAndFlush(productPrice);
        }
    }

    @Override
    public ProductPrice getById(String id) {
        return productPriceRepository.findById(id).orElseThrow();
    }

    @Override
    public void delete(String id) {
        productPriceRepository.deleteById(id);
    }

//    @Override
//    public ProductPrice findProductPriceIsActive(String productId, Boolean active) {
//        return productPriceRepository.findByProduct_IdAndIsActive(productId, active).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found"));
//    }
}
