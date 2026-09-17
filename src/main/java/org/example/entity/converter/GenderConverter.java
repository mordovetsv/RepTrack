package org.example.entity.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.example.entity.enums.Gender;

/**
 * Конвертер JPA, преобразующий {@link Gender} в строку колонки
 * {@code users.gender} (в нижнем регистре) и обратно. Применяется
 * автоматически ко всем полям типа {@link Gender} благодаря {@code autoApply = true}.
 */
@Converter(autoApply = true)
public class GenderConverter implements AttributeConverter<Gender, String> {

    /**
     * @param attribute значение enum на стороне Java
     * @return строка для записи в базу данных, либо {@code null}
     */
    @Override
    public String convertToDatabaseColumn(Gender attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    /**
     * @param dbData строка, прочитанная из базы данных
     * @return соответствующее значение enum, либо {@code null}
     */
    @Override
    public Gender convertToEntityAttribute(String dbData) {
        return dbData == null ? null : Gender.valueOf(dbData.toUpperCase());
    }
}
