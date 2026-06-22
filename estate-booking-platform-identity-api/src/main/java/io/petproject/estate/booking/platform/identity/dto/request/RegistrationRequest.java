package io.petproject.estate.booking.platform.identity.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistrationRequest(
    @Email @NotBlank @Size(max = 255)
    String email,

    @NotBlank @Size(max = 255)
    String givenName,

    @NotBlank @Size(max = 255)
    String familyName,

    @NotBlank @Size(min = 12, max = 128)
    String password
) { }
