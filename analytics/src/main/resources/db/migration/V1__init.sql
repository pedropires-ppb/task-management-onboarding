CREATE TABLE task_events (
                             id BIGSERIAL PRIMARY KEY,
                             task_id BIGINT NOT NULL,
                             task_type VARCHAR(50) NOT NULL,
                             created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL
);