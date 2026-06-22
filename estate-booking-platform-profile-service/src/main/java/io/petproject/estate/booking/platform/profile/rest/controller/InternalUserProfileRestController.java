package io.petproject.estate.booking.platform.profile.rest.controller;

import io.petproject.estate.booking.platform.profile.dto.request.CreateUserProfileRequest;
import io.petproject.estate.booking.platform.profile.dto.request.SyncIdentityProfileRequest;
import io.petproject.estate.booking.platform.profile.dto.response.UserProfileResponse;
import io.petproject.estate.booking.platform.profile.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/api/v1/user-profiles")
public class InternalUserProfileRestController {

    private final UserProfileService userProfileService;

    @PostMapping
    public ResponseEntity<UserProfileResponse> createProfile(@Valid @RequestBody CreateUserProfileRequest request) {
        UserProfileResponse response = userProfileService.createProfile(request);
        return ResponseEntity.status(CREATED).body(response);
    }

    @PatchMapping("/by-user-id/{userId}")
    public ResponseEntity<UserProfileResponse> syncProfileIdentity(@RequestBody SyncIdentityProfileRequest request,
                                                                   @PathVariable UUID userId) {
        UserProfileResponse response = userProfileService.syncProfileIdentity(userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-user-id/{userId}")
    public ResponseEntity<UserProfileResponse> getProfileByUserId(@PathVariable UUID userId) {
        UserProfileResponse response = userProfileService.findByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/compensate-registration/{userId}")
    public ResponseEntity<Void> compensateRegistration(@PathVariable UUID userId) {
        userProfileService.hardDelete(userId);
        return ResponseEntity.noContent().build();
    }

}
