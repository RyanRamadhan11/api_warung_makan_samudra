package com.enigma.livecode_wms.repository;

import com.enigma.livecode_wms.constant.ERole;
import com.enigma.livecode_wms.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}
