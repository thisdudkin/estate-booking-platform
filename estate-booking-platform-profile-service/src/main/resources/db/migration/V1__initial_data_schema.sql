CREATE TABLE user_profiles
(
    id               uuid PRIMARY KEY,
    keycloak_user_id uuid         NOT NULL UNIQUE,
    email            varchar(255) NOT NULL UNIQUE,
    phone            varchar(32),
    display_name     varchar(255),
    status           varchar(32)  NOT NULL,
    created_at       timestamptz  NOT NULL DEFAULT now(),
    updated_at       timestamptz  NOT NULL DEFAULT now()
);

CREATE TABLE landlord_profiles
(
    user_id             uuid PRIMARY KEY REFERENCES user_profiles,
    company_name        varchar(255),
    verification_status varchar(32) NOT NULL,
    tax_number          varchar(64),
    created_at          timestamptz NOT NULL DEFAULT now(),
    updated_at          timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE tenant_profiles
(
    user_id             uuid PRIMARY KEY REFERENCES user_profiles,
    preferred_city      varchar(128),
    preferred_min_price numeric(12, 2),
    preferred_max_price numeric(12, 2),
    preferences         jsonb,
    created_at          timestamptz NOT NULL DEFAULT now(),
    updated_at          timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE favorite_listings
(
    tenant_id  uuid        NOT NULL REFERENCES user_profiles,
    listing_id uuid        NOT NULL,
    created_at timestamptz NOT NULL DEFAULT now(),
    PRIMARY KEY (tenant_id, listing_id)
);

CREATE TABLE outbox_events
(
    id             uuid PRIMARY KEY,
    aggregate_type varchar(64)  NOT NULL,
    aggregate_id   uuid         NOT NULL,
    event_type     varchar(128) NOT NULL,
    payload        jsonb        NOT NULL,
    status         varchar(32)  NOT NULL,
    created_at     timestamptz  NOT NULL DEFAULT now(),
    published_at   timestamptz
);
