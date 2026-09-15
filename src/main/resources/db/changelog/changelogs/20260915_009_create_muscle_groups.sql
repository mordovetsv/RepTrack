--liquibase formatted sql

--changeset mordovets:20260915_009_create_muscle_groups
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'muscle_groups'
CREATE TABLE muscle_groups (
    id SMALLINT PRIMARY KEY,
    name VARCHAR(30) NOT NULL UNIQUE
);

COMMENT ON TABLE muscle_groups IS 'Справочник групп мышц';

INSERT INTO muscle_groups (id, name) VALUES (1, 'chest');
INSERT INTO muscle_groups (id, name) VALUES (2, 'back');
INSERT INTO muscle_groups (id, name) VALUES (3, 'legs');
INSERT INTO muscle_groups (id, name) VALUES (4, 'shoulders');
INSERT INTO muscle_groups (id, name) VALUES (5, 'arms');
INSERT INTO muscle_groups (id, name) VALUES (6, 'core');

--rollback DROP TABLE muscle_groups;
