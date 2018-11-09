package com.github.thaynarasilvapinto.repositories.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = ["com.github.thaynarasilvapinto.repositories"])
open class RepositoryConfig