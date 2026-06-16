package io.petproject.estate.booking.platform.profile.service;

import io.petproject.estate.booking.platform.profile.dto.request.CreateUserProfileRequest;
import io.petproject.estate.booking.platform.profile.dto.request.SyncIdentityProfileRequest;
import io.petproject.estate.booking.platform.profile.dto.response.UserProfileResponse;
import io.petproject.estate.booking.platform.profile.entity.UserProfile;
import io.petproject.estate.booking.platform.profile.exception.UserProfileAlreadyExistsException;
import io.petproject.estate.booking.platform.profile.exception.UserProfileNotFoundException;
import io.petproject.estate.booking.platform.profile.mapper.UserProfileMapper;
import io.petproject.estate.booking.platform.profile.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileMapper userProfileMapper;
    private final UserProfileRepository userProfileRepository;

    @Transactional
    public UserProfileResponse createProfile(CreateUserProfileRequest request) {
        UserProfile userProfile = userProfileMapper.toEntity(request);
        try {
            userProfileRepository.saveAndFlush(userProfile);
        } catch (DataIntegrityViolationException e) {
            throw new UserProfileAlreadyExistsException(
                "User profile already exists with email [%s] or userId [%s]"
                    .formatted(userProfile.getEmail(), userProfile.getUserId()), e
            );
        }
        log.info("IN - createProfile: userProfile [{}] successfully created", userProfile.getEmail());
        return userProfileMapper.toResponse(userProfile);
    }

    @Transactional
    public UserProfileResponse syncProfileIdentity(UUID userId, SyncIdentityProfileRequest request) {
        UserProfile existingProfile = userProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new UserProfileNotFoundException("User profile not found: " + userId));
        userProfileMapper.syncFromIdentity(request, existingProfile);
        try {
            userProfileRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new UserProfileAlreadyExistsException(
                "User profile already exists with email [%s]"
                    .formatted(existingProfile.getEmail()), e);
        }
        log.info("IN - syncProfileIdentity: userProfile [{}] is synchronized with identity", existingProfile.getEmail());
        return userProfileMapper.toResponse(existingProfile);
    }

    @Transactional(readOnly = true)
    public UserProfileResponse findByUserId(UUID userId) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new UserProfileNotFoundException("User profile not found: " + userId));
        log.info("IN - findByUserId: userProfile [{}] successfully found", userProfile.getEmail());
        return userProfileMapper.toResponse(userProfile);
    }

    @Transactional
    public void hardDelete(UUID userId) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new UserProfileNotFoundException("User profile not found: " + userId));
        userProfileRepository.delete(userProfile);
        log.info("IN - hardDelete: userProfile [{}] successfully deleted", userProfile.getEmail());
    }

}
