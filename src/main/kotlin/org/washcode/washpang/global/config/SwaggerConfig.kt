package org.washcode.washpang.global.config

import org.springframework.context.annotation.Configuration
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI

@Configuration
class SwaggerConfig {

    fun info(): Info {
        return Info()
            .title("Wash Pang(Kotlin)")
            .version("2.0")
            .description("API 명세서")
    }

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .components(Components()
                .addSecuritySchemes("access-token", SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")))
            .addSecurityItem(SecurityRequirement().addList("access-token"))
            .info(info())
    }
}