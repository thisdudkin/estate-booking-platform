CREATE TABLE IF NOT EXISTS favorite_listing
(
    id                uuid PRIMARY KEY DEFAULT uuidv7(),
    tenant_profile_id uuid NOT NULL REFERENCES tenant,
    listing_id        uuid NOT NULL
);

ALTER TABLE favorite_listing
    ADD CONSTRAINT uq_favorite_listing_tenant_profile_id_listing_id
        UNIQUE (tenant_profile_id, listing_id);