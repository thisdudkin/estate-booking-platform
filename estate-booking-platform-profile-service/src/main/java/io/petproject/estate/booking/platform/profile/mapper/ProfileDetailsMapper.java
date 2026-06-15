package io.petproject.estate.booking.platform.profile.mapper;

import io.petproject.estate.booking.platform.profile.dto.response.ProfileDetailsResponse;
import io.petproject.estate.booking.platform.profile.entity.LandlordProfile;
import io.petproject.estate.booking.platform.profile.entity.TenantProfile;
import io.petproject.estate.booking.platform.profile.entity.UserProfile;
import io.petproject.estate.booking.platform.profile.mapper.config.MapperConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    config = MapperConfiguration.class,
    uses = {
        UserProfileMapper.class,
        TenantProfileMapper.class,
        LandlordProfileMapper.class
    }
)
public interface ProfileDetailsMapper {

    @Mapping(target = "profile", source = "profile")
    @Mapping(target = "tenant", source = "tenant")
    @Mapping(target = "landlord", source = "landlord")
    ProfileDetailsResponse toResponse(UserProfile profile,
                                      TenantProfile tenant,
                                      LandlordProfile landlord);

}
