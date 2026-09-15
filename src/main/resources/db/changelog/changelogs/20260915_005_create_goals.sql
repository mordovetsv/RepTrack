--liquibase formatted sql

--changeset mordovets:20260915_005_create_goals
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'goals'
CREATE TABLE goals (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL CONSTRAINT fk_goals_user REFERENCES users(id) ON DELETE CASCADE,
    exercise_id BIGINT NOT NULL CONSTRAINT fk_goals_exercise REFERENCES exercises(id),
    target_sets INT NOT NULL,
    target_reps INT NOT NULL,
    weight_kg NUMERIC(5,2) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    achieved_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

COMMENT ON TABLE goals IS 'Цель пользователя по конкретному упражнению — живёт, пока не будет достигнута';
COMMENT ON COLUMN goals.achieved_at IS 'Когда цель была достигнута впервые';

-- быстрый поиск активных целей пользователя
CREATE INDEX idx_goals_user_active ON goals (user_id, is_active);

--rollback DROP TABLE goals;
