package com.projeto.lab.implementacao.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SwaggerConfig {

        @SuppressWarnings("unchecked")
        @Bean
        public OpenAPI customOpenAPI() {
                final String securitySchemeName = "bearerAuth";

                return new OpenAPI()
                                .info(new Info()
                                                .title("Sistema Mérito Digital API")
                                                .version("1.0.0")
                                                .description("API para gerenciamento de alunos, professores e distribuição de moedas."))
                                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                                .components(new io.swagger.v3.oas.models.Components()
                                                .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                                                .name(securitySchemeName)
                                                                .type(SecurityScheme.Type.HTTP)
                                                                .scheme("bearer")
                                                                .bearerFormat("JWT"))
                                                .addRequestBodies("fileUpload", new RequestBody()
                                                                .content(new Content()
                                                                                .addMediaType("multipart/form-data",
                                                                                                new MediaType()
                                                                                                                .schema(new Schema<Object>()
                                                                                                                                .type("object")
                                                                                                                                .properties(Map.of(
                                                                                                                                                "file",
                                                                                                                                                new Schema<Object>()
                                                                                                                                                                .type("string")
                                                                                                                                                                .format("binary"))))))));
        }
}