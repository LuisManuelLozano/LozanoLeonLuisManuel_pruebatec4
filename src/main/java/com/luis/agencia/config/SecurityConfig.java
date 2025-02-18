package com.luis.agencia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Deshabilita CSRF (solo para desarrollo)
                .cors(withDefaults()) // Habilita CORS
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/**").permitAll() // Permite GET a todas las URLs
                        .anyRequest().authenticated() // Requiere autenticación para cualquier otra petición
                )
                .httpBasic(withDefaults());

        return http.build();
    }

    @Bean
    public HttpFirewall allowUrlEncodedPercentAndSlashHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true); // Permite barras codificadas en URL
        firewall.setAllowUrlEncodedPercent(true); // Permite caracteres "%" en la URL
        firewall.setAllowUrlEncodedPeriod(true); // Permite caracteres "." en la URL
        firewall.setAllowSemicolon(true); // Permite caracteres ";" en la URL
        firewall.setAllowBackSlash(true); // Permite caracteres "\" en la URL
        return firewall;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.httpFirewall(allowUrlEncodedPercentAndSlashHttpFirewall());
    }
}











