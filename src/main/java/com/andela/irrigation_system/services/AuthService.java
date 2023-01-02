package com.andela.irrigation_system.services;


import com.andela.irrigation_system.config.Permissions;
import com.andela.irrigation_system.exception.AuthException;
import com.andela.irrigation_system.model.UserForm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.List;
import java.util.Set;


@Slf4j
@Component
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;

    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;
    private static final String TOKEN_ISSUER = "andela.com";
    private static final String PERMISSIONS_KEY = "permissions";

    @Value("${app.jwt.key:please-use-hashicorp-vault-or-any-other-private-storage-for-all-related-stuff}")
    private String cryptoKey;
    private SecretKey secretKey;
    @Value("${app.jwt.expired.minutes:10}")
    private Integer accessTokenExpiredMinutes;

    @PostConstruct
    public void init() {
        this.secretKey = new SecretKeySpec(cryptoKey.getBytes(), SIGNATURE_ALGORITHM.getJcaName());
    }

    public String issueAccessToken(UserForm user, Set<String> permissions) {
        return userService.findByEmail(user.getEmail()).map(u -> {
            final DateTime now = DateTime.now();
            return Jwts.builder()
                    .setIssuer(TOKEN_ISSUER)
                    .setIssuedAt(now.toDate())
                    .setSubject(user.getEmail())
                    .setExpiration(now.plusMinutes(accessTokenExpiredMinutes).toDate())
                    .claim(PERMISSIONS_KEY, permissions)
                    .signWith(secretKey)
                    .compact();
        }).orElseThrow(() -> new AuthException("User doesn't exists", "NOT_FOUND", HttpStatus.NOT_FOUND));
    }

    @SuppressWarnings("unchecked")
    public boolean validateAccessToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).requireIssuer(TOKEN_ISSUER).build().parseClaimsJws(token);
            if (!SIGNATURE_ALGORITHM.getValue().equals(claims.getHeader().getAlgorithm())) {
                throw new AuthException("Signature algorithm does not match", token, HttpStatus.UNAUTHORIZED);
            }
            String userEmail = claims.getBody().getSubject();
            if (!userService.existsByEmail(userEmail)) {
                throw new AuthException("User email doesn't exists", token, HttpStatus.UNAUTHORIZED);
            }
            List<String> permissions = (List<String>) claims.getBody().get(PERMISSIONS_KEY);
            return permissions.contains(Permissions.IRRIGATION_ADMIN);
        } catch (ExpiredJwtException e) {
            throw new AuthException("Access token expired: " + token, e.getMessage(), HttpStatus.GONE);
        } catch (JwtException e) {
            throw new AuthException("Access token corrupted: " + token, e.getMessage(), HttpStatus.PRECONDITION_FAILED);
        }
    }

    //todo: implement authentication flow via AuthenticationManager
    public Authentication getAuthentication(String jwt) {
        return null;
    }
}
