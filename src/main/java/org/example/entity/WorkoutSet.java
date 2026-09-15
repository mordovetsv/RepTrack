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
 * Один фактически выполненный подход в рамках тренировки. Замаплен на
 * таблицу {@code sets}; класс назван {@code WorkoutSet}, чтобы не
 * конфликтовать с {@link java.util.Set}.
 */
@Entity
@Table(name = "sets")
public class WorkoutSet {

    /** Первичный ключ. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Тренировка, в рамках которой выполнен подход. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "session_id", nullable = false)
    private WorkoutSession session;

    /** Цель, к которой относится подход. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "goal_id", nullable = false)
    private Goal goal;

    /** Порядковый номер подхода в рамках тренировки и цели. */
    @Column(name = "set_number", nullable = false)
    private Integer setNumber;

    /** Фактическое количество выполненных повторений. */
    @Column(name = "reps_done", nullable = false)
    private Integer repsDone;

    /** Фактический рабочий вес в килограммах — может отличаться от цели. */
    @Column(name = "weight_kg", nullable = false, precision = 5, scale = 2)
    private BigDecimal weightKg;

    /** Дата и время создания записи — проставляется базой данных. */
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    /**
     * @return первичный ключ подхода
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id первичный ключ подхода
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return тренировка, в рамках которой выполнен подход
     */
    public WorkoutSession getSession() {
        return session;
    }

    /**
     * @param session тренировка, в рамках которой выполнен подход
     */
    public void setSession(WorkoutSession session) {
        this.session = session;
    }

    /**
     * @return цель, к которой относится подход
     */
    public Goal getGoal() {
        return goal;
    }

    /**
     * @param goal цель, к которой относится подход
     */
    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    /**
     * @return порядковый номер подхода
     */
    public Integer getSetNumber() {
        return setNumber;
    }

    /**
     * @param setNumber порядковый номер подхода
     */
    public void setSetNumber(Integer setNumber) {
        this.setNumber = setNumber;
    }

    /**
     * @return фактическое количество выполненных повторений
     */
    public Integer getRepsDone() {
        return repsDone;
    }

    /**
     * @param repsDone фактическое количество выполненных повторений
     */
    public void setRepsDone(Integer repsDone) {
        this.repsDone = repsDone;
    }

    /**
     * @return фактический рабочий вес в килограммах
     */
    public BigDecimal getWeightKg() {
        return weightKg;
    }

    /**
     * @param weightKg фактический рабочий вес в килограммах
     */
    public void setWeightKg(BigDecimal weightKg) {
        this.weightKg = weightKg;
    }

    /**
     * @return дата и время создания записи
     */
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
