package com.github.thaynarasilvapinto.web

import com.github.thaynarasilvapinto.repositories.config.RepositoryConfig
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@SpringBootApplication
@Configuration
@Import(RepositoryConfig::class)
@ComponentScan(basePackages = ["com.github.thaynarasilvapinto"])
open class SimuladorBancoApplication

fun main(args: Array<String>) {
    SpringApplication.run(SimuladorBancoApplication::class.java, *args)
}