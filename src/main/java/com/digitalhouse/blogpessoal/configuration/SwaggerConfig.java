package com.digitalhouse.blogpessoal.configuration;

import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;

@Configuration //indicar q é classe de configuração
public class SwaggerConfig {

	//indicando q ele deve invocar aquele met e vai gerenciar o met q ele vai returnar pra ele -> pode ser injetado emqq ponto da minha aplicacao
	@Bean //utiliza em mets de uma classe q geralmente é marcada com @Configuration, classe de configuraçao
	public OpenAPI springBlogPessoalOpenAPI() {
		
		return new OpenAPI()
			.info(new Info()	
				.title("Aplicação do Blog Pessoal")
				.description("PRojeto Desenvolvido durante o treinamento Full Stack da Digital House")
				.version("v.0.1")
				
			.license(new License()
				.name("Digital House")
				.url("https://digitalhouse.com")
			)
			
			.contact(new Contact()
				.name("Treinamento Porto Seguro")
				.email("portoseguro@porto.com"))
			)
			
			.externalDocs(new ExternalDocumentation()
					.description("GitHub")
					.url("https://github.com/portoseguro")
			);
		
	}	

	//OpenApiCustomiser -> mét q permite personalizar as msgs de erros do meu HttpResponse lá no Swagger
	@Bean
	public OpenApiCustomiser custumerGlobalHeaderOpenApiCustomiser() {
		
		return openApi -> {
						//vai retornar o caminho de cada endpoint
			openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {
				ApiResponses apiResponses = operation.getResponses();
				
				apiResponses.addApiResponse("200", createApiResponse("Sucesso!"));
				apiResponses.addApiResponse("201", createApiResponse("Objeto Persistido"));
				apiResponses.addApiResponse("204", createApiResponse("Objeto Excluído"));
				apiResponses.addApiResponse("400", createApiResponse("Erro na requisição"));
				apiResponses.addApiResponse("401", createApiResponse("Acesso não autorizado"));
				apiResponses.addApiResponse("404", createApiResponse("Objeto não encontrado"));
				apiResponses.addApiResponse("500", createApiResponse("Erro na aplicação"));
			
			}));
			
		};
		
	}
	
	
	private ApiResponse createApiResponse(String message) {
	
		return new ApiResponse().description(message);
		
	}
	
}

//é uma espécie de cabeçalho, onde tem todas as infos da minha API
