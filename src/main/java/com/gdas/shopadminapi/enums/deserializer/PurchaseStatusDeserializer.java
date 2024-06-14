package com.gdas.shopadminapi.enums.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.gdas.shopadminapi.enums.PurchaseStatus;

import java.io.IOException;

public class PurchaseStatusDeserializer extends JsonDeserializer<PurchaseStatus> {

    @Override
    public PurchaseStatus deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        return node.isObject()
                ? PurchaseStatus.valueOf(node.get("id").textValue())
                : PurchaseStatus.valueOf(node.textValue());

    }

}
