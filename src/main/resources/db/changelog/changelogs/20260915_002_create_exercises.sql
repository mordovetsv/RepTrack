--liquibase formatted sql

--changeset mordovets:20260915_002_create_exercises
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'exercises'
CREATE TABLE exercises (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    muscle_group VARCHAR(50),
    is_custom BOOLEAN NOT NULL DEFAULT FALSE,
    user_id BIGINT CONSTRAINT fk_exercises_user REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

COMMENT ON COLUMN exercises.muscle_group IS 'chest / back / legs / shoulders / arms / core';
COMMENT ON COLUMN exercises.user_id IS 'NULL для глобальных упражнений';

CREATE INDEX idx_exercises_user_id ON exercises (user_id);

-- проверка: кастомное упражнение обязано иметь user_id
ALTER TABLE exercises ADD CONSTRAINT chk_custom_exercise_has_user
CHECK (
    (is_custom = FALSE AND user_id IS NULL) OR
    (is_custom = TRUE  AND user_id IS NOT NULL)
);

--rollback DROP TABLE exercises;
