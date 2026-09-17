--liquibase formatted sql

--changeset mordovets:20260918_012_bodyweight_multiple_per_day
ALTER TABLE body_weight_log DROP CONSTRAINT uq_bwl_user_date;
ALTER TABLE body_weight_log RENAME COLUMN measured_at TO created_at;
ALTER TABLE body_weight_log ALTER COLUMN created_at TYPE TIMESTAMPTZ USING created_at::timestamptz;
ALTER TABLE body_weight_log ALTER COLUMN created_at SET DEFAULT NOW();

COMMENT ON COLUMN body_weight_log.created_at IS 'Момент замера — несколько замеров в день разрешены';

--rollback ALTER TABLE body_weight_log ALTER COLUMN created_at TYPE DATE USING created_at::date;
--rollback ALTER TABLE body_weight_log ALTER COLUMN created_at SET DEFAULT CURRENT_DATE;
--rollback ALTER TABLE body_weight_log RENAME COLUMN created_at TO measured_at;
--rollback ALTER TABLE body_weight_log ADD CONSTRAINT uq_bwl_user_date UNIQUE (user_id, measured_at);
