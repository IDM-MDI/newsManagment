package ru.clevertec.newsmanagement.newsservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Configuration class for WebProto that implements {@link WebMvcConfigurer}.
 * <p>
 * This class adds a {@link ProtobufHttpMessageConverter} to the list of {@link HttpMessageConverter}s used by Spring MVC.
 * This enables the application to send and receive Protocol Buffers messages over HTTP.
 * @author Dayanch
 */
@Configuration
public class WebProtoConfig implements WebMvcConfigurer {

    /**
     * Adds a {@link ProtobufHttpMessageConverter} to the list of {@link HttpMessageConverter}s used by Spring MVC.
     * @param converters the list of {@link HttpMessageConverter} to extend
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new ProtobufHttpMessageConverter());
    }
}
