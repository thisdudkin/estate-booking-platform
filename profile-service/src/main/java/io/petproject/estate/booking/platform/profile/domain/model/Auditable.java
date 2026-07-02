package io.petproject.estate.booking.platform.profile.domain.model;

import java.time.Instant;

public interface Auditable {

    Instant getCreatedAt();

    Instant getUpdatedAt();

}
