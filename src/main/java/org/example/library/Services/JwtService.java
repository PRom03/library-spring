package org.example.library.Services;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.example.library.MyUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;


@Service
public class JwtService {

    private static final long EXPIRATION_TIME = 1000 * 60 * 60 ;
    private final Key key = Keys.hmacShaKeyFor(Base64.getDecoder().decode("bpgLU4UeHts67/e+jOsUlRG13qoBQFV0pcA5lS+7DSM="));
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    public boolean validateToken(String token, MyUserDetails userDetails) {
        String email = extractEmail(token);
        return email.equals(userDetails.getEmail()) && !isExpired(token);
    }

    public boolean isExpired(String token) {
        Date exp = Jwts.parser().setSigningKey(key)
                .parseClaimsJws(token).getBody().getExpiration();
        return exp.before(new Date());
    }
}
