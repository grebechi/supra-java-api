package br.com.gabrielrebechi.suprajava.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API SupraJava")
                        .version("1.0")
                        .description("API desenvolvida para a disciplina de Programação Orientada a Objetos 1 (POO1). "
                                + "Este sistema permite a criação e gestão de orçamentos com fornecedores, produtos, unidades de medida e itens.")
                        .contact(new Contact()
                                .name("Gabriel Rebechi")
                                .email("2024020316@aluno.osorio.ifrs.edu.br")
                                .url("https://gabrielrebechi.com.br"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
