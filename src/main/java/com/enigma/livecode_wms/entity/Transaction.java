package com.enigma.livecode_wms.entity;

import com.enigma.livecode_wms.constant.ETransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "t_transaction")
//untuk geter setter
//@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "transaction_type", nullable = false)
    private ETransactionType transactionType;

    @Column(name = "trans_date", nullable = false)
    private LocalDateTime transDate;

    @Column(name = "receipt_number", nullable = false)
    private String receiptNumber;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.PERSIST)
    private List<TransactionDetail> transactionDetails;

}
