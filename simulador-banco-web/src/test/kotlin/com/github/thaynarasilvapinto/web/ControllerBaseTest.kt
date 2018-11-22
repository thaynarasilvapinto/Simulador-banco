package com.github.thaynarasilvapinto.web

import com.github.thaynarasilvapinto.web.config.ControllerTestConfig
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

/*@AutoConfigureMockMvc*/
@WebAppConfiguration
@SpringBootTest
@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [(ControllerTestConfig::class)])
abstract class ControllerBaseTest{

    @Autowired
    private lateinit var context: WebApplicationContext
    @Autowired
    protected lateinit var mvc: MockMvc

    @Before
    fun setUp() {
        this.mvc = MockMvcBuilders
            .webAppContextSetup(this.context)
            .build()
    }
}