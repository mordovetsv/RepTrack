--liquibase formatted sql

--changeset mordovets:20260915_004_create_days_of_week
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'days_of_week'
CREATE TABLE days_of_week (
    id SMALLINT PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE
);

COMMENT ON TABLE days_of_week IS 'Справочник дней недели';

INSERT INTO days_of_week (id, name) VALUES (1, 'Понедельник');
INSERT INTO days_of_week (id, name) VALUES (2, 'Вторник');
INSERT INTO days_of_week (id, name) VALUES (3, 'Среда');
INSERT INTO days_of_week (id, name) VALUES (4, 'Четверг');
INSERT INTO days_of_week (id, name) VALUES (5, 'Пятница');
INSERT INTO days_of_week (id, name) VALUES (6, 'Суббота');
INSERT INTO days_of_week (id, name) VALUES (7, 'Воскресенье');

--rollback DROP TABLE days_of_week;

--changeset mordovets:20260915_004_create_workout_sessions
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'workout_sessions'
CREATE TABLE workout_sessions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL CONSTRAINT fk_sessions_user REFERENCES users(id) ON DELETE CASCADE,
    day_of_week_id SMALLINT NOT NULL CONSTRAINT fk_sessions_day_of_week REFERENCES days_of_week(id),
    name VARCHAR(100),
    started_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    finished_at TIMESTAMPTZ,
    notes TEXT
);

COMMENT ON TABLE workout_sessions IS 'Одна тренировка — контейнер для подходов';
COMMENT ON COLUMN workout_sessions.day_of_week_id IS 'Плановый день недельного сплита (напр. ''пятничная тренировка''); может не совпадать с фактическим днём started_at, если тренировка перенесена';
COMMENT ON COLUMN workout_sessions.name IS 'Опциональное название: Грудь и трицепс';
COMMENT ON COLUMN workout_sessions.finished_at IS 'NULL пока тренировка идёт';

CREATE INDEX idx_sessions_user_date ON workout_sessions (user_id, started_at);
CREATE INDEX idx_sessions_day_of_week ON workout_sessions (day_of_week_id);

--rollback DROP TABLE workout_sessions;
