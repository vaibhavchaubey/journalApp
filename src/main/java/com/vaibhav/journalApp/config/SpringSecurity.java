package com.vaibhav.journalApp.config;

import com.vaibhav.journalApp.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurity {

    // Injecting our custom UserDetailsServiceImpl to fetch user details from DB
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    // Defines the main security configuration for HTTP requests
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Disabling CSRF since we're using stateless REST API (no session)
                .csrf(AbstractHttpConfigurer::disable)

                // Authorizing requests based on URL patterns
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**").permitAll()          // No auth needed for public endpoints
                        .requestMatchers("/journal/**", "/user/**").authenticated()  // Require authentication
                        .requestMatchers("/admin/**").hasRole("ADMIN")      // Only accessible by users with ADMIN role
                        .anyRequest().authenticated()                       // All other endpoints require auth
                )

                // Enabling basic auth (you can later switch to JWT or form login)
                .httpBasic(Customizer.withDefaults())

                // Ensuring the app is stateless (important for JWT)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .build();
    }

    // Provides the AuthenticationManager bean used in services for manual authentication (e.g. login method)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();  // This manager will internally use the DaoAuthenticationProvider
    }

    // Configures the authentication provider: how to fetch users + how to verify passwords

    /* Yes, you can absolutely use public AuthenticationProvider authenticationProvider() instead of public DaoAuthenticationProvider authenticationProvider() — and it’s often considered good practice to do so!

    ✅ Why it works:
    DaoAuthenticationProvider implements the AuthenticationProvider interface.

    So when you return a DaoAuthenticationProvider, it's valid to declare the return type as the interface AuthenticationProvider.

    ✅ Recommended practice:
    Using the interface (AuthenticationProvider) as the return type improves flexibility, testability, and code abstraction. You’re coding to an interface rather than a specific implementation. */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        // Link our custom user service to load user details from DB
        authProvider.setUserDetailsService(userDetailsService);

        // Password verification logic using bcrypt
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    // Password encoder bean using BCrypt (good standard for hashing)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


/* 🔁 In short:
        ✅ securityFilterChain() → Controls what endpoints are public/protected.

        ✅ DaoAuthenticationProvider → How login is validated: user + password check.

        ✅ AuthenticationManager → Used manually when verifying credentials (like in your verify() method).

        ✅ BCryptPasswordEncoder → Handles hashing and password match checks. */


/* Flow of verify(Users user):
     When you call authManager.authenticate(...), here’s what happens behind the scenes:

    1) Spring’s AuthenticationManager is used to handle the authentication.

    2) AuthenticationManager loops through its list of AuthenticationProviders.

    3) It finds your DaoAuthenticationProvider because you registered it as a @Bean.

    4) DaoAuthenticationProvider:
        Calls your UserDetailsServiceImpl to fetch the user by username.

        Uses the PasswordEncoder to compare the provided raw password with the hashed password in DB.

    5) If all checks pass, Spring returns an Authentication object that is authenticated.  */