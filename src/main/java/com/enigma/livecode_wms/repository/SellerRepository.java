package com.enigma.livecode_wms.repository;

import com.enigma.livecode_wms.entity.Admin;
import com.enigma.livecode_wms.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends JpaRepository<Seller, String> {
}
