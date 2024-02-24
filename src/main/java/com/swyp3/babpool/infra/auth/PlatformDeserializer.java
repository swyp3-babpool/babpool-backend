package com.swyp3.babpool.infra.auth;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class PlatformDeserializer extends JsonDeserializer<Platform> {
    @Override
    public Platform deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return Platform.valueOf(jsonParser.getText().toUpperCase());
    }
}
