package com.schilings.neiko.authorization.common.jackson2;


import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.schilings.neiko.authorization.common.userdetails.User;

public class NeikoAuthorizationJackson2Module extends SimpleModule {

    public NeikoAuthorizationJackson2Module() {
        super(NeikoAuthorizationJackson2Module.class.getName(), new Version(1, 0, 0, null, null, null));
    }
    @Override
    public void setupModule(SetupContext context) {
        context.setMixInAnnotations(User.class, UserMixin.class);
    }
}
