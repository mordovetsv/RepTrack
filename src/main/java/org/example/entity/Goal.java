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

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Цель пользователя по конкретному упражнению — живёт, пока не будет
 * достигнута, и не привязана к конкретной тренировке напрямую. Связь
 * с {@link WorkoutSession} устанавливается через общий {@link DayOfWeek}:
 * цели дня сплита предлагаются пользователю, когда он начинает тренировку
 * на этот же день.
 */
@Entity
@Table(name = "goals")
public class Goal {

    /** Первичный ключ. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Пользователь, которому принадлежит цель. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** Упражнение, по которому поставлена цель. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    /** День недельного сплита, к которому относится цель. */
    @Column(name = "day_of_week_id", nullable = false)
    private DayOfWeek dayOfWeek;

    /** Целевое количество подходов. */
    @Column(name = "target_sets", nullable = false)
    private Integer targetSets;

    /** Целевое количество повторений в подходе. */
    @Column(name = "target_reps", nullable = false)
    private Integer targetReps;

    /** Целевой рабочий вес в килограммах. */
    @Column(name = "weight_kg", nullable = false, precision = 5, scale = 2)
    private BigDecimal weightKg;

    /** Признак того, что цель ещё не достигнута и актуальна. */
    @Column(name = "is_active", nullable = false)
    private boolean active;

    /** Дата и время, когда цель была достигнута впервые; {@code null}, если ещё не достигнута. */
    @Column(name = "achieved_at")
    private OffsetDateTime achievedAt;

    /** Дата и время создания записи — проставляется базой данных. */
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    /**
     * @return первичный ключ цели
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id первичный ключ цели
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return пользователь, которому принадлежит цель
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user пользователь, которому принадлежит цель
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return упражнение, по которому поставлена цель
     */
    public Exercise getExercise() {
        return exercise;
    }

    /**
     * @param exercise упражнение, по которому поставлена цель
     */
    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    /**
     * @return день недельного сплита, к которому относится цель
     */
    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    /**
     * @param dayOfWeek день недельного сплита, к которому относится цель
     */
    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    /**
     * @return целевое количество подходов
     */
    public Integer getTargetSets() {
        return targetSets;
    }

    /**
     * @param targetSets целевое количество подходов
     */
    public void setTargetSets(Integer targetSets) {
        this.targetSets = targetSets;
    }

    /**
     * @return целевое количество повторений в подходе
     */
    public Integer getTargetReps() {
        return targetReps;
    }

    /**
     * @param targetReps целевое количество повторений в подходе
     */
    public void setTargetReps(Integer targetReps) {
        this.targetReps = targetReps;
    }

    /**
     * @return целевой рабочий вес в килограммах
     */
    public BigDecimal getWeightKg() {
        return weightKg;
    }

    /**
     * @param weightKg целевой рабочий вес в килограммах
     */
    public void setWeightKg(BigDecimal weightKg) {
        this.weightKg = weightKg;
    }

    /**
     * @return {@code true}, если цель ещё не достигнута и актуальна
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @param active признак актуальности цели
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * @return дата и время, когда цель была достигнута впервые, либо {@code null}
     */
    public OffsetDateTime getAchievedAt() {
        return achievedAt;
    }

    /**
     * @param achievedAt дата и время, когда цель была достигнута впервые
     */
    public void setAchievedAt(OffsetDateTime achievedAt) {
        this.achievedAt = achievedAt;
    }

    /**
     * @return дата и время создания записи
     */
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
