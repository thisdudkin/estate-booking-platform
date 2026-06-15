package io.petproject.estate.booking.platform.profile.mapper;

import io.petproject.estate.booking.platform.profile.dto.request.CreateUserProfileRequest;
import io.petproject.estate.booking.platform.profile.dto.request.SyncIdentityProfileRequest;
import io.petproject.estate.booking.platform.profile.dto.request.UpdateUserProfileRequest;
import io.petproject.estate.booking.platform.profile.dto.response.UserProfileResponse;
import io.petproject.estate.booking.platform.profile.entity.UserProfile;
import io.petproject.estate.booking.platform.profile.mapper.config.MapperConfiguration;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(config = MapperConfiguration.class)
public interface UserProfileMapper {

    @Mapping(target = "profileId", source = "id")
    UserProfileResponse toResponse(UserProfile profile);

    @Mapping(target = "id", ignore = true)
    UserProfile toEntity(CreateUserProfileRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    void updateFromRequest(UpdateUserProfileRequest request,
                           @MappingTarget UserProfile profile);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "displayName", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    void syncFromIdentity(SyncIdentityProfileRequest request,
                          @MappingTarget UserProfile profile);

}
