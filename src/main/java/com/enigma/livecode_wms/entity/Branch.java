package com.enigma.livecode_wms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "m_branch")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "branch_code", unique = true, nullable = false, length = 100)
    private String branchCode;

    @Column(name = "branch_name", nullable = false, length = 200)
    private String branchName;

    @Column(name = "address", nullable = false, length = 200)
    private String address;

    @Column(name = "phone_number",  unique = true, nullable = false, length = 200)
    private String phoneNumber;

}
