package com.gdas.shopadminapi.entities.converters;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.gdas.shopadminapi.enums.Measure;
import com.gdas.shopadminapi.entities.RequestProductDocument;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@Converter
public class RequestProductDocumentConverter implements AttributeConverter<RequestProductDocument, String> {

    @Override
    public String convertToDatabaseColumn(RequestProductDocument requestProductDocument) {
        if (requestProductDocument == null) return null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addSerializer(Measure.class, new JsonSerializer<Measure>() {
                @Override
                public void serialize(Measure measure, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                    jsonGenerator.writeString(measure.name());
                }
            });
            mapper.registerModule(module);
            String result = mapper.writeValueAsString(requestProductDocument);
            return result;
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("current value can't be converted to JSON", e);
        }
    }

    @Override
    public RequestProductDocument convertToEntityAttribute(String string) {
        if (isEmpty(string)) return null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(string, RequestProductDocument.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("current value can't be converted to POJO", e);
        }
    }
}
