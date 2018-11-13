package com.github.thaynarasilvapinto.web.config

import com.github.thaynarasilvapinto.web.SimuladorBancoApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@Configuration
@EnableAutoConfiguration
@Import(SimuladorBancoApplication::class)
@ActiveProfiles(profiles = ["test"])
@ComponentScan(basePackages = ["com.github.thaynarasilvapinto.web"])
open class ControllerTestConfig