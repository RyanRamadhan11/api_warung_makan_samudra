package com.enigma.livecode_wms.service.impl;

import com.enigma.livecode_wms.constant.ERole;
import com.enigma.livecode_wms.dto.request.AuthRequest;
import com.enigma.livecode_wms.dto.response.LoginResponse;
import com.enigma.livecode_wms.dto.response.RegisterResponse;
import com.enigma.livecode_wms.entity.*;
import com.enigma.livecode_wms.repository.AdminRepository;
import com.enigma.livecode_wms.repository.SellerRepository;
import com.enigma.livecode_wms.repository.UserCredentialRepository;
import com.enigma.livecode_wms.security.JwtUtil;
import com.enigma.livecode_wms.service.AuthService;
import com.enigma.livecode_wms.service.CustomerService;
import com.enigma.livecode_wms.service.RoleService;
import com.enigma.livecode_wms.util.ValidationUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserCredentialRepository userCredentialRepository;
    private final PasswordEncoder passwordEncoder;


    private final CustomerService customerService;
    private final RoleService roleService;

    private final AdminRepository adminRepository;
    private final SellerRepository sellerRepository;

    private final ValidationUtil validationUtil;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Transactional(rollbackOn = Exception.class)
    @Override
    public RegisterResponse registerCustomer(AuthRequest request) {
        try {
            //TODO 1 : set Role
            Role role = Role.builder()
                    .name(ERole.ROLE_CUSTOMER)
                    .build();

            //harus dikasih penampung
            Role role1 = roleService.getOrSave(role);

            //TODO 2 : set credential
            UserCredential userCredential = UserCredential.builder()
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(role1)
                    .build();

            userCredentialRepository.saveAndFlush(userCredential);

            //TODO 3 : set customer
            Customer customer = Customer.builder()
                    .userCredential(userCredential)
                    .name(request.getCustomerName())
                    .address(request.getAddress())
                    .email(request.getEmail())
                    .mobilePhone(request.getMobilePhone())
                    .build();

            customerService.createNewCustomer(customer);

            return RegisterResponse.builder()
                    .username(userCredential.getUsername())
                    .role(userCredential.getRole().getName().toString())
                    .build();

        }catch (DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "user already exist");
        }
    }


    @Transactional(rollbackOn = Exception.class)
    @Override
    public RegisterResponse registerSeller(AuthRequest request) {
        try {
            //TODO 1 : set Role
            Role role = Role.builder()
                    .name(ERole.ROLE_SELLER)
                    .build();

            //harus dikasih penampung
            Role role1 = roleService.getOrSave(role);


            //TODO 2 : set credential
            UserCredential userCredential = UserCredential.builder()
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(role1)
                    .build();

            userCredentialRepository.saveAndFlush(userCredential);

            //Todo 3 : set seller
            Seller seller = Seller.builder()
                    .sellerName(request.getUsername())
                    .email(request.getEmail())
                    .phoneNumber(request.getMobilePhone())
                    .userCredential(userCredential)
                    .build();

           sellerRepository.save(seller);

            return RegisterResponse.builder()
                    .username(userCredential.getUsername())
                    .role(userCredential.getRole().getName().toString())
                    .build();

        }catch (DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "seller already exist");
        }
    }


    @Transactional(rollbackOn = Exception.class)
    @Override
    public RegisterResponse registerAdmin(AuthRequest request) {
        try {
            //TODO 1 : set Role
            Role role = Role.builder()
                    .name(ERole.ROLE_ADMIN)
                    .build();

            //harus dikasih penampung
            Role role1 = roleService.getOrSave(role);


            //TODO 2 : set credential
            UserCredential userCredential = UserCredential.builder()
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(role1)
                    .build();

            userCredentialRepository.saveAndFlush(userCredential);

            //Todo 3 : set Admin
            Admin admin = Admin.builder()
                    .name(request.getUsername())
                    .email(request.getEmail())
                    .phoneNumber(request.getMobilePhone())
                    .userCredential(userCredential)
                    .build();

            adminRepository.save(admin);

            return RegisterResponse.builder()
                    .username(userCredential.getUsername())
                    .role(userCredential.getRole().getName().toString())
                    .build();

        }catch (DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "admin already exist");
        }
    }

    @Override
    public LoginResponse login(AuthRequest authRequest) {
        //tempat untuk logic login
        validationUtil.validate(authRequest);

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.getUsername().toLowerCase(),
                authRequest.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        AppUser appUser = (AppUser) authentication.getPrincipal();
        String token = jwtUtil.generateToken(appUser);

        return LoginResponse.builder()
                .token(token)
                .role(appUser.getRole().name())
                .build();
    }
}

