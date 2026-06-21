CREATE SCHEMA IF NOT EXISTS data;
SET search_path TO data, pg_catalog, public;

CREATE TABLE registration_attempts
(
    id              uuid PRIMARY KEY,
    idempotency_key varchar(128) NOT NULL UNIQUE,
    request_hash    varchar(64)  NOT NULL,
    email           varchar(255) NOT NULL UNIQUE,
    requested_role  varchar(32)  NOT NULL CHECK (requested_role IN ('TENANT', 'LANDLORD')),
    status          varchar(32)  NOT NULL CHECK (status IN ('STARTED',
                                                            'USER_CREATED',
                                                            'PROFILE_CREATED',
                                                            'COMPLETED',
                                                            'COMPENSATION_PENDING',
                                                            'COMPENSATED',
                                                            'COMPENSATION_FAILED',
                                                            'FAILED')),
    user_id         uuid UNIQUE,
    profile_id      uuid UNIQUE,
    error_code      varchar(64),
    error_message   varchar(1000),
    retry_count     integer      NOT NULL DEFAULT 0 CHECK (retry_count >= 0),
    version         bigint       NOT NULL DEFAULT 0,
    created_at      timestamptz  NOT NULL DEFAULT now(),
    updated_at      timestamptz  NOT NULL DEFAULT now(),
    completed_at    timestamptz,
    CONSTRAINT chk_registration_email_normalized CHECK (email = lower(email)),
    CONSTRAINT chk_registration_request_hash CHECK (request_hash ~ '^[0-9a-f]{64}$')
);

CREATE INDEX idx_registration_attempts_status_updated_at
    ON registration_attempts (status, updated_at);
