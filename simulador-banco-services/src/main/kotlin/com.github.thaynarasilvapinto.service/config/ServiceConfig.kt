package com.github.thaynarasilvapinto.service.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = ["com.github.thaynarasilvapinto.service"])
open class ServiceConfigopen class ServiceConfig