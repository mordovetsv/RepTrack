package org.example.validation;

import org.springframework.stereotype.Component;

/**
 * Валидатор первичных ключей и идентификаторов, используемых в сервисном слое.
 */
@Component
public class IdValidator {

    /**
     * Проверяет, что идентификатор не {@code null} и положителен.
     *
     * @param id идентификатор для проверки
     * @throws IllegalArgumentException если {@code id} равен {@code null} или не положителен
     */
    public void requirePositive(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be positive");
        }
    }
}
