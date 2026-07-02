CREATE TABLE IF NOT EXISTS landlord
(
    profile_id    uuid PRIMARY KEY REFERENCES profile,
    public_name   varchar(255),
    contact_phone varchar(30),
    verified      boolean     NOT NULL DEFAULT false,
    created_at    timestamptz NOT NULL DEFAULT now(),
    updated_at    timestamptz NOT NULL DEFAULT now()
);