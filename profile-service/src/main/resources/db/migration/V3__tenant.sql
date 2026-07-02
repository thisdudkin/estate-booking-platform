CREATE TABLE IF NOT EXISTS tenant
(
    profile_id     uuid PRIMARY KEY REFERENCES profile,
    preferred_city varchar(50),
    contact_name   varchar(255),
    contact_phone  varchar(30),
    created_at   timestamptz  NOT NULL DEFAULT now(),
    updated_at   timestamptz  NOT NULL DEFAULT now()
);