package com.github.thaynarasilvapinto.web

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
open class SwaggerConfig {

    @Bean
    open fun api(): Docket = Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.github.thaynarasilvapinto.web"))
        .paths(PathSelectors.any()).build()
        .apiInfo(apiInfo())

    private fun apiInfo(): ApiInfo {
        return ApiInfoBuilder().title("Simulador Banco")
            .description("Um projeto de treinamento para preparar o(a) estagiário(a) para que o mesmo consiga desenvolver features reais.").version("1.0").build()
    }
}