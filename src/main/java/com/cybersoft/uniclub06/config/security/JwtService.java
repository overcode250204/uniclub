//package com.cybersoft.uniclub06.config.security;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.stereotype.Service;
//
//import java.security.Key;
//import java.util.function.Function;
//
//@Service
//public class JwtService {
////    public String extractUsername (String token) {
////        return extractClaim(token, Claims::getSubject);
////    }
////
////    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
////        final Claims claims = extractAllClamins(token);
////        return claimsResolver.apply(claims);
////    }
////
////    private static final String SECRET_KEY = "584fZG7Ol81uWwnGIVoNmdSJnCFKJsob";
////
////    private Claims extractAllClamins (String token) {
////        return Jwts
////                .parserBuilder()
////                .setSigningKey(getSignInKey())// sign for token
////                .build()// build token
////                .parseClaimsJws(token)// claim
////                .getBody();
////    }
////
////    private Key getSignInKey() {
////        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
////        return Keys.hmacShaKeyFor(keyBytes);
////    }
//}
