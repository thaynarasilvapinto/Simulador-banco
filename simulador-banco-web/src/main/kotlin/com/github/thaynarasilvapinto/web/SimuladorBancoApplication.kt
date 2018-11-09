package com.github.thaynarasilvapinto.web

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
open class SimuladorBancoApplication

fun main(args: Array<String>) {
    SpringApplication.run(SimuladorBancoApplication::class.java, *args)
}