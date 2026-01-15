package com.sena.sembrix.security.service;

import com.sena.sembrix.identity.ProfileProducer;
import com.sena.sembrix.identity.UserEntity;
import com.sena.sembrix.identity.dto.ProfileProducerDto;
import com.sena.sembrix.identity.repository.UserRepository;
import com.sena.sembrix.identity.service.impl.ProfileProducerServiceImpl;
import com.sena.sembrix.identity.service.impl.UserServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.lang.Objects;
import io.jsonwebtoken.security.Keys;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final UserDetailsService userDetailsService;
    private final ProfileProducerServiceImpl profileProducerService;
    private final UserRepository userRepository;

    public JwtService(UserDetailsService userDetailsService, ProfileProducerServiceImpl profileProducerService, UserRepository userRepository) {
        this.userDetailsService = userDetailsService;
        this.profileProducerService = profileProducerService;
        this.userRepository = userRepository;
    }

    @Value("${jwt.secretKey}")
    private String secretKey;

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
            Map<String, Objects> extraClaims,
            UserDetails userDetails) {
            Map<String, Object> claims = new HashMap<>(extraClaims);
            UserEntity userEntity = userRepository.findByEmail(userDetails.getUsername()).orElse(null);
            ProfileProducerDto profileProducer = null;
            if (userEntity != null) {
            profileProducer = profileProducerService.findByUserId(userEntity.getId());
             }
            if (profileProducer != null) {
            claims.put("profileProducerId", profileProducer.getId());
            }
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24)) // 1 day
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token) && userDetails.isEnabled());
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {

        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateRefresh(Map<String, Objects> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 604800000))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    public String getUsernameFromToken(String token) {
        return extractUserName(token);
    }

    public Boolean validateToken(String token) {
        String userName = extractUserName(token);
        if (StringUtils.isNotEmpty(userName) && !isTokenExpired(token)) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
            return isTokenValid(token, userDetails);
        }
        return false;
    }
}
