package com.personal.iphonehouse.services;

import com.personal.iphonehouse.dtos.JwtResponseDTO;
import com.personal.iphonehouse.utils.DateUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    public static final String SECRET = "958c4cc9eab74ebd6596777715a7896a3f9f2352c53fa4fd2a215f0fc022b7d0";
    public static final int EXPIRATION_TIME = 1000 * 60 * 60 * 12; // 12 horas
//    public static final int EXPIRATION_TIME = 1000 * 60 * 5;
    // TODO Poner el tiempo de vencimiento que va
//    public static final int EXPIRATION_TIME = 1000 * 60 * 60; // 1 hora
//    public static final int EXPIRATION_TIME = 1000 * 60; // 1 min

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new DateUtil().utilDateNow());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public Boolean validateToken(String token) {
        if (isTokenExpired(token)) {
            return false;
        }

        final String username = extractUsername(token);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


    public JwtResponseDTO GenerateToken(String username){
        Map<String, Object> claims = new HashMap<>();
        Date expiration = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
        return new JwtResponseDTO(createToken(claims, username, expiration), expiration);
    }



    private String createToken(Map<String, Object> claims, String username, Date expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expiration)
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public String obtainTokenFromHeader(HttpServletRequest servletRequest) {
        String authorizationHeader = servletRequest.getHeader("Authorization");
        String accessToken = "";

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            accessToken = authorizationHeader.substring(7);
        }
        if (accessToken.isBlank() || accessToken.isEmpty()) {
            throw new RuntimeException("Token not found");
        }
        return accessToken;
    }

    public JwtResponseDTO refreshJwt(HttpServletRequest req) {
        String token = obtainTokenFromHeader(req);
        // 1. Verify the token
        Claims claims = parseToken(token);
        // 2. Extract the subject (userId) from the claims
        String username = claims.getSubject();

        // 3. Create a new token with a new expiration date
//        Map<String, Object> claims = new HashMap<>();
//        Date expiration = new Date(System.currentTimeMillis() * EXPIRATION_TIME);
        return GenerateToken(username);

    }

    private Claims parseToken(String token) {
        // Parse the token and extract the claims
        // You can use the Jwts library from JJWT (Java JWT)
        Key key = Keys.hmacShaKeyFor(SECRET.getBytes());
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}