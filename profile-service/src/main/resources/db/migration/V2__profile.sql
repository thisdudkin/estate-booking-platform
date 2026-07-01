CREATE TABLE IF NOT EXISTS profile
(
    id           uuid PRIMARY KEY DEFAULT uuidv7(),
    user_id      uuid         NOT NULL UNIQUE,
    display_name varchar(510) NOT NULL,
    phone_number varchar(30)
);

CREATE INDEX idx_profiles_phone_number
    ON profile (phone_number);