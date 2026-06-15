package io.petproject.estate.booking.platform.profile.mapper;

import io.petproject.estate.booking.platform.profile.dto.request.UpdateLandlordVerificationStatusRequest;
import io.petproject.estate.booking.platform.profile.dto.request.UpsertLandlordProfileRequest;
import io.petproject.estate.booking.platform.profile.dto.response.LandlordProfileResponse;
import io.petproject.estate.booking.platform.profile.entity.LandlordProfile;
import io.petproject.estate.booking.platform.profile.mapper.config.MapperConfiguration;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(config = MapperConfiguration.class)
public interface LandlordProfileMapper {

    LandlordProfileResponse toResponse(LandlordProfile profile);

    @Mapping(target = "profile", ignore = true)
    @Mapping(target = "profileId", ignore = true)
    @Mapping(target = "verificationStatus", constant = "PENDING")
    LandlordProfile toEntity(UpsertLandlordProfileRequest request);

    @Mapping(target = "profile", ignore = true)
    @Mapping(target = "profileId", ignore = true)
    @Mapping(target = "verificationStatus", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    void updateFromRequest(UpsertLandlordProfileRequest request,
                           @MappingTarget LandlordProfile entity);

    @Mapping(target = "profile", ignore = true)
    @Mapping(target = "profileId", ignore = true)
    @Mapping(target = "taxNumber", ignore = true)
    @Mapping(target = "companyName", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    void updateVerificationStatus(UpdateLandlordVerificationStatusRequest request,
                                  @MappingTarget LandlordProfile profile);

}
