--liquibase formatted sql

--changeset mordovets:20260915_003_create_body_weight_log
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'body_weight_log'
CREATE TABLE body_weight_log (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL CONSTRAINT fk_bwl_user REFERENCES users(id) ON DELETE CASCADE,
    weight_kg NUMERIC(5,2) NOT NULL,
    measured_at DATE NOT NULL DEFAULT CURRENT_DATE
);

COMMENT ON TABLE body_weight_log IS 'История изменений веса тела пользователя';

CREATE INDEX idx_bwl_user_date ON body_weight_log (user_id, measured_at);

-- уникальность: один замер в день на пользователя
ALTER TABLE body_weight_log ADD CONSTRAINT uq_bwl_user_date UNIQUE (user_id, measured_at);

--rollback DROP TABLE body_weight_log;
