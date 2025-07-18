package com.example.ElearningSystem.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JWTService {
    @Value("${jwt.secret}")
private String secretKey;
    private SecretKeySpec keySpec;

    @PostConstruct
    public void init(){
        byte[] decodeKey=Base64.getDecoder().decode(secretKey);
        this.keySpec=new SecretKeySpec(decodeKey,"HmacSHA256");
    }


    public String generateToken(String username, Collection<? extends GrantedAuthority> authority) {
        System.out.println(secretKey);
        Map<String,Object> claims=new HashMap<>();
        claims.put("authorities",authority.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        claims.put("username",username);
       return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+30 * 60 * 1000))
               .signWith(getSignKey())
                .compact();



    }

    public SecretKey getSignKey() {
//        byte[] keyBytes= Decoders.BASE64.decode(secretKey);//decode the secretKey which is encoded by base64
//        return Keys.hmacShaKeyFor(keyBytes);
        //generate HMAC SHA based secret key using provided keybytes
        //this will help to sign JWTs using HMAC algorithms like HS256
        return keySpec;
    }

    public String extractUserName(String token) {
    return extractClaim(token, Claims::getSubject);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
    final String username=extractUserName(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


    private <T> T extractClaim(String token, Function<Claims,T> claimResolver){
    final Claims claims=extractAllClaims(token);
    return claimResolver.apply(claims);

    }
    private Claims extractAllClaims(String token) {
        System.out.println(getSignKey());
        try {
            return Jwts.parser()
                    .verifyWith(getSignKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException | MalformedJwtException |
                 UnsupportedJwtException | IllegalArgumentException e) {
            throw new RuntimeException("Invalid JWT Token", e);
        }}
    private boolean isTokenExpired(String token){
    return extractExpiration(token).before(new Date());
    }
    private Date extractExpiration(String token){
return extractClaim(token, Claims::getExpiration);
    }

public List<String> extractRole(String token){
    return extractClaim(token, c->c.get("authorities",List.class));
}

}
