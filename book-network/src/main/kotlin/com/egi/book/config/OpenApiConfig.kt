package com.egi.book.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.annotations.servers.Server

@OpenAPIDefinition(
    info =
        Info(
            title = "OpenApi specification",
            version = "1.0",
            description = "OpenApi documentation for Spring Security",
            license =
                io.swagger.v3.oas.annotations.info.License(
                    name = "License name",
                    url = "https://some-url.com",
                ),
            contact =
                Contact(
                    name = "\${application.account.name}",
                    email = "\${application.account.email}",
                ),
            termsOfService = "Terms of service",
        ),
    servers = [
        Server(
            url = "http://localhost:8088/api/v1",
            description = "Local ENV",
        ),
    ],
    security = [
        SecurityRequirement(name = "bearerAuth"),
    ],
)
@SecurityScheme(
    name = "bearerAuth",
    description = "JWT auth description",
    scheme = "bearer",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    `in` = SecuritySchemeIn.HEADER,
)
class OpenApiConfig
