package com.gdas.shopadminapi.enums.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.gdas.shopadminapi.enums.Measure;

import java.io.IOException;

public class MeasureDeserializer extends JsonDeserializer<Measure> {

    @Override
    public Measure deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        return node.isObject()
                ? Measure.valueOf(node.get("id").textValue())
                : Measure.valueOf(node.textValue());

    }

}
