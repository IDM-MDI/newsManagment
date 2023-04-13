package ru.clevertec.newsmanagement.apigateway.validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

class AuthenticationValidatorTest {

    @Test
    void isHeaderContainAuthorizationShouldReturnTrue() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "value");

        boolean actual = AuthenticationValidator.isHeaderContainAuthorization(headers);

        Assertions.assertThat(actual)
                .isTrue();
    }
    @Test
    void isHeaderContainAuthorizationShouldReturnFalse() {
        HttpHeaders headers = new HttpHeaders();

        boolean actual = AuthenticationValidator.isHeaderContainAuthorization(headers);

        Assertions.assertThat(actual)
                .isFalse();
    }
    @Test
    void isBearerShouldReturnTrue() {
        String[] bearer = new String[] {"Bearer", "value"};

        boolean actual = AuthenticationValidator.isBearer(bearer);

        Assertions.assertThat(actual)
                .isTrue();
    }
    @Test
    void isBearerShouldReturnFalseByNotBearer() {
        String[] bearer = new String[] {"value", "value"};

        boolean actual = AuthenticationValidator.isBearer(bearer);

        Assertions.assertThat(actual)
                .isFalse();
    }
}