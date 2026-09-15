--liquibase formatted sql

--changeset mordovets:20260915_007_seed_default_exercises
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM exercises WHERE is_custom = FALSE
-- Грудь
INSERT INTO exercises (name, description, muscle_group, is_custom) VALUES ('Жим лёжа со штангой', 'Базовое упражнение на грудь', 'chest', FALSE);
INSERT INTO exercises (name, description, muscle_group, is_custom) VALUES ('Жим лёжа с гантелями', 'Изолирующее упражнение на грудь', 'chest', FALSE);
INSERT INTO exercises (name, description, muscle_group, is_custom) VALUES ('Разводка с гантелями', 'Растяжка и изоляция грудных мышц', 'chest', FALSE);

-- Спина
INSERT INTO exercises (name, description, muscle_group, is_custom) VALUES ('Становая тяга', 'Базовое упражнение на спину и ноги', 'back', FALSE);
INSERT INTO exercises (name, description, muscle_group, is_custom) VALUES ('Подтягивания', 'Упражнение на широчайшие мышцы спины', 'back', FALSE);
INSERT INTO exercises (name, description, muscle_group, is_custom) VALUES ('Тяга штанги в наклоне', 'Базовое упражнение на спину', 'back', FALSE);

-- Ноги
INSERT INTO exercises (name, description, muscle_group, is_custom) VALUES ('Приседания со штангой', 'Базовое упражнение на квадрицепс', 'legs', FALSE);
INSERT INTO exercises (name, description, muscle_group, is_custom) VALUES ('Жим ногами', 'Упражнение на квадрицепс в тренажёре', 'legs', FALSE);
INSERT INTO exercises (name, description, muscle_group, is_custom) VALUES ('Румынская тяга', 'Упражнение на бицепс бедра', 'legs', FALSE);

-- Плечи
INSERT INTO exercises (name, description, muscle_group, is_custom) VALUES ('Жим штанги стоя', 'Базовое упражнение на дельты', 'shoulders', FALSE);
INSERT INTO exercises (name, description, muscle_group, is_custom) VALUES ('Разводка гантелей в стороны', 'Изоляция средней дельты', 'shoulders', FALSE);

-- Руки
INSERT INTO exercises (name, description, muscle_group, is_custom) VALUES ('Подъём штанги на бицепс', 'Базовое упражнение на бицепс', 'arms', FALSE);
INSERT INTO exercises (name, description, muscle_group, is_custom) VALUES ('Французский жим', 'Базовое упражнение на трицепс', 'arms', FALSE);

-- Кор
INSERT INTO exercises (name, description, muscle_group, is_custom) VALUES ('Планка', 'Статическое упражнение на кор', 'core', FALSE);
INSERT INTO exercises (name, description, muscle_group, is_custom) VALUES ('Скручивания', 'Упражнение на прямую мышцу живота', 'core', FALSE);

--rollback DELETE FROM exercises WHERE is_custom = FALSE;
