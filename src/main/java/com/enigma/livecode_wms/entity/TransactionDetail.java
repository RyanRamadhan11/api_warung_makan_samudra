package com.enigma.livecode_wms.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "t_transaction_detail")
//untuk geter setter
//@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class TransactionDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @ManyToOne
    @JoinColumn(name = "product_price_id")
    private ProductPrice productPrice;

    @Column(name = "quantity")
    private Integer quantity;
}
