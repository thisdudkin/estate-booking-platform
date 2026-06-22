package io.petproject.estate.booking.platform.identity.client;

import feign.Request;
import io.petproject.estate.booking.platform.identity.config.properties.ProfileServiceProperties;
import org.springframework.context.annotation.Bean;

class ProfileServiceFeignConfiguration {

    @Bean
    Request.Options profileServiceRequestOptions(ProfileServiceProperties properties) {
        return new Request.Options(
            properties.connectTimeout(),
            properties.readTimeout(),
            true
        );
    }

}
