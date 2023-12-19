package com.enigma.livecode_wms.service.impl;

import com.enigma.livecode_wms.entity.Role;
import com.enigma.livecode_wms.repository.RoleRepository;
import com.enigma.livecode_wms.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    //inject
    private final RoleRepository repository;

    @Override
    public Role getOrSave(Role role) {
        Optional<Role> optionalRole = repository.findByName(role.getName());
        //jika ada di DB di get
        if (!optionalRole.isEmpty()){
            return optionalRole.get();
        }

        return repository.save(role);
    }
}

