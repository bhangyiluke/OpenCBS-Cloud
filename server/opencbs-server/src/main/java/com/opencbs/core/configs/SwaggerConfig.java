package com.opencbs.core.configs;

import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "OpenCBS API Documentation",
                version = "0.1.0",
                contact = @Contact(name = "OpenCBS", url = "https://www.opencbs.com", email = "contact@opencbs.com")
        )
)
@Configuration
public class SwaggerConfig {
    // Basic OpenAPI definition via annotations. springdoc will auto-configure the
    // UI at `/swagger-ui.html` or `/swagger-ui/index.html` depending on the version.
}