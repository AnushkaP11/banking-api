package com.bank.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Banking API",
                version = "v1.0.0",
                description = "REST API for managing customers, accounts, and transactions in a mini banking system",
                contact = @Contact(name = "Banking API Support", email = "support@bank.com"),
                license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0")
        ),
        servers = @Server(url = "http://localhost:8080", description = "Local Development Server")
)
public class OpenApiConfig {
}

