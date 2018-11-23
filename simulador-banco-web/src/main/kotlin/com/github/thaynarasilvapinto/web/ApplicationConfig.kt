package com.github.thaynarasilvapinto.web

import com.github.thaynarasilvapinto.repositories.config.RepositoryConfig
import com.github.thaynarasilvapinto.service.config.ServiceConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import


@SpringBootApplication
@Configuration
@Import(
    RepositoryConfig::class,
    ServiceConfig::class
)
@ComponentScan(basePackages = ["com.github.thaynarasilvapinto"])
open class ApplicationConfig