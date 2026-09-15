package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.OffsetDateTime;

/**
 * Пользователь Telegram-бота.
 */
@Entity
@Table(name = "users")
public class User {

    /** Первичный ключ. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Идентификатор пользователя в Telegram — уникален, обязателен. */
    @Column(name = "telegram_id", nullable = false, unique = true)
    private Long telegramId;

    /** Имя пользователя в Telegram (необязательное). */
    @Column(name = "username", length = 100)
    private String username;

    /** Дата рождения пользователя. */
    @Column(name = "birth_date")
    private LocalDate birthDate;

    /** Пол пользователя: male / female / other. */
    @Column(name = "gender", length = 10)
    private String gender;

    /** Часовой пояс пользователя, по умолчанию Europe/Moscow. */
    @Column(name = "timezone", nullable = false, length = 50)
    private String timezone;

    /** Дата и время создания записи — проставляется базой данных. */
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    /**
     * @return первичный ключ пользователя
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id первичный ключ пользователя
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return идентификатор пользователя в Telegram
     */
    public Long getTelegramId() {
        return telegramId;
    }

    /**
     * @param telegramId идентификатор пользователя в Telegram
     */
    public void setTelegramId(Long telegramId) {
        this.telegramId = telegramId;
    }

    /**
     * @return имя пользователя в Telegram
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username имя пользователя в Telegram
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return дата рождения пользователя
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * @param birthDate дата рождения пользователя
     */
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * @return пол пользователя
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender пол пользователя
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return часовой пояс пользователя
     */
    public String getTimezone() {
        return timezone;
    }

    /**
     * @param timezone часовой пояс пользователя
     */
    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    /**
     * @return дата и время создания записи
     */
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
