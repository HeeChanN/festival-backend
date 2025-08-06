package com.halo.eventer.global.security.provider;

import java.security.Key;
import java.util.Date;
import java.util.List;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.halo.eventer.global.security.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {

    @Value("${jwt.token.secret}")
    private String secret;

    private Key key;

    public final CustomUserDetailsService customUserDetailService;

    @Autowired
    public JwtProvider(CustomUserDetailsService customUserDetailService) {
        this.customUserDetailService = customUserDetailService;
    }

    @PostConstruct
    protected void init() {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String createToken(String account, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(account);
        claims.put("roles", roles);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + 3600000)) // 만료기간
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getAccount(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            // Bearer 검증
            if (!token.substring(0, "BEARER ".length()).equalsIgnoreCase("BEARER ")) {
                return false;
            } else {
                token = token.split(" ")[1].trim();
            }
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            // 만료되었을 시 false
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // Authorization Header를 통해 인증을 한다. 헤더에서 Authorization 부분만 파싱해서 사용하기 위해 만들었음
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    //    // 토큰 검증
    //    public boolean validateToken(String token) {
    //        try {
    ////            // Bearer 검증
    ////            if (!token.substring(0, "BEARER ".length()).equalsIgnoreCase("BEARER ")) {
    ////                return false;
    ////            } else {
    ////                token = token.split(" ")[1].trim();
    ////            }
    //            Jws<Claims> claims =
    // Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    //            // 만료되었을 시 false
    //            return !claims.getBody().getExpiration().before(new Date());
    //        } catch (Exception e) {
    //            return false;
    //        }
    //    }

    // 쿠키 통해 인증
    //    public String resolveToken(HttpServletRequest request) {
    //        Cookie[] cookies = request.getCookies();
    //        if (cookies != null) {
    //            for (Cookie cookie : cookies) {
    //                if ("JWT".equals(cookie.getName())) {
    //                    return cookie.getValue();
    //                }
    //            }
    //        }
    //        return null;
    //    }

    // 권한정보 획득
    // Spring Security 인증과정에서 권한확인을 위한 기능
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = customUserDetailService.loadUserByUsername(this.getAccount(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
