package org.seenu.taskManager.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.seenu.taskManager.dto.UserSignUpRequestDto;
import org.seenu.taskManager.service.MyUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig {

    private final MyUserService myUserService;
    private final CorsConfig corsConfig;
    AppSecurityConfig(MyUserService myUserService, CorsConfig corsConfig) {
        this.myUserService = myUserService;
        this.corsConfig = corsConfig;
    }
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(bCryptPasswordEncoder());
        provider.setUserDetailsService(myUserService);
        return new ProviderManager(provider);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder()
    {

        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))
                .csrf(csrf -> csrf.disable()) // Keep disabled for REST APIs; enable for MVC
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Create session when needed
                        .maximumSessions(1)                                        // Optional: one session per user
                        .maxSessionsPreventsLogin(false)                           // Optional: new login kicks old session
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**").permitAll()   // Your auth controller (login, register, etc.)
                        .anyRequest().authenticated()
                );
        return http.build();
    }

    @Bean
    public Cache<String, UserSignUpRequestDto> pendingUserCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(1000)
                .build();
    }
}
