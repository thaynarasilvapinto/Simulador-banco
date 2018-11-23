package com.github.thaynarasilvapinto.service.config

import com.github.thaynarasilvapinto.repositories.config.RepositoryConfig
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
@Import(RepositoryConfig::class)
@ComponentScan(basePackages = ["com.github.thaynarasilvapinto.service"])
open class ServiceConfig