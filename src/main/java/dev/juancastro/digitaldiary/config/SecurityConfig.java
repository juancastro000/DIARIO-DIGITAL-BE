package dev.juancastro.digitaldiary.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


import dev.juancastro.digitaldiary.security.JpaUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${api-endpoint}")
    private String endpoint;

    private final JpaUserDetailsService userDetailsService;

    public SecurityConfig(JpaUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(endpoint + "/public/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(endpoint + "/auth/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(endpoint + "/user/**")).authenticated()
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults());

        http.headers(headers -> headers
        .contentSecurityPolicy(csp -> csp.policyDirectives("frame-ancestors 'self'"))
        );
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder);
        return builder.build();
    }

    
}