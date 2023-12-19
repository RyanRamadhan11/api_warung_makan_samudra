package com.enigma.livecode_wms.service;

import com.enigma.livecode_wms.entity.AppUser;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    AppUser loadUserByUserId(String id);
}
