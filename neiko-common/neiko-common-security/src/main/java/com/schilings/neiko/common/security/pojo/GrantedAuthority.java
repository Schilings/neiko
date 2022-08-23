package com.schilings.neiko.common.security.pojo;

import java.io.Serializable;

public interface GrantedAuthority extends Serializable {

    default String getAuthority() {
        return "";
    }
    
}
