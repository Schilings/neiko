package com.schilings.neiko.autoconfigure.shiro.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
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
public class DefaultJWTRepository extends JWTRepository {

	public String getToken(Map<String, String> claimMap) {
		return getToken(claimMap, new Date(System.currentTimeMillis() + JWTConstants.DEFAULT_INVALID_TIME));
	}

	public String getToken(Map<String, String> claimMap, Date expire) {
		return getToken(claimMap, Algorithm.HMAC256(JWTConstants.DEFAULT_HASH_SALT), expire);
	}

	public String getToken(Map<String, String> claimMap, Algorithm algorithm, Date expire) {
		JWTCreator.Builder builder = JWT.create();
		claimMap.keySet().forEach(key -> builder.withClaim(key, claimMap.get(key)));
		return builder.withExpiresAt(expire).sign(algorithm);
	}

	public DecodedJWT verify(String token) throws JWTVerificationException {
		return verify(Algorithm.HMAC256(JWTConstants.DEFAULT_HASH_SALT), token);
	}

	public DecodedJWT verify(Algorithm algorithm, String token) throws JWTVerificationException {
		return JWT.require(algorithm).build().verify(token);
	}

}
