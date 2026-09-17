package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.example.validation.IdValidator;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final IdValidator idValidator;

    /**
     * Находит пользователя по идентификатору Telegram, либо создаёт нового,
     * если он обращается впервые. Если {@code username} не передан, ему
     * присваивается значение "Анонимный пользователь".
     *
     * @param telegramId идентификатор пользователя в Telegram
     * @param username имя пользователя в Telegram, может быть {@code null}
     * @return найденный или новый пользователь
     * @throws IllegalArgumentException если {@code telegramId} равен {@code null} или не положителен
     */
    public User findOrCreateByTelegramId(Long telegramId, String username) {
        idValidator.requirePositive(telegramId);
        String resolvedUsername = (username != null) ? username : "Анонимный пользователь";
        return repository.findByTelegramId(telegramId)
                .orElseGet(() -> repository.save(new User(telegramId, resolvedUsername)));
    }

    /**
     * Ищет пользователя по идентификатору Telegram без создания нового.
     *
     * @param telegramId идентификатор пользователя в Telegram
     * @return найденный пользователь, либо {@link Optional#empty()}
     * @throws IllegalArgumentException если {@code telegramId} равен {@code null} или не положителен
     */
    public Optional<User> getByTelegramId(Long telegramId) {
        idValidator.requirePositive(telegramId);
        return repository.findByTelegramId(telegramId);
    }

    /**
     * Ищет пользователя по первичному ключу.
     *
     * @param id первичный ключ пользователя
     * @return найденный пользователь, либо {@link Optional#empty()}
     * @throws IllegalArgumentException если {@code id} равен {@code null} или не положителен
     */
    public Optional<User> getById(Long id) {
        idValidator.requirePositive(id);
        return repository.findById(id);
    }
}
