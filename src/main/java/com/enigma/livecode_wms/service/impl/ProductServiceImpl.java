package com.enigma.livecode_wms.service.impl;

import com.enigma.livecode_wms.constant.ETransactionType;
import com.enigma.livecode_wms.dto.request.ProductRequest;
import com.enigma.livecode_wms.dto.response.BranchResponse;
import com.enigma.livecode_wms.dto.response.ProductResponse;
import com.enigma.livecode_wms.entity.Branch;
import com.enigma.livecode_wms.entity.Product;
import com.enigma.livecode_wms.entity.ProductPrice;
import com.enigma.livecode_wms.repository.ProductPriceRepository;
import com.enigma.livecode_wms.repository.ProductRepository;
import com.enigma.livecode_wms.service.BranchService;
import com.enigma.livecode_wms.service.ProductPriceService;
import com.enigma.livecode_wms.service.ProductService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final BranchService branchService;
    private final ProductPriceService productPriceService;

    @Override
    public Product create(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public Product getById(String id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Product update(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void delete(String id) {
        productRepository.deleteById(id);
    }

    @Override
    public ProductResponse createProductAndProductPrice(ProductRequest productRequest) {
        BranchResponse branchResponse = branchService.getById(productRequest.getBranchId().getId());

        Product product = Product.builder()
                .productCode(productRequest.getProductCode())
                .productName(productRequest.getProductName())
                .build();
        productRepository.saveAndFlush(product);

        ProductPrice productPrice = ProductPrice.builder()
                .price(productRequest.getPrice())
                .isActive(true)
                .product(product)
                .branch(Branch.builder()
                        .id(branchResponse.getId())
                        .build())
                .build();

        productPriceService.create(productPrice);

        return ProductResponse.builder()
                .productId(product.getId())
                .productPriceId(productPrice.getId())
                .productCode(product.getProductCode())
                .productName(product.getProductName())
                .price(productPrice.getPrice())
                .branch(BranchResponse.builder()
                        .id(branchResponse.getId())
                        .branchCode(branchResponse.getBranchCode())
                        .branchName(branchResponse.getBranchName())
                        .address(branchResponse.getAddress())
                        .phoneNumber(branchResponse.getPhoneNumber())
                        .build())
                .build();

    }

    @Override
    public ProductResponse updateProductAndProductPrice(ProductRequest productRequest) {
        BranchResponse branchResponse = branchService.getById(productRequest.getBranchId().getId());

        Optional<Product> optionalProduct = productRepository.findById(productRequest.getId());

        if (optionalProduct.isPresent()) {
            // Data produk yang ingin diupdate ditemukan
            Product product = optionalProduct.get();

            // Lakukan pembaruan properti produk
            product.setProductCode(productRequest.getProductCode());
            product.setProductName(productRequest.getProductName());

            // Simpan perubahan produk
            productRepository.save(product);

            // Lakukan pembaruan atau penambahan harga produk
            ProductPrice productPrice = product.getProductPrices().isEmpty()
                    ? new ProductPrice()
                    : product.getProductPrices().get(0); // Ambil harga produk pertama

            // Update properti harga produk
            productPrice.setPrice(productRequest.getPrice());
            productPrice.setActive(true);
            productPrice.setBranch(Branch.builder().id(branchResponse.getId()).build());
            productPrice.setProduct(product);

            // Simpan perubahan harga produk
            productPriceService.createOrUpdate(productPrice);

            // Kemudian kembalikan respons yang sesuai
            return ProductResponse.builder()
                    .productId(product.getId())
                    .productPriceId(productPrice.getId())
                    .productCode(product.getProductCode())
                    .productName(product.getProductName())
                    .price(productPrice.getPrice())
                    .branch(BranchResponse.builder()
                            .id(branchResponse.getId())
                            .branchCode(branchResponse.getBranchCode())
                            .branchName(branchResponse.getBranchName())
                            .address(branchResponse.getAddress())
                            .phoneNumber(branchResponse.getPhoneNumber())
                            .build())
                    .build();
        } else {
            // Produk tidak ditemukan
            return null;
        }

    }

    @Override
    public ProductResponse getProductAndProductPriceByBranchId(String id) {
        // Mendapatkan branch berdasarkan ID
        BranchResponse branchResponse = branchService.getById(id);

        // Mendapatkan produk berdasarkan branch ID
        List<Product> products = productRepository.findByProductPrices_Branch_Id(id);

        // Mendapatkan harga produk pertama yang aktif jika ada
        Optional<ProductPrice> productPrice = products.stream()
                .flatMap(product -> product.getProductPrices().stream())
                .filter(ProductPrice::isActive)
                .findFirst();

        // Membuat respons produk jika harga produk aktif ditemukan
        return productPrice.map(price ->
                        ProductResponse.builder()
                                .productId(price.getProduct().getId())
                                .productPriceId(price.getId())
                                .productCode(price.getProduct().getProductCode())
                                .productName(price.getProduct().getProductName())
                                .price(price.getPrice())
                                .branch(BranchResponse.builder()
                                        .id(branchResponse.getId())
                                        .branchCode(branchResponse.getBranchCode())
                                        .branchName(branchResponse.getBranchName())
                                        .address(branchResponse.getAddress())
                                        .phoneNumber(branchResponse.getPhoneNumber())
                                        .build())
                                .build())
                .orElse(null); // Mengembalikan null jika tidak ada harga produk aktif
    }

    @Override
    public void deleteProductAndProductPrice(String id) {
        Optional<Product> optionalProduct = productRepository.findById(id);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            // Dapatkan daftar harga produk
            List<ProductPrice> productPrices = product.getProductPrices();

            // Hapus masing-masing harga produk
            for (ProductPrice productPrice : productPrices) {
                productPriceService.delete(productPrice.getId());
            }

            // Hapus produk
            productRepository.deleteById(id);
        }
    }




    @Override
    public Page<ProductResponse> getAllProductByCodeNamePrice(String productCode, String productName, Long minPrice, Long maxPrice, Integer page, Integer size) {

        Specification<Product> specification = (root, query, criteriaBuilder) -> {

            Join<Product, ProductPrice> productPrices = root.join("productPrices");
            List<Predicate> predicates = new ArrayList<>();

            if (productCode != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("productCode")), "%" + productCode.toLowerCase() + "%"));
            }
            if (productName != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("productName")), "%" + productName.toLowerCase() + "%"));
            }
            if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(productPrices.get("price"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(productPrices.get("price"), maxPrice));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        Pageable pageable = PageRequest.of(page, size);

        Page<Product> products = productRepository.findAll(specification, pageable);

        List<ProductResponse> productResponses = new ArrayList<>();

        for (Product product : products.getContent()) {

            Optional<ProductPrice> productPrice = product.getProductPrices()
                    .stream()
                    .filter(ProductPrice::isActive).findFirst();

            if (productPrice.isEmpty()) continue;

            Branch branch = productPrice.get().getBranch();

            productResponses.add(ProductResponse.builder()
                    .productId(product.getId())
                    .productPriceId(productPrice.get().getId())
                    .productCode(product.getProductCode())
                    .productName(product.getProductName())
                    .price(productPrice.get().getPrice())
                    .branch(BranchResponse.builder()
                            .id(branch.getId())
                            .branchCode(branch.getBranchCode())
                            .branchName(branch.getBranchName())
                            .address(branch.getAddress())
                            .phoneNumber(branch.getPhoneNumber())
                            .build())
                    .build());

        }
        return new PageImpl<>(productResponses, pageable, products.getTotalElements());
    }

}
