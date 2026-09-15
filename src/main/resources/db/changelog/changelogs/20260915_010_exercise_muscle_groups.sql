--liquibase formatted sql

--changeset mordovets:20260915_010_create_exercise_muscle_groups
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'exercise_muscle_groups'
CREATE TABLE exercise_muscle_groups (
    exercise_id BIGINT NOT NULL CONSTRAINT fk_emg_exercise REFERENCES exercises(id) ON DELETE CASCADE,
    muscle_group_id SMALLINT NOT NULL CONSTRAINT fk_emg_muscle_group REFERENCES muscle_groups(id),
    PRIMARY KEY (exercise_id, muscle_group_id)
);

COMMENT ON TABLE exercise_muscle_groups IS 'Многие-ко-многим: какие группы мышц задействует упражнение';

CREATE INDEX idx_emg_muscle_group ON exercise_muscle_groups (muscle_group_id);

--rollback DROP TABLE exercise_muscle_groups;

--changeset mordovets:20260915_010_migrate_muscle_group_data
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM information_schema.columns WHERE table_name = 'exercises' AND column_name = 'muscle_group'
-- переносим существующее значение muscle_group в связующую таблицу
INSERT INTO exercise_muscle_groups (exercise_id, muscle_group_id)
SELECT e.id, mg.id
FROM exercises e
JOIN muscle_groups mg ON mg.name = e.muscle_group
WHERE e.muscle_group IS NOT NULL;

-- многосуставные упражнения нагружают несколько групп мышц примерно поровну
INSERT INTO exercise_muscle_groups (exercise_id, muscle_group_id)
SELECT e.id, mg.id
FROM exercises e
JOIN muscle_groups mg ON mg.name IN ('arms', 'shoulders')
WHERE e.name IN ('Жим лёжа со штангой', 'Жим лёжа с гантелями')
ON CONFLICT DO NOTHING;

INSERT INTO exercise_muscle_groups (exercise_id, muscle_group_id)
SELECT e.id, mg.id
FROM exercises e
JOIN muscle_groups mg ON mg.name = 'legs'
WHERE e.name = 'Становая тяга'
ON CONFLICT DO NOTHING;

--rollback DELETE FROM exercise_muscle_groups;

--changeset mordovets:20260915_010_drop_muscle_group_column
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM information_schema.columns WHERE table_name = 'exercises' AND column_name = 'muscle_group'
ALTER TABLE exercises DROP COLUMN muscle_group;

--rollback ALTER TABLE exercises ADD COLUMN muscle_group VARCHAR(50);
