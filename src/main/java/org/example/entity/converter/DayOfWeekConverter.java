package org.example.entity.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.example.entity.enums.DayOfWeek;

/**
 * Конвертер JPA, преобразующий {@link DayOfWeek} в числовой идентификатор
 * колонки {@code day_of_week_id} (SMALLINT) и обратно. Применяется
 * автоматически ко всем полям типа {@link DayOfWeek} благодаря
 * {@code autoApply = true}.
 */
@Converter(autoApply = true)
public class DayOfWeekConverter implements AttributeConverter<DayOfWeek, Short> {

    /**
     * @param attribute значение enum на стороне Java
     * @return идентификатор дня недели для записи в базу данных, либо {@code null}
     */
    @Override
    public Short convertToDatabaseColumn(DayOfWeek attribute) {
        return attribute == null ? null : attribute.getId();
    }

    /**
     * @param dbData идентификатор дня недели, прочитанный из базы данных
     * @return соответствующее значение enum, либо {@code null}
     */
    @Override
    public DayOfWeek convertToEntityAttribute(Short dbData) {
        return dbData == null ? null : DayOfWeek.fromId(dbData);
    }
}
