package com.ddng.scheduleapi.event;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

/**
 * <h1>SellingEvent 역직렬화 클래스</h1>
 *
 * @see SellingEvent
 */
public class SellingEventDeserializer extends JsonDeserializer<SellingEvent>
{
    @Override
    public SellingEvent deserialize(JsonParser jsonParser,
                                    DeserializationContext deserializationContext) throws IOException, JsonProcessingException
    {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode jsonNode = oc.readTree(jsonParser);

        return new SellingEvent(jsonNode.get("saleId").asLong());
    }
}
