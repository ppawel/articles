package com.ppawel.articles;

import org.springframework.boot.actuate.autoconfigure.EndpointWebMvcAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * Test context designed for data access layer tests - excludes web-related
 * autoconfigurations to speed up execution.
 */
@Configuration
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class, WebMvcAutoConfiguration.class,
        ManagementWebSecurityAutoConfiguration.class, EndpointWebMvcAutoConfiguration.class})
public class DataAccessTestContext {
}
