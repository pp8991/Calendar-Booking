package com.calendar.booking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Calendar Booking System API")
                        .version("1.0")
                        .description("API documentation for the Calendar Booking System")
                        .contact(new Contact()
                                .name("Prakhar Pandey")
                                .email("prakharpandey96@gmail.com")));
    }
}
