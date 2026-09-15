--liquibase formatted sql

--changeset mordovets:20260915_008_add_day_of_week_to_goals
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.columns WHERE table_name = 'goals' AND column_name = 'day_of_week_id'
ALTER TABLE goals ADD COLUMN day_of_week_id SMALLINT
    CONSTRAINT fk_goals_day_of_week REFERENCES days_of_week(id);

COMMENT ON COLUMN goals.day_of_week_id IS 'День недельного сплита, к которому относится цель — связывает goals с workout_sessions через общий day_of_week';

UPDATE goals SET day_of_week_id = 1 WHERE day_of_week_id IS NULL;

ALTER TABLE goals ALTER COLUMN day_of_week_id SET NOT NULL;

CREATE INDEX idx_goals_day_of_week ON goals (day_of_week_id);

--rollback ALTER TABLE goals DROP COLUMN day_of_week_id;
