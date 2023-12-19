package com.enigma.livecode_wms.controller;

import com.enigma.livecode_wms.constant.AppPath;
import com.enigma.livecode_wms.dto.request.AuthRequest;
import com.enigma.livecode_wms.dto.response.CommonResponse;
import com.enigma.livecode_wms.dto.response.LoginResponse;
import com.enigma.livecode_wms.dto.response.RegisterResponse;
import com.enigma.livecode_wms.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AppPath.AUTH)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody AuthRequest authRequest) {
        RegisterResponse registerResponse = authService.registerAdmin(authRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.<RegisterResponse>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Successfully created register new Admin")
                        .data(registerResponse)
                        .build());
    }

    @PostMapping("/register/customer")
    public ResponseEntity<?> registerCustomer(@RequestBody AuthRequest authRequest) {
        RegisterResponse registerResponse = authService.registerCustomer(authRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.<RegisterResponse>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Successfully created register new customer")
                        .data(registerResponse)
                        .build());
    }

    @PostMapping("/register/seller")
    public ResponseEntity<?> registerSeller(@RequestBody AuthRequest authRequest) {
        RegisterResponse registerResponse = authService.registerSeller(authRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.<RegisterResponse>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Successfully created register new seller")
                        .data(registerResponse)
                        .build());
    }



    @PostMapping("/login")
    public ResponseEntity<?> loginCustomer(@RequestBody AuthRequest authRequest) {
        LoginResponse loginResponse = authService.login(authRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.<LoginResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully Login")
                        .data(loginResponse)
                        .build());
    }
}

