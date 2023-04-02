package com.schilings.neiko.security.oauth2.authorization.server.jackson;


import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;

import java.io.IOException;

@JsonDeserialize(using = LongMixin.LongMixinDeserializer.class)
public abstract class LongMixin {
    
    static class LongMixinDeserializer extends JsonDeserializer<Long> {
        @Override
        public Long deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            return (Long) NumberDeserializers.find(Long.TYPE, Long.class.getName()).deserialize(p, ctxt);
        }
    }
}

