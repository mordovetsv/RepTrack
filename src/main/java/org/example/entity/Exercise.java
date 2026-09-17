package org.example.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.example.entity.enums.MuscleGroup;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Упражнение — глобальное (доступно всем пользователям) или кастомное
 * (создано конкретным пользователем).
 */
@Entity
@Table(name = "exercises")
public class Exercise {

    /** Первичный ключ. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Название упражнения. */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /** Описание упражнения. */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * Группы мышц, которые задействует упражнение.
     */
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "exercise_muscle_groups", joinColumns = @JoinColumn(name = "exercise_id"))
    @Column(name = "muscle_group_id", nullable = false)
    private Set<MuscleGroup> muscleGroups = new HashSet<>();

    /** Признак того, что упражнение кастомное (создано пользователем), а не глобальное. */
    @Column(name = "is_custom", nullable = false)
    private boolean custom;

    /** Пользователь-владелец кастомного упражнения; для глобальных упражнений — {@code null}. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /** Дата и время создания записи — проставляется базой данных. */
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    /**
     * @return первичный ключ упражнения
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id первичный ключ упражнения
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return название упражнения
     */
    public String getName() {
        return name;
    }

    /**
     * @param name название упражнения
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return описание упражнения
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description описание упражнения
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return группы мышц, которые задействует упражнение
     */
    public Set<MuscleGroup> getMuscleGroups() {
        return muscleGroups;
    }

    /**
     * @param muscleGroups группы мышц, которые задействует упражнение
     */
    public void setMuscleGroups(Set<MuscleGroup> muscleGroups) {
        this.muscleGroups = muscleGroups;
    }

    /**
     * @return {@code true}, если упражнение кастомное
     */
    public boolean isCustom() {
        return custom;
    }

    /**
     * @param custom признак кастомного упражнения
     */
    public void setCustom(boolean custom) {
        this.custom = custom;
    }

    /**
     * @return пользователь-владелец кастомного упражнения, либо {@code null} для глобального
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user пользователь-владелец кастомного упражнения
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return дата и время создания записи
     */
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
