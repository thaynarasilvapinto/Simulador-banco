package com.github.thaynarasilvapinto.web.config

import org.junit.runner.RunWith
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.context.web.WebAppConfiguration

@AutoConfigureMockMvc
@WebAppConfiguration
@SpringBootTest
@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [(ControllerTestConfig::class)])
abstract class ControllerBaseTest