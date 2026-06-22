package io.petproject.estate.booking.platform.identity.dto.request;

import io.petproject.estate.booking.platform.identity.dto.UserProfileStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateUserProfileRequest(
    @NotNull
    UUID userId,

    @Email
    @NotBlank
    @Size(max = 255)
    String email,

    @Size(max = 32)
    String phone,

    @Size(max = 255)
    String displayName,

    @NotNull
    UserProfileStatus status
) { }
