package com.dev.cinema.security;

import com.dev.cinema.model.JwtToken;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.compression.GzipCompressionCodec;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;

@Service
public class JWTTokenService implements Clock {
    private static final GzipCompressionCodec COMPRESSION_CODEC = new GzipCompressionCodec();

    private final String issuer;
    private final int expirationSec;
    private final int clockSkewSec;
    private final String secretKey;

    JWTTokenService(@Value("${jwt.issuer}") String issuer,
                    @Value("${jwt.expiration-sec}") int expirationSec, //длительность токена сутки
                    @Value("${jwt.clock-skew-sec}") int clockSkewSec,
                    @Value("${jwt.secret-key}") String secretKey) {
        this.issuer = issuer;
        this.expirationSec = expirationSec;
        this.clockSkewSec = clockSkewSec;
        this.secretKey = secretKey;
    }

    @Override
    public Date now() {
        return DateTime.now().toDate();
    }

    public JwtToken permanent(Map<String, String> attributes) {
        return newToken(attributes, 0);
    }

    public JwtToken expiring(Map<String, String> attributes) {
        return newToken(attributes, expirationSec);
    }

    private JwtToken newToken(Map<String, String> attributes, int expirationSec) {
        DateTime now = DateTime.now();
        Claims claims = Jwts
                .claims()
                .setIssuer(issuer)
                .setIssuedAt(now.toDate());

        if (expirationSec > 0) {
            final DateTime expiresAt = now.plusSeconds(expirationSec);
            claims.setExpiration(expiresAt.toDate());
        }
        claims.putAll(attributes);

        String token = Jwts
                .builder()
                .setClaims(claims)
                .signWith(HS256, secretKey)
                .compressWith(COMPRESSION_CODEC)
                .compact();

        return new JwtToken(token);
    }

    public Map<String, String> verify(JwtToken token) {
        JwtParser parser = Jwts
                .parser()
                .requireIssuer(issuer)
                .setClock(this)
                .setAllowedClockSkewSeconds(clockSkewSec)
                .setSigningKey(secretKey);
        try {
            Claims claims = parser.parseClaimsJws(token.getToken()).getBody();
            Map<String, String> body = new HashMap<>();
            for (final Map.Entry<String, Object> e : claims.entrySet()) {
                body.put(e.getKey(), String.valueOf(e.getValue()));
            }
            return body;
        } catch (IllegalArgumentException | JwtException e) {
            return new HashMap<>();
        }
    }
}
