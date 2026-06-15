package io.petproject.estate.booking.platform.profile.repository;

import io.petproject.estate.booking.platform.profile.entity.TenantProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TenantProfileRepository extends JpaRepository<TenantProfile, UUID> {
}
