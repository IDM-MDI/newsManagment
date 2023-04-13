package ru.clevertec.newsmanagement.apigateway.filter;

import lombok.NoArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.clevertec.newsmanagement.apigateway.model.User;
import ru.clevertec.newsmanagement.apigateway.validator.AuthenticationValidator;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    private final WebClient.Builder webClientBuilder;

    public AuthFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if(!AuthenticationValidator.isHeaderContainAuthorization(exchange.getRequest().getHeaders())) {
                throw new RuntimeException("Missing authorization information");
            }
            String authorization = exchange.getRequest()
                    .getHeaders()
                    .get(HttpHeaders.AUTHORIZATION)
                    .get(0);
            String[] splitedAuthorization = authorization.split(" ");

            if(!AuthenticationValidator.isBearer(splitedAuthorization)) {
                throw new RuntimeException("Incorrect authorization structure");
            }

            return webClientBuilder.build()
                    .post()
                    .uri("http://user-service/users/api/v1/auth/validateToken?token=" + splitedAuthorization[1])
                    .retrieve()
                    .bodyToMono(User.class)
                    .map(response -> {
                        exchange.getRequest().mutate().header("username", response.getUsername());
                        exchange.getRequest().mutate().header("role", response.getRole());
                        exchange.getRequest().mutate().header("auth-token", response.getJwt());
                        return exchange;
                    })
                    .flatMap(chain::filter);
        };
    }

    @NoArgsConstructor
    public static class Config {

    }
}
