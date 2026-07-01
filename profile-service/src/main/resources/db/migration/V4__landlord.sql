CREATE TABLE IF NOT EXISTS landlord
(
    profile_id          uuid PRIMARY KEY REFERENCES profile,
    public_name         varchar(255),
    contact_phone       varchar(30),
    verification_status boolean
);