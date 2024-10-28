package com.example.spring.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // BCryptPasswordEncoder for encoding passwords
    }

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private UserDetailsService userDetailsService;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("SecurityConfig: [Class: SecurityConfig] Configuring security filter chain.");
    
        return http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(requests -> requests
                // Allow all users to access login and registration endpoints
                .requestMatchers("/users/login", "/error", "/users/login/teacher", "/users/registerManagement","/users/login/student").permitAll()
    
                // Students' Access Control
                .requestMatchers(HttpMethod.POST, "/students").hasAnyAuthority("MANAGEMENT", "TEACHER") // Management and teachers can add students
                .requestMatchers(HttpMethod.GET, "/students/all").hasAnyAuthority("MANAGEMENT", "TEACHER") // All roles can view students
                .requestMatchers(HttpMethod.POST, "/users/registerStudent").hasAnyAuthority("MANAGEMENT","TEACHER") // Only management can register teachers
                .requestMatchers(HttpMethod.GET, "/students/me").hasAuthority("STUDENT") // All roles can view students
                .requestMatchers(HttpMethod.PUT, "/students/**").hasAnyAuthority("MANAGEMENT", "TEACHER") // Management and teachers can update students
                .requestMatchers(HttpMethod.DELETE, "/students/**").hasAuthority("MANAGEMENT") // Only management can delete students
    
                // Teachers' Access Control
                .requestMatchers(HttpMethod.POST, "/teachers").hasAuthority("MANAGEMENT") // Only management can add teachers
                .requestMatchers(HttpMethod.POST, "/users/registerTeacher").hasAuthority("MANAGEMENT") // Only management can register teachers
                .requestMatchers(HttpMethod.GET, "/teachers/all").hasAnyAuthority("MANAGEMENT") // Both management and teachers can view teachers
                .requestMatchers(HttpMethod.GET, "/teachers/me").hasAuthority("TEACHER") // Only teachers can view their own details
                .requestMatchers(HttpMethod.PUT, "/teachers/**").hasAnyAuthority("MANAGEMENT", "TEACHER") // Both management and teachers can update teachers
                .requestMatchers(HttpMethod.DELETE, "/teachers/**").hasAuthority("MANAGEMENT") // Only management can delete teachers
    
                // All other requests need to be authenticated
                .anyRequest().authenticated())
    
            // Configure basic authentication and JWT filter
            .httpBasic(Customizer.withDefaults())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Use stateless sessions
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // Add JWT filter before the default authentication filter
            .build();
    }
    
    @Bean
    public AuthenticationProvider authenticationProvider() {
        logger.info("SecurityConfig: [Class: SecurityConfig] Configuring authentication provider.");
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        logger.info("SecurityConfig: [Class: SecurityConfig] Configuring authentication manager.");
        return config.getAuthenticationManager();
    }
}