package com.alkemy.ong.jwt;

import com.alkemy.ong.models.MyUserDetails;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private int expiration;

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(Authentication authentication) {
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        List<String> roles = myUserDetails.getCredentials();

        return Jwts.builder()
                .setSubject(myUserDetails.getUsername())
                .claim("firstName", myUserDetails.getFirstName())
                .claim("lastName", myUserDetails.getLastName())
                .claim("id", myUserDetails.getId())
                .claim("roles",roles)
                .claim("photo", myUserDetails.getPhoto())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + (expiration * 100)))
                .signWith(SignatureAlgorithm.HS512,secret.getBytes())
                .compact();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret.getBytes(Charset.forName("UTF-8")))
                .parseClaimsJws(token.replace("{", "").replace("}", "")).getBody();
    }

    public List<String> extractCredentials(String token) {
        //return (List<String>) extractAllClaims(token).get("rolesDos");
        Claims allClaims =  extractAllClaims(token);
        List<String> roleClaims = (List<String>) allClaims.get("roles");
        return roleClaims;
    }

    public String extractId(String token) {
        return (String) extractAllClaims(token).get("id");
    }
    
    public String getToken(HttpServletRequest httpServletRequest){
        String header = httpServletRequest.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer"))
            return header.replace("Bearer","");
        return null;
    }

    public Boolean validateToken(String token) {

        try {
            Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token);
            return true;
        }catch (MalformedJwtException e){
            logger.error("token mal formado");
        }catch (UnsupportedJwtException e){
            logger.error("token no soportado");
        }catch (ExpiredJwtException e){
            logger.error("token expirado");
        }catch (IllegalArgumentException e){
            logger.error("token vacio");
        }catch (SignatureException e){
            logger.error("token mal formado");
        }

        return false;

    }

}

