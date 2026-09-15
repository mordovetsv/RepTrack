package org.example.repository;

import org.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Репозиторий для работы с пользователями ({@link User}).
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Ищет пользователя по идентификатору Telegram.
     *
     * @param telegramId идентификатор пользователя в Telegram
     * @return найденный пользователь, либо {@link Optional#empty()}
     */
    Optional<User> findByTelegramId(Long telegramId);
}
