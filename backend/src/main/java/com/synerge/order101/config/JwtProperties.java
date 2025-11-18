package com.synerge.order101.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Setter
@Getter
@NoArgsConstructor
@Component
@ConfigurationProperties("jwt")
public class JwtProperties {
    private String issuer;

    private String secret;
}
