package com.enigma.livecode_wms.repository;

import com.enigma.livecode_wms.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product> {
    List<Product> findByProductPrices_Branch_Id(String branchId);
}

