package com.vinod.ptcp_app.config;

import com.vinod.ptcp_app.security.JwtAuthenticationFilter;
import com.vinod.ptcp_app.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailsManager.setUsersByUsernameQuery("SELECT username, password, true FROM Users WHERE username=?");
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("SELECT username, role FROM Users WHERE username=?");
        return jdbcUserDetailsManager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/api/auth/**").disable()) // Disable CSRF for auth endpoints
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Enable CORS with correct CorsConfigurationSource
                .authorizeHttpRequests(configure -> configure
                        .requestMatchers("/api/auth/**").permitAll() // Allow login/register
                        .requestMatchers("/ws/**").permitAll() // Allow WebSocket connections
                        .requestMatchers(HttpMethod.GET, "/api/files").hasAnyAuthority("ROLE_ADMIN", "ROLE_TEACHER", "ROLE_PARENT")
                        .requestMatchers(HttpMethod.GET, "/api/chat/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_TEACHER", "ROLE_PARENT")
                        .requestMatchers(HttpMethod.GET, "/api/files/download/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_TEACHER", "ROLE_PARENT")
                        .requestMatchers(HttpMethod.POST, "/api/files/upload").hasAnyAuthority("ROLE_ADMIN", "ROLE_TEACHER")
                        .requestMatchers("/api/events/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_TEACHER", "ROLE_PARENT")
                ).addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class) // Add JWT filter here
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Use stateless JWT
                .httpBasic(Customizer.withDefaults()); // HTTP Basic authentication (optional if you're using JWT)
        // Disable cross-site request forgery (CSRF)
        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000")); // Specify your frontend's URL here
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Allowed HTTP methods
        config.setAllowedHeaders(List.of("*")); // Allow all headers
        config.setAllowCredentials(true); // Allow cookies/auth headers

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // Apply CORS settings to all endpoints
        return source;
    }
}
