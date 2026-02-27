package com.opencbs.core.domain.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class ExtraJsonConverter implements AttributeConverter<ExtraJson, String> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(ExtraJson attribute) {
        if (attribute == null) return null;
        try {
            return MAPPER.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize ExtraJson", e);
        }
    }

    @Override
    public ExtraJson convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            return MAPPER.readValue(dbData, ExtraJson.class);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to deserialize ExtraJson", e);
        }
    }
}
