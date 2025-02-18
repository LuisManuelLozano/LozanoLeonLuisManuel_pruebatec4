package com.luis.agencia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.luis.agencia", "com.luis.agencia.config"}) // Ajusta los paquetes según tu estructura
public class AgenciaApplication {
	public static void main(String[] args) {
		SpringApplication.run(AgenciaApplication.class, args);
	}
}
