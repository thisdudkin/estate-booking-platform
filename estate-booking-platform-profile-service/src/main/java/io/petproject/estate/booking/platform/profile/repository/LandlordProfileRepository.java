package io.petproject.estate.booking.platform.profile.repository;

import io.petproject.estate.booking.platform.profile.entity.LandlordProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LandlordProfileRepository extends JpaRepository<LandlordProfile, UUID> {
}
