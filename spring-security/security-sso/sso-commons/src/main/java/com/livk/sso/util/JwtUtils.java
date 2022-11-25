package com.livk.sso.util;

import com.livk.sso.entity.Payload;
import com.livk.util.DateUtils;
import com.livk.util.JacksonUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

/**
 * <p>
 * JwtUtils
 * </p>
 *
 * @author livk
 * @date 2022/4/11
 */
@UtilityClass
public class JwtUtils {

    private static final String JWT_KEY = "livk";

    public <T> String generateTokenExpireInMinutes(T userInfo, PrivateKey privateKey, long expire) {
        return Jwts.builder().claim(JWT_KEY, JacksonUtils.toJsonStr(userInfo)).setId(createJTI())
                .setExpiration(DateUtils.toDate(LocalDateTime.now().plusMinutes(expire)))
                .signWith(privateKey, SignatureAlgorithm.RS256).compact();
    }

    public <T> String generateTokenExpireInSeconds(T userInfo, PrivateKey privateKey, long expire) {
        return Jwts.builder().claim(JWT_KEY, JacksonUtils.toJsonStr(userInfo)).setId(createJTI())
                .setExpiration(DateUtils.toDate(LocalDateTime.now().plusSeconds(expire)))
                .signWith(privateKey, SignatureAlgorithm.RS256).compact();
    }

    private Jws<Claims> parserToken(String token, PublicKey publicKey) {
        return Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(token);
    }

    public String createJTI() {
        return new String(Base64.getEncoder().encode(UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8)));
    }

    public <T> Payload<T> getInfo(String token, PublicKey publicKey, Class<T> targetClass) {
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        Claims body = claimsJws.getBody();
        return Payload.<T>builder().id(body.getId())
                .userInfo(JacksonUtils.toBean(body.get(JWT_KEY).toString(), targetClass))
                .expiration(body.getExpiration()).build();
    }

    public <T> Payload<T> getInfo(String token, PublicKey publicKey) {
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        Claims body = claimsJws.getBody();
        return Payload.<T>builder().id(body.getId()).expiration(body.getExpiration()).build();
    }

}
