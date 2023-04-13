package ru.clevertec.newsmanagement.apigateway.filter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.clevertec.newsmanagement.apigateway.model.User;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthFilterTest {

    @Mock
    private WebClient.Builder webClientBuilder;
    @Mock
    private WebClient webClient;
    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    @Mock
    private WebClient.RequestBodySpec requestBodySpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private GatewayFilterChain filterChain;
    @Mock
    private ServerWebExchange exchange;
    @Mock
    private ServerHttpRequest request;
    @Mock
    private ServerHttpResponse response;
    @InjectMocks
    private AuthFilter authFilter;


    @Test
    void shouldThrowExceptionIfAuthorizationHeaderIsMissing() {
        doReturn(new HttpHeaders())
                .when(request).getHeaders();
        doReturn(request)
                .when(exchange).getRequest();

        Assertions.assertThatThrownBy(() -> authFilter.apply(new AuthFilter.Config()).filter(exchange, null))
                        .isInstanceOf(RuntimeException.class)
                        .hasMessageContaining("Missing authorization information");
    }

    @Test
    void shouldThrowExceptionIfAuthorizationHeaderDoesNotContainBearer() {
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.AUTHORIZATION, Collections.singletonList("Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ=="));
        doReturn(headers)
                .when(request).getHeaders();
        doReturn(request)
                .when(exchange).getRequest();

        Assertions.assertThatThrownBy(() -> authFilter.apply(new AuthFilter.Config()).filter(exchange, null))
                        .isInstanceOf(RuntimeException.class)
                        .hasMessageContaining("Incorrect authorization structure");
    }

    @Test
    void shouldInvokeDownstreamFiltersIfAuthorizationHeaderIsValid() {
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.AUTHORIZATION, Collections.singletonList("Bearer abc123"));
        doReturn(headers)
                .when(request).getHeaders();
        doReturn(request)
                .when(exchange).getRequest();

        doReturn(webClient)
                .when(webClientBuilder).build();
        doReturn(requestBodyUriSpec)
                .when(webClient).post();
        doReturn(requestBodySpec)
                .when(requestBodyUriSpec).uri(any(String.class));
        doReturn(responseSpec)
                .when(requestBodySpec).retrieve();
        doReturn(Mono.just(new User("username", "admin", "jwt")))
                .when(responseSpec).bodyToMono(User.class);

        Mono<Void> result = authFilter.apply(new AuthFilter.Config()).filter(exchange, filterChain);

        verify(webClient, times(1)).post();
        verify(requestBodyUriSpec, times(1)).uri("http://user-service/users/api/v1/auth/validateToken?token=abc123");
        verify(requestBodySpec, times(1)).retrieve();
        verify(responseSpec, times(1)).bodyToMono(User.class);
    }
}