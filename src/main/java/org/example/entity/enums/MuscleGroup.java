package org.example.entity.enums;

/**
 * Группа мышц. Значения соответствуют строкам справочника
 * {@code muscle_groups} в базе данных. Одно упражнение может
 * задействовать сразу несколько групп мышц примерно в равной степени
 * (например, жим лёжа — грудь, руки и плечи), поэтому связь с
 * {@code Exercise} — многие-ко-многим, а не одно значение на упражнение.
 */
public enum MuscleGroup {
    CHEST((short) 1),
    BACK((short) 2),
    LEGS((short) 3),
    SHOULDERS((short) 4),
    ARMS((short) 5),
    CORE((short) 6);

    /** Идентификатор группы мышц, совпадающий с id в таблице {@code muscle_groups}. */
    private final short id;

    MuscleGroup(short id) {
        this.id = id;
    }

    /**
     * @return идентификатор группы мышц в базе данных
     */
    public short getId() {
        return id;
    }

    /**
     * Находит группу мышц по её идентификатору из базы данных.
     *
     * @param id идентификатор группы мышц
     * @return соответствующее значение enum
     * @throws IllegalArgumentException если идентификатор не соответствует ни одной группе мышц
     */
    public static MuscleGroup fromId(short id) {
        for (MuscleGroup group : values()) {
            if (group.id == id) {
                return group;
            }
        }
        throw new IllegalArgumentException("Unknown muscle_group id: " + id);
    }
}
