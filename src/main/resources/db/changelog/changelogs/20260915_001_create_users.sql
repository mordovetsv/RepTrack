--liquibase formatted sql

--changeset mordovets:20260915_001_create_users
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'users'
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    telegram_id BIGINT NOT NULL UNIQUE,
    username VARCHAR(100),
    birth_date DATE,
    gender VARCHAR(10),
    timezone VARCHAR(50) NOT NULL DEFAULT 'Europe/Moscow',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

COMMENT ON COLUMN users.gender IS 'male / female / other';

CREATE UNIQUE INDEX idx_users_telegram_id ON users (telegram_id);

--rollback DROP TABLE users;
