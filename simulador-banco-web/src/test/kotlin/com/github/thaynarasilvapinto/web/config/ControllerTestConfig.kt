package com.github.thaynarasilvapinto.web.config

import com.github.thaynarasilvapinto.web.ApplicationConfig
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(ApplicationConfig::class)
open class ControllerTestConfig