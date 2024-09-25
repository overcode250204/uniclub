package com.cybersoft.uniclub06.config.security;

import com.cybersoft.uniclub06.filter.CustomFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public CorsConfigurationSource corsConfiguration() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); //encode theo chuẩn BCrypt
    }

    //ở đây khi thằng CustomAuthenProvider vẫn hiểu vì nó đã đưa lên IoC và truyền thành theo dạng tham số
    //nếu không gọi theo dạng tham số thì phải @AutoWired
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity, CustomAuthenProvider customAuthenProvider) throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(customAuthenProvider)
                .build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomFilter customFilter, CorsConfigurationSource corsConfiguration) throws Exception {



        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfiguration))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))// không cho xài session
                .authorizeHttpRequests(request -> {
                    request.requestMatchers(HttpMethod.POST,"/authen","/authen/validate").permitAll();
                    request.requestMatchers(HttpMethod.GET, "/file/**", "/product/**").permitAll();
                    request.requestMatchers("/product").hasRole("ADMIN");
                })
                .addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }
}
