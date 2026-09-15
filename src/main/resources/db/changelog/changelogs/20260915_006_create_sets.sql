--liquibase formatted sql

--changeset mordovets:20260915_006_create_sets
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'sets'
CREATE TABLE sets (
    id BIGSERIAL PRIMARY KEY,
    session_id BIGINT NOT NULL CONSTRAINT fk_sets_session REFERENCES workout_sessions(id) ON DELETE CASCADE,
    goal_id BIGINT NOT NULL CONSTRAINT fk_sets_goal REFERENCES goals(id),
    set_number INT NOT NULL,
    reps_done INT NOT NULL,
    weight_kg NUMERIC(5,2) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

COMMENT ON TABLE sets IS 'Один подход в рамках тренировки';
COMMENT ON COLUMN sets.weight_kg IS 'Фактический вес — может отличаться от цели';

CREATE INDEX idx_sets_session ON sets (session_id);
CREATE INDEX idx_sets_goal ON sets (goal_id);

-- уникальность: один номер подхода на цель в рамках сессии
ALTER TABLE sets ADD CONSTRAINT uq_sets_session_goal_number UNIQUE (session_id, goal_id, set_number);

--rollback DROP TABLE sets;
