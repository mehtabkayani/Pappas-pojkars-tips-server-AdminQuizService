package com.pappaspojkars.pappaspojkarstipsserveradminquizservice.utilities;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.LocalDateTime;

import static com.pappaspojkars.pappaspojkarstipsserveradminquizservice.utilities.Utilities.SERVER_OFFSET;

@Converter(autoApply = true)
public class LocalDateTimeAttributeConverter implements
        AttributeConverter<LocalDateTime, Long> {

    @Override
    public Long convertToDatabaseColumn(LocalDateTime attribute) {
        return attribute == null ? null : attribute.toEpochSecond(SERVER_OFFSET);
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Long dbData) {
        return dbData == null ? null : LocalDateTime.ofEpochSecond(dbData, 0, SERVER_OFFSET);
    }
}
