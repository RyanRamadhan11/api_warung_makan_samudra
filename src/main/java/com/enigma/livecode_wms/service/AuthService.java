package com.enigma.livecode_wms.service;

import com.enigma.livecode_wms.dto.request.AuthRequest;
import com.enigma.livecode_wms.dto.response.LoginResponse;
import com.enigma.livecode_wms.dto.response.RegisterResponse;

public interface AuthService {
    RegisterResponse registerCustomer(AuthRequest request);

    RegisterResponse registerAdmin(AuthRequest request);

    RegisterResponse registerSeller(AuthRequest request);

    LoginResponse login(AuthRequest authRequest);
}
