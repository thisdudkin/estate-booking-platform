package io.petproject.estate.booking.platform.profile.domain.model.user;

public enum UserStatus {
    ACTIVE,
    SUSPENDED,
    DELETED;

    public boolean isActive() {
        return this == ACTIVE;
    }

    public boolean isDeleted() {
        return this == DELETED;
    }
}
