package io.petproject.estate.booking.platform.identity.client;

import feign.FeignException;
import feign.RetryableException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import io.petproject.estate.booking.platform.identity.dto.request.CreateUserProfileRequest;
import io.petproject.estate.booking.platform.identity.dto.response.UserProfileResponse;
import io.petproject.estate.booking.platform.identity.exception.ProfileServiceIntegrationException;
import io.petproject.estate.booking.platform.identity.exception.ProfileServiceUnavailableException;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

@Component
public class ProfileServiceGateway {

    private static final String INSTANCE_NAME = "profile-service";

    private final ProfileServiceClient client;
    private final Retry retry;
    private final CircuitBreaker circuitBreaker;

    public ProfileServiceGateway(
        ProfileServiceClient client,
        RetryRegistry retryRegistry,
        CircuitBreakerRegistry circuitBreakerRegistry
    ) {
        this.client = client;
        this.retry = retryRegistry.retry(INSTANCE_NAME);
        this.circuitBreaker = circuitBreakerRegistry.circuitBreaker(INSTANCE_NAME);
    }

    public UserProfileResponse createProfile(CreateUserProfileRequest request) {
        Supplier<UserProfileResponse> guardedCall = CircuitBreaker.decorateSupplier(
            circuitBreaker,
            () -> createOrReconcile(request)
        );
        try {
            return Retry.decorateSupplier(retry, guardedCall).get();
        } catch (CallNotPermittedException exception) {
            throw new ProfileServiceUnavailableException("Profile Service circuit breaker is open", exception);
        } catch (ProfileServiceIntegrationException | ProfileServiceUnavailableException exception) {
            throw exception;
        } catch (RuntimeException exception) {
            throw new ProfileServiceUnavailableException("Profile Service is unavailable", exception);
        }
    }

    private UserProfileResponse createOrReconcile(CreateUserProfileRequest request) {
        try {
            return Objects.requireNonNull(client.createProfile(request).getBody());
        } catch (FeignException.Conflict exception) {
            return reconcile(request, exception);
        } catch (RetryableException exception) {
            return reconcileOrUnavailable(request, exception);
        } catch (FeignException exception) {
            if (exception.status() >= 500) {
                return reconcileOrUnavailable(request, exception);
            }
            throw new ProfileServiceIntegrationException(
                "Profile Service rejected profile creation", exception);
        }
    }

    private UserProfileResponse reconcileOrUnavailable(
        CreateUserProfileRequest request,
        RuntimeException originalException
    ) {
        try {
            return reconcile(request, originalException);
        } catch (FeignException.NotFound notFound) {
            throw new ProfileServiceUnavailableException("Profile Service is unavailable", originalException);
        } catch (RetryableException | FeignException.InternalServerError reconciliationException) {
            originalException.addSuppressed(reconciliationException);
            throw new ProfileServiceUnavailableException("Profile Service is unavailable", originalException);
        }
    }

    private UserProfileResponse reconcile(
        CreateUserProfileRequest request,
        RuntimeException originalException
    ) {
        UserProfileResponse existing = client.getProfileByUserId(request.userId());
        assertSameProfile(request, existing, originalException);
        return existing;
    }

    private static void assertSameProfile(
        CreateUserProfileRequest request,
        UserProfileResponse existing,
        RuntimeException cause
    ) {
        UUID existingUserId = existing.userId();
        if (!request.userId().equals(existingUserId)
            || !request.email().equalsIgnoreCase(existing.email())) {
            throw new ProfileServiceIntegrationException(
                "Existing profile does not match the registration request", cause);
        }
    }
}
