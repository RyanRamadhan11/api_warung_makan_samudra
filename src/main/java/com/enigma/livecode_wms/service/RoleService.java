package com.enigma.livecode_wms.service;

import com.enigma.livecode_wms.entity.Role;

public interface RoleService {
    Role getOrSave(Role role);
}
