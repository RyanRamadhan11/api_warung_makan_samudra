package com.enigma.livecode_wms.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "m_seller")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)

public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "seller_name", nullable = false, length = 30)
    private String sellerName;

    @Column(name = "email", unique = true, nullable = false, length = 30)
    private String email;

    @Column(name = "phone_number", unique = true, nullable = false, length = 30)
    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "user_credential_id")
    private UserCredential userCredential;
}

