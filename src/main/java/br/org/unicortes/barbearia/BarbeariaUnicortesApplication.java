package br.org.unicortes.barbearia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class BarbeariaUnicortesApplication {

	public static void main(String[] args) {
		SpringApplication.run(BarbeariaUnicortesApplication.class, args);
	}

}