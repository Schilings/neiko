package com.schilings.neiko.extend.sa.token.oauth2.pojo;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 *
 * <p>
 * 身份验证
 * </p>
 *
 * @author Schilings
 */
public interface Authentication extends Serializable {

	Object getTokenDetails();

	UserDetails getUserDetails();

	boolean isAuthenticated();

	void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException;

}
