package com.schilings.neiko.autoconfigure.shiro.token;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.schilings.neiko.autoconfigure.shiro.cons.JWTConstants;

import java.util.Date;
import java.util.Map;

/**
 * <pre>
 * <p></p>
 * </pre>
 *
 * @author <a href="https://github.com/kil1ua">Ken-Chy129</a>
 * @date 2022/8/9 1:43
 */
public abstract class JWTRepository {

    public String getToken(Map<String, String> claimMap) {
        return getToken(claimMap, new Date(System.currentTimeMillis() + JWTConstants.DEFAULT_HMAC_PERIOD));
    }

    public String getToken(Map<String, String> claimMap, Date expire){
        return getToken(claimMap, Algorithm.HMAC256(JWTConstants.DEFAULT_HASH_SALT), expire);
    }

    public abstract String getToken(Map<String, String> claimMap, Algorithm algorithm, Date expire);

    public DecodedJWT verify(String token) throws JWTVerificationException {
        return verify(Algorithm.HMAC256(JWTConstants.DEFAULT_HASH_SALT), token);
    }
    
    public abstract DecodedJWT verify(Algorithm algorithm, String token) throws JWTVerificationException;
    
}
