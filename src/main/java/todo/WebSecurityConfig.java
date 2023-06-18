package todo;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private static final String ROLE_USER = "user";
    private static final String[] REGEX_API_PATH = { "/v1/todo", "/v1/todo**/**" };

    private final JwtConverter jwtConverter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.authorizeHttpRequests() //
                .requestMatchers(GET, REGEX_API_PATH).hasRole(ROLE_USER) //
                .requestMatchers(POST, REGEX_API_PATH).hasRole(ROLE_USER) //
                .requestMatchers(PATCH, REGEX_API_PATH).hasRole(ROLE_USER) //
                .requestMatchers(DELETE, REGEX_API_PATH).hasRole(ROLE_USER) //
                .anyRequest().authenticated();

        http.oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtConverter);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }
}
