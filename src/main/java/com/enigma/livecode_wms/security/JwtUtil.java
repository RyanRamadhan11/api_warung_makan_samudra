package com.enigma.livecode_wms.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.enigma.livecode_wms.entity.AppUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {


    @Value("${app.wms.jwt.jwt-secret}")
    private String jwtSecret;

    @Value("${app.wms.jwt.app-name}")
    private String appName;

    @Value("${app.wms.jwt.jwtExpirationInSecond}")
    private Long jwtExpirationInSecond;

    public String generateToken(AppUser appUser) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes(StandardCharsets.UTF_8));
            String token = JWT.create()
                    .withIssuer(appName) //info untuk application nama yang kita buat
                    .withSubject(appUser.getId()) //menentukan object yang akan dibuat biasanya dari id
                    .withExpiresAt(Instant.now().plusSeconds(jwtExpirationInSecond)) //menetapkan waktu kadaluarsa token nanti, dalam sini kadaluarsanya adalah 60 detik setelah dibuat
                    .withIssuedAt(Instant.now()) //menetapkan waktu suatu token dibuat
                    .withClaim("role", appUser.getRole().name()) //menambahkan claim atau into nama pengguna
                    .sign(algorithm); //ini itu seperti ttd kontrak bahwa algoritma yang kita pakai itu udah pasti HMAC256
            return token;
        } catch (JWTCreationException e) {
            throw new RuntimeException();
        }
    }

    public boolean verifyJwtToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes(StandardCharsets.UTF_8));
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getIssuer().equals(appName);
        } catch (JWTCreationException e) {
            throw new RuntimeException();
        }
    }

    public Map<String, String> getUserInfoByToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes(StandardCharsets.UTF_8));
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);

            Map<String, String> userInfo = new HashMap<>();
            userInfo.put("userId", decodedJWT.getSubject());
            userInfo.put("role", decodedJWT.getClaim("role").asString());
            return userInfo;

        } catch (JWTVerificationException e) {
            throw new RuntimeException();
        }
    }

}
