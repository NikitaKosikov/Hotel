package com.kosnik.service.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Service
public class JwtService {

    private final String jwtSecretKey = "secret";
    private static final String TOKEN_AUTHORITIES = "authorities";

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public boolean hasClaim(String token, String claimName){
        final Claims claims = extractAllClaims(token);
        return claims.get(claimName)!=null;
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).getBody();
    }


    public String generateToken(UserDetails userDetails){
        return createToken(new HashMap<>(), userDetails);
    }

    public String generateToken( Map<String, Object> extraClaims, UserDetails userDetails){
        return createToken(extraClaims, userDetails);
    }

    private String createToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .claim(TOKEN_AUTHORITIES, userDetails.getAuthorities())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(24)))
                .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateToken(OAuth2AuthenticationToken authentication){
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return Jwts.builder()
                .setSubject(authentication.getPrincipal().getName())
                .claim(TOKEN_AUTHORITIES, authorities)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(24)))
                .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
                .compact();
    }
}
