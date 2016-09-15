package com.ppawel.articles;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableJpaRepositories
@SpringBootApplication
public class ArticlesApplication {

    /**
     * Overrides default HTTP security configuration.
     * <p>
     * Note: authentication uses the default Spring Boot setup (username/password defined in application properties).
     */
    @Configuration
    @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
    protected static class ApplicationSecurity extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    // Disable CSRF for simplicity of the task - would need additional CSRF token handling
                    // in RestAssured REST API tests.
                    .csrf().disable()

                    // CRUD requests only available to editors, get (search/list) to all
                    .authorizeRequests()
                    .antMatchers(HttpMethod.GET).permitAll()
                    .antMatchers(HttpMethod.DELETE).hasRole("EDITOR")
                    .antMatchers(HttpMethod.PUT).hasRole("EDITOR")
                    .antMatchers(HttpMethod.POST).hasRole("EDITOR")
                    .and().httpBasic();
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(ArticlesApplication.class, args);
    }
}
