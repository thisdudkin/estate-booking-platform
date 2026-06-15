package io.petproject.estate.booking.platform.profile.repository;

import io.petproject.estate.booking.platform.profile.entity.FavoriteListing;
import io.petproject.estate.booking.platform.profile.entity.base.FavoriteListingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteListingRepository extends JpaRepository<FavoriteListing, FavoriteListingId> {
}
