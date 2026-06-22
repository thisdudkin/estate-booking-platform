package io.petproject.estate.booking.platform.identity.client;

import io.petproject.estate.booking.platform.identity.dto.request.CreateUserProfileRequest;
import io.petproject.estate.booking.platform.identity.dto.response.UserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
    name = "profile-service",
    url = "${application.integration.profile-service.base-url}",
    path = "/internal/api/v1/user-profiles",
    configuration = ProfileServiceFeignConfiguration.class
)
public interface ProfileServiceClient {

    @PostMapping
    ResponseEntity<UserProfileResponse> createProfile(@RequestBody CreateUserProfileRequest request);

    @GetMapping("/by-user-id/{userId}")
    UserProfileResponse getProfileByUserId(@PathVariable UUID userId);

}
