package com.enigma.livecode_wms.security;

import com.enigma.livecode_wms.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter { //OncePerRequestFilter untuk melakukan sekali dalam 1 request

    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try{
            //validasi token jwt
            String headerAuth = request.getHeader("Authorization");
            String token = null;
            if (headerAuth != null && headerAuth.startsWith("Bearer ")){
                token = headerAuth.substring(7);
            }
            if(token != null && jwtUtil.verifyJwtToken(token)){

                //set auth ke spring security
                Map<String,String> userInfo = jwtUtil.getUserInfoByToken(token);
                UserDetails user = userService.loadUserByUserId(userInfo.get("userId"));

                //validasi token
                UsernamePasswordAuthenticationToken authenticationFToken = new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());

                //menambahkan informasi berupa alamat IP ke host dalam bentuk security
                authenticationFToken.setDetails(new WebAuthenticationDetailsSource());

                //menyimpan auth ke spring contex
                SecurityContextHolder.getContext().setAuthentication(authenticationFToken);
            }
        }catch (Exception e){
            e.getLocalizedMessage();
        }
        filterChain.doFilter(request,response);
    }
}
