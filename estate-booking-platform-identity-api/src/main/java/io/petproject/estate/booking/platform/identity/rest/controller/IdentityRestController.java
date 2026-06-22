package io.petproject.estate.booking.platform.identity.rest.controller;

import io.petproject.estate.booking.platform.identity.dto.request.RegistrationRequest;
import io.petproject.estate.booking.platform.identity.entity.enums.RegistrationRole;
import io.petproject.estate.booking.platform.identity.service.IdentityService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/register")
public class IdentityRestController {

    private final IdentityService identityService;

    @PostMapping("/tenant")
    public ResponseEntity<Void> registerTenant(
        @RequestHeader("Idempotency-Key") @NotBlank @Size(max = 128) String idempotencyKey,
        @Valid @RequestBody RegistrationRequest request
    ) {
        identityService.register(idempotencyKey, request, RegistrationRole.TENANT);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/landlord")
    public ResponseEntity<Void> registerLandlord(
        @RequestHeader("Idempotency-Key") @NotBlank @Size(max = 128) String idempotencyKey,
        @Valid @RequestBody RegistrationRequest request
    ) {
        identityService.register(idempotencyKey, request, RegistrationRole.LANDLORD);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
