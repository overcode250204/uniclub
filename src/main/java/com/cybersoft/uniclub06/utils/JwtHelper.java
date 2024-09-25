package com.cybersoft.uniclub06.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtHelper {
    @Value("${jwts.key}")
    private String key;

    private int expirationTime = 8 * 60 * 60 * 1000;

    public String generateToken(String data) {//sinh ra token
        //biến key kiểu String đã lưu trữ trước đó thành secretkey
        SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
        Date currentDate = new Date();// tu động lấy ngày hiện tại
        long millisecondFuture = currentDate.getTime() + expirationTime;
        Date dateFuture = new Date(millisecondFuture);


        String token = Jwts.builder().signWith(secretKey).expiration(dateFuture).subject(data).compact();
        return token;
    }

    public String decodeToken(String token) {
        String decodeToken = "";
        try {
            SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
            decodeToken = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (Exception e) {
            e.getMessage();
        }

        return decodeToken;
    }





}
