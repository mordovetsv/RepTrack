package org.example.entity.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.example.entity.enums.MuscleGroup;

/**
 * Конвертер JPA, преобразующий {@link MuscleGroup} в числовой идентификатор
 * колонки {@code muscle_group_id} (SMALLINT) и обратно. Применяется
 * автоматически ко всем полям и элементам коллекций типа {@link MuscleGroup}
 * благодаря {@code autoApply = true}.
 */
@Converter(autoApply = true)
public class MuscleGroupConverter implements AttributeConverter<MuscleGroup, Short> {

    /**
     * @param attribute значение enum на стороне Java
     * @return идентификатор группы мышц для записи в базу данных, либо {@code null}
     */
    @Override
    public Short convertToDatabaseColumn(MuscleGroup attribute) {
        return attribute == null ? null : attribute.getId();
    }

    /**
     * @param dbData идентификатор группы мышц, прочитанный из базы данных
     * @return соответствующее значение enum, либо {@code null}
     */
    @Override
    public MuscleGroup convertToEntityAttribute(Short dbData) {
        return dbData == null ? null : MuscleGroup.fromId(dbData);
    }
}
