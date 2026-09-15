package org.example.entity;

/**
 * День недели недельного сплита тренировок. Значения соответствуют
 * строкам справочника {@code days_of_week} в базе данных (id 1–7,
 * понедельник — воскресенье).
 * <p>
 * Это плановый день сплита (например, «пятничная тренировка»), а не
 * фактическая календарная дата — тренировка может быть перенесена и
 * выполнена в другой день недели.
 */
public enum DayOfWeek {
    MONDAY((short) 1),
    TUESDAY((short) 2),
    WEDNESDAY((short) 3),
    THURSDAY((short) 4),
    FRIDAY((short) 5),
    SATURDAY((short) 6),
    SUNDAY((short) 7);

    /** Идентификатор дня недели, совпадающий с id в таблице {@code days_of_week}. */
    private final short id;

    DayOfWeek(short id) {
        this.id = id;
    }

    /**
     * @return идентификатор дня недели в базе данных
     */
    public short getId() {
        return id;
    }

    /**
     * Находит день недели по его идентификатору из базы данных.
     *
     * @param id идентификатор дня недели (1–7)
     * @return соответствующее значение enum
     * @throws IllegalArgumentException если идентификатор не соответствует ни одному дню недели
     */
    public static DayOfWeek fromId(short id) {
        for (DayOfWeek day : values()) {
            if (day.id == id) {
                return day;
            }
        }
        throw new IllegalArgumentException("Unknown day_of_week id: " + id);
    }
}
