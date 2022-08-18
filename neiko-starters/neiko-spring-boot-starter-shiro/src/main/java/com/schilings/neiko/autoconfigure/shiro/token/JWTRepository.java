package com.schilings.neiko.autoconfigure.shiro.token;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.Map;

/**
 * <pre>
 * <p></p>
 * </pre>
 *
 * @author <a href="https://github.com/kil1ua">Ken-Chy129</a>
 * @date 2022/8/18 21:57
 */
public abstract class JWTRepository {

    public abstract String getToken(Map<String, String> claimMap);

    public abstract String getToken(Map<String, String> claimMap, Date expire);

    public abstract String getToken(Map<String, String> claimMap, Algorithm algorithm, Date expire);

    public abstract DecodedJWT verify(String token) throws JWTVerificationException;

    public abstract DecodedJWT verify(Algorithm algorithm, String token) throws JWTVerificationException;
    
}
