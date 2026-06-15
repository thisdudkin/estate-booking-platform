package io.petproject.estate.booking.platform.profile.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Embeddable
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class FavoriteListingId implements Serializable {

    @Column(nullable = false)
    private UUID tenantId;

    @Column(nullable = false)
    private UUID listingId;

}
