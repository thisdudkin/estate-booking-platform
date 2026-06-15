CREATE TABLE user_profiles
(
    id           uuid PRIMARY KEY,
    user_id      uuid         NOT NULL UNIQUE,
    email        varchar(255) NOT NULL UNIQUE,
    phone        varchar(32),
    display_name varchar(255),
    status       varchar(32)  NOT NULL CHECK (status IN ('ACTIVE', 'BLOCKED', 'DELETED')),
    created_at   timestamptz  NOT NULL DEFAULT now(),
    updated_at   timestamptz  NOT NULL DEFAULT now()
);

CREATE TABLE landlord_profiles
(
    profile_id          uuid PRIMARY KEY REFERENCES user_profiles ON DELETE CASCADE,
    company_name        varchar(255),
    verification_status varchar(32) NOT NULL CHECK (verification_status IN ('PENDING', 'VERIFIED', 'REJECTED')),
    tax_number          varchar(64),
    created_at          timestamptz NOT NULL DEFAULT now(),
    updated_at          timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE tenant_profiles
(
    profile_id          uuid PRIMARY KEY REFERENCES user_profiles ON DELETE CASCADE,
    preferred_city      varchar(128),
    preferred_min_price numeric(12, 2),
    preferred_max_price numeric(12, 2),
    preferences         jsonb,
    created_at          timestamptz NOT NULL DEFAULT now(),
    updated_at          timestamptz NOT NULL DEFAULT now(),

    CONSTRAINT chk_tenant_price_range CHECK (preferred_min_price IS NULL OR preferred_max_price IS NULL OR
                                             preferred_min_price <= preferred_max_price)
);

CREATE TABLE favorite_listings
(
    tenant_id  uuid        NOT NULL REFERENCES tenant_profiles (profile_id) ON DELETE CASCADE,
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
    status         varchar(32)  NOT NULL CHECK (status IN ('NEW', 'PUBLISHED', 'FAILED')),
    created_at     timestamptz  NOT NULL DEFAULT now(),
    published_at   timestamptz
);
