package com.schilings.neiko.samples.shiro.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.schilings.neiko.autoconfigure.shiro.token.JWTRepository;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * <pre>
 * <p></p>
 * </pre>
 *
 * @author <a href="https://github.com/kil1ua">Ken-Chy129</a>
 * @date 2022/8/10 14:26
 */
@Component
public class MyJWTRepository extends JWTRepository {
 
    @Override
    public String getToken(Map<String, String> claimMap, Algorithm algorithm, Date expire) {
        JWTCreator.Builder builder = JWT.create();
        claimMap.keySet().forEach(key -> builder.withClaim(key, claimMap.get(key)));
        return builder.withExpiresAt(expire)
                .sign(algorithm);
    }

    @Override
    public DecodedJWT verify(Algorithm algorithm, String token) throws JWTVerificationException {
        return JWT.require(algorithm).build().verify(token);
    }
}
