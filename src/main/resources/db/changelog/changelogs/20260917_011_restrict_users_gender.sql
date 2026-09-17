--liquibase formatted sql

--changeset mordovets:20260917_011_restrict_users_gender
ALTER TABLE users ADD CONSTRAINT chk_users_gender CHECK (gender IS NULL OR gender IN ('male', 'female'));

COMMENT ON COLUMN users.gender IS 'male / female';

--rollback ALTER TABLE users DROP CONSTRAINT chk_users_gender;
--rollback COMMENT ON COLUMN users.gender IS 'male / female / other';
