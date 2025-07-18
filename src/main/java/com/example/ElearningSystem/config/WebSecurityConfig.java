package com.example.ElearningSystem.config;



import com.example.ElearningSystem.service.JWTService;
import com.example.ElearningSystem.utils.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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


@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

   @Autowired
   private UserDetailsService userDetailsService;
   @Autowired
   private JWTFilter jwtFilter;
   @Autowired
   private JWTService jwtService;
   @Bean
 public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

//csrf enabled is default, so we don't need to call it here
// additionally, that method is deprecated for spring 6.1

      return http
               .csrf(csrf->csrf.disable())
              .authenticationProvider(authenticationProvider())
               .authorizeHttpRequests(request->request
                       .requestMatchers("/auth/token/login").permitAll()
                       .requestMatchers("/api/adminControl/*").hasAuthority(UserRoles.Admin.name())
                       .requestMatchers("/api/student/*").hasAuthority(UserRoles.Student.name())
                       .anyRequest().authenticated())
               .sessionManagement(session->session
                       .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
              .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
              .oauth2ResourceServer(oauth->oauth
                      .jwt(Customizer.withDefaults()))
               .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

@Bean
    public AuthenticationProvider authenticationProvider(){
    DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder());//verifying the password using this password encoder method
    return provider;

    }





}
