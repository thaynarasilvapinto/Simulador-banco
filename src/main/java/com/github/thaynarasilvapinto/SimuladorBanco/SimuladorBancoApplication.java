package com.github.thaynarasilvapinto.SimuladorBanco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing
public class SimuladorBancoApplication{

	public static void main(String[] args) {
		SpringApplication.run(SimuladorBancoApplication.class, args);
	}

}
