package com.luis.agencia.config;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig { // O el nombre de tu clase de configuración

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
