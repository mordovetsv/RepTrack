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
import java.time.LocalDate;

/**
 * Один замер веса тела пользователя за конкретную дату. Полная история
 * изменений веса пользователя — это набор таких записей, например
 * {@code BodyWeightMeasurementRepository.findByUserId(...)}; «текущий»
 * вес определяется как запись с максимальной {@link #getMeasuredAt()}.
 */
@Entity
@Table(name = "body_weight_log")
public class BodyWeightMeasurement {

    /** Первичный ключ. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Пользователь, которому принадлежит замер веса. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** Вес тела в килограммах. */
    @Column(name = "weight_kg", nullable = false, precision = 5, scale = 2)
    private BigDecimal weightKg;

    /** Дата замера — не больше одного замера в день на пользователя. */
    @Column(name = "measured_at", nullable = false)
    private LocalDate measuredAt;

    /**
     * @return первичный ключ замера
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id первичный ключ замера
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return пользователь, которому принадлежит замер веса
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user пользователь, которому принадлежит замер веса
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return вес тела в килограммах
     */
    public BigDecimal getWeightKg() {
        return weightKg;
    }

    /**
     * @param weightKg вес тела в килограммах
     */
    public void setWeightKg(BigDecimal weightKg) {
        this.weightKg = weightKg;
    }

    /**
     * @return дата замера
     */
    public LocalDate getMeasuredAt() {
        return measuredAt;
    }

    /**
     * @param measuredAt дата замера
     */
    public void setMeasuredAt(LocalDate measuredAt) {
        this.measuredAt = measuredAt;
    }
}
