package com.alkemy.ong.jwt;

import com.alkemy.ong.models.UserEntity;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtUtils {

    @Value("${SECRET}")
    private String secret;

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    public String extractId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractEmail(String token) {
        return (String) extractAllClaims(token).get("email");
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret.getBytes(Charset.forName("UTF-8")))
                .parseClaimsJws(token.replace("{", "").replace("}", "")).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserEntity userEntity) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", userEntity.getEmail());
        claims.put("rolesIds", userEntity.getRoleIds());
        claims.put("photo", userEntity.getPhoto());
        return createToken(claims, userEntity.getId());
    }

    private String createToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, secret.getBytes(Charset.forName("UTF-8"))).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {

        final String username = extractId(token);

        try {
            if(username.equals(userDetails.getUsername()) && !isTokenExpired(token)){
                Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token);
                return true;
            }
        } catch (SignatureException ex) {
            logger.info("Error signing JWT token: " + ex.getMessage());
        } catch (MalformedJwtException ex) {
            logger.info("Token malformed: " + ex.getMessage());
        } catch (ExpiredJwtException ex) {
            logger.info("Token has expired: " + ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            logger.info("JWT token not supported: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            logger.info("JWT claim empty");
        }
        return false;

    }

}

