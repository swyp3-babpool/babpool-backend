package com.swyp3.babpool.global.config;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class ObjectMapperResolver{

    public static class JsonRequestNumericStringToLong extends JsonDeserializer<Long> {
        @Override
        public Long deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
            if(org.springframework.util.StringUtils.hasText(jsonParser.getText()) && StringUtils.isNumeric(jsonParser.getText())){
                return Long.parseLong(jsonParser.getText());
            }
            return null;
        }
    }

    public static class JsonResponseLongToString extends JsonSerializer<Long> {
        @Override
        public void serialize(Long idLong, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(String.valueOf(idLong));
        }
    }
}
