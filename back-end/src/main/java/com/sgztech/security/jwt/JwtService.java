package com.sgztech.security.jwt;

import com.sgztech.domain.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JwtService {

    @Value("${security.jwt.expiracao}")
    private String expiration;

    @Value("${security.jwt.chave-assinatura}")
    private String signatureKey;

    public String generateToken(User user) {
        long exp = Long.parseLong(expiration);
        LocalDateTime expirationDateTime = LocalDateTime.now().plusMinutes(exp);
        Instant instant = expirationDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date date = Date.from(instant);

        return Jwts
                .builder()
                .setSubject(user.getEmail())
                //.setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, signatureKey)
                .compact();
    }

    private Claims getClaims(String token) throws ExpiredJwtException {
        return Jwts
                .parser()
                .setSigningKey(signatureKey)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isValidToken(String token) {
        try {
            /*Claims claims = getClaims(token);
            Date expirationDate = claims.getExpiration();
            LocalDateTime dateTime =
                    expirationDate.toInstant()
                            .atZone(ZoneId.systemDefault()).toLocalDateTime();
            return !LocalDateTime.now().isAfter(dateTime); */
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUserEmail(String token) throws ExpiredJwtException {
        return getClaims(token).getSubject();
    }
}