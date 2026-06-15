package io.petproject.estate.booking.platform.profile.mapper;

import io.petproject.estate.booking.platform.profile.dto.request.UpsertTenantProfileRequest;
import io.petproject.estate.booking.platform.profile.dto.response.TenantProfileResponse;
import io.petproject.estate.booking.platform.profile.entity.TenantProfile;
import io.petproject.estate.booking.platform.profile.mapper.config.MapperConfiguration;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(config = MapperConfiguration.class)
public interface TenantProfileMapper {

    TenantProfileResponse toResponse(TenantProfile profile);

    @Mapping(target = "profile", ignore = true)
    @Mapping(target = "profileId", ignore = true)
    TenantProfile toEntity(UpsertTenantProfileRequest request);

    @Mapping(target = "profile", ignore = true)
    @Mapping(target = "profileId", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    void updateFromRequest(UpsertTenantProfileRequest request,
                           @MappingTarget TenantProfile profile);

}
