package com.gdas.shopadminapi.enums.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.gdas.shopadminapi.enums.ItemType;

import java.io.IOException;

public class ItemTypeDeserializer extends JsonDeserializer<ItemType> {

    @Override
    public ItemType deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        return node.isObject()
                ? ItemType.valueOf(node.get("id").textValue())
                : ItemType.valueOf(node.textValue());

    }

}
