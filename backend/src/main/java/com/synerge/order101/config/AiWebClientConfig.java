package com.synerge.order101.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AiWebClientConfig {
    @Value("${ai.python.base-url}")
    private String pythonBaseUrl;

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder
                .baseUrl(pythonBaseUrl)
                // 버퍼 일단 늘려둠
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(10 * 1024 * 1024))  // 10MB
                        .build())
                .build();
    }
}
