package ru.clevertec.newsmanagement.apigateway.validator;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;

@UtilityClass
public class AuthenticationValidator {
    private static final String BEARER = "Bearer";
    public boolean isHeaderContainAuthorization(HttpHeaders headers) {
        return headers.containsKey(HttpHeaders.AUTHORIZATION);
    }

    public boolean isBearer(String[] splitedAuthorization) {
        return splitedAuthorization.length != 2 || BEARER.equals(splitedAuthorization[0]);
    }
}
