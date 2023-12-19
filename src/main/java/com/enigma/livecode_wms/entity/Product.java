package com.enigma.livecode_wms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Table(name = "m_product")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "product_code", unique = true, nullable = false, length = 100)
    private String productCode;

    @Column(name = "product_name", nullable = false, length = 100)
    private String productName;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductPrice> productPrices;

}
