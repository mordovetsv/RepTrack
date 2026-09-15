package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;

/**
 * Одна тренировка — контейнер для фактически выполненных подходов
 * ({@link WorkoutSet}).
 */
@Entity
@Table(name = "workout_sessions")
public class WorkoutSession {

    /** Первичный ключ. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Пользователь, проводящий тренировку. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Плановый день недельного сплита (например, «пятничная тренировка»);
     * может не совпадать с фактическим днём {@link #getStartedAt()},
     * если тренировка перенесена на другой день.
     */
    @Column(name = "day_of_week_id", nullable = false)
    private DayOfWeek dayOfWeek;

    /** Опциональное название тренировки, например «Грудь и трицепс». */
    @Column(name = "name", length = 100)
    private String name;

    /** Фактическое время начала тренировки — проставляется базой данных. */
    @Column(name = "started_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime startedAt;

    /** Время окончания тренировки; {@code null}, пока тренировка идёт. */
    @Column(name = "finished_at")
    private OffsetDateTime finishedAt;

    /** Произвольные заметки к тренировке. */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    /**
     * @return первичный ключ тренировки
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id первичный ключ тренировки
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return пользователь, проводящий тренировку
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user пользователь, проводящий тренировку
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return плановый день недельного сплита
     */
    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    /**
     * @param dayOfWeek плановый день недельного сплита
     */
    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    /**
     * @return название тренировки
     */
    public String getName() {
        return name;
    }

    /**
     * @param name название тренировки
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return фактическое время начала тренировки
     */
    public OffsetDateTime getStartedAt() {
        return startedAt;
    }

    /**
     * @return время окончания тренировки, либо {@code null}, если она ещё идёт
     */
    public OffsetDateTime getFinishedAt() {
        return finishedAt;
    }

    /**
     * @param finishedAt время окончания тренировки
     */
    public void setFinishedAt(OffsetDateTime finishedAt) {
        this.finishedAt = finishedAt;
    }

    /**
     * @return заметки к тренировке
     */
    public String getNotes() {
        return notes;
    }

    /**
     * @param notes заметки к тренировке
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }
}
