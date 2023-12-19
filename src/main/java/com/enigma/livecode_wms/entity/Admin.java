package com.enigma.livecode_wms.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "m_admin")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "email", unique = true, nullable = false, length = 30)
    private String email;

    @Column(name = "phone", unique = true, nullable = false, length = 30)
    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "user_credential_id")
    private UserCredential userCredential;
}

