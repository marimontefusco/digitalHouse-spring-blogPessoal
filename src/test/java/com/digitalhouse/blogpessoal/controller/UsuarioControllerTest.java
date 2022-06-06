package com.digitalhouse.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.digitalhouse.blogpessoal.model.Usuario;
import com.digitalhouse.blogpessoal.repository.UsuarioRepository;
import com.digitalhouse.blogpessoal.service.UsuarioService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) //@Order()
public class UsuarioControllerTest {

	@Autowired //injeção de dependencia
	private TestRestTemplate testRestTemplate;
	
	//criando o usuário service
	@Autowired
	private UsuarioService usuarioService;
	
	//criando o usuário repository
	@Autowired
	private UsuarioRepository usuarioRepository; //-> pra poder limpar o iusuario de banco de Testes (h2)
	
	//criando o void start //pra limpar/apagar tds os dados da tabela -> começar com o banco limpo sem nenhum dado 
	@BeforeAll
	void start() {
		usuarioRepository.deleteAll();
	}
	
	//criando o método de Test cadastrarUsuario()
	@Test //executará um teste aqui no spring
	@Order(1) //será o p[rimeiro a ser executado
	@DisplayName("Cadastrar um usuário") //vai configurar uma msg que sweerá exibida ao invés do nome do meu método
	public void deveCriarUsuario() {
		//nao preciso chamar o usuario service, só com o httpEntity ele já faz a requisiçao
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(new Usuario(0L, "Gabriel Henrique", 
				"gabriel_henrique@gmail.com", "12345678","http://fotolegal.jpg" ));//vou passar um novo usuario com as infos dele e passas os campos
	
		ResponseEntity<Usuario> resposta = testRestTemplate
				.exchange("/usuarios/cadastrar", HttpMethod.POST, requisicao, Usuario.class);//exchancge ->> trocar ->. troca passando um valor e recebendo outro
						//p fazer o envio da requisiçao preciso dos 4 parametros:
						//1: a URI -> endereco da endpoint 
						//2: método Http 
						//3: objeto HttpEntity  -> nesse ex objeto requisiçao contento obj da classe usuario 
						//4: o conteudo esperado dentro do corpo da minha resposta -> @ResponseBody será do tipo usuario -> usuario
				assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
				assertEquals(requisicao.getBody().getNome(), resposta.getBody().getNome());//checar se o NOME do usuario enviado na requisicao foi persistido no meu BD -> atraves do met getBody -> acesso aos objs da requisiçao e da resposta
				assertEquals(requisicao.getBody().getUsuario(), resposta.getBody().getUsuario());//checar se o USUARIO do usuario enviado na requisicao foi persistido no meu BD ->atraves do met getNome getUsuario getFoto -> acesso aos parametror
				assertEquals(requisicao.getBody().getFoto(), resposta.getBody().getFoto());//checar se o FOTO do usuario enviado na requisicao foi persistido no meu BD			
	} //equivalente ao que fiz no postman, criar um obj HTTPent receber um objeto da classe usuario e vai ser enviado no corpo da minha requisição@RequestBodyos atributos numa classe usuásio
	
	
	//Mét pra nao criar um usuário em cima de outro já existente e testar o Erro de usuario duplicado não esou testando a persistencia dos dados -> meu obj é testar o erro do usuario duplicado, e não a persistencia, se ela rejeita pela segunda vez
	@Test 
	@Order(2) //segundo a ser executado
	@DisplayName("Não deve permitir duplicação de usuário") //vai configurar uma msg que sweerá exibida ao invés do nome do meu método
	public void naoDeveDuplicarUsuario() {
				
		usuarioService.cadastrarUsuario(new Usuario(0L, "Adriana Mucciolo", 
				"adriana_mucciolo@gmail.com", "12345678","http://fotolegaladriana.jpg" ));//id, nome, usuario, senha, foto
		
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(new Usuario (0L, "Adriana Mucciolo", 
				"adriana_mucciolo@gmail.com", "12345678","http://fotolegaladriana.jpg"));
		
		ResponseEntity<Usuario> resposta = testRestTemplate
				.exchange("/usuarios/cadastrar", HttpMethod.POST, requisicao, Usuario.class);
						 	//1 URI -> "/usuarios/cadastrar"
												//2 qual é o método -> Post 
																//verificar
																			//conteudo q esperamos no corpo da resposta	-> Usuario.class
		assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());//checar se a resp da minha requisiçao é a resposta esperada -> pra obter o status da resposta: .getStatusCode()
		
		
	} 
	
	@Test
	@Order(3) 
	@DisplayName("Alterar um usuário") 
	public void deveAlterarUmUsuario() {
		
	<Usuario> usuarioCreate = usuarioService.cadastrarUsuario(new Usuario(0L,"Joyce", 
				"joyce@gmail.com", "12345678","http://fotolegaladriana.jpg"));
		
		Usuario usuarioUpdate = new Usuario(usuarioCreate.get().getId(),
				"Joyce Meireles", "joyce_meireles@gmail.com", "12345678","http://fotolegaljoyce.jpg");
		
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(usuarioUpdate);	
		
		ResponseEntity<Usuario> resposta = testRestTemplate.withBasicAuth("root", "root")
																	   //(User, password
				.exchange("/usuarios/cadastrar", HttpMethod.PUT, requisicao, Usuario.class);
													  //mét PUT pq quero Atualizar
		
		//req http vai ser enviada atraves do met exchange e a reposnse vai ser recebido pelo obj reposta do tipo respensoEntity
		assertEquals(HttpStatus.OK,resposta.getStatusCode());
		assertEquals(usuarioUpdate.getNome(), resposta.getBody().getNome());
		assertEquals(usuarioUpdate.getUsuario(), resposta.getBody().getUsuario());
		assertEquals(usuarioUpdate.getFoto(), resposta.getBody().getFoto());
	}
	
	@Test
	@Order(4)
	@DisplayName("Listar todos os usuários")
	public void deveMostrarUsuarios() {
		
		usuarioService.cadastrarUsuario(new Usuario(0L,
				"Kevim Lhouis", "kevim@gmail.com", "12345678","http://fotokevim.jpg"));
	
		usuarioService.cadastrarUsuario(new Usuario(1L,
				"Vanessa Jesus", "vanessa@gmail.com", "12345678","http://fotovanessa.jpg"));
	
		ResponseEntity<String> resposta = testRestTemplate
				.withBasicAuth("root","root")
				.exchange("/usuarios/all", HttpMethod.GET, null, String.class);
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
	
	//met de assercao pra checar se a resposta da minha requisiçao é exatamente igual resposta que estou esperando -> expected: <200 OK>
	}
	
}


//@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) ->> escolhe uma porta aleatória pra fazer a conexão caso a porta 8080 esteja ocupada
//@TestInstance(TestInstance.Lifecycle.PER_CLASS) ->> indica q o ciclo de vida do meu teste será por classe
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class) ->> indica em qual ordem os testes serão executados 
//-> executados na ordem da anotação @Order(numMet-1) 

//@Autowired ->> injeção de dependencia
//
//TestRestTemplate ->> vai enviar as requisições pra nossa aplicação
//UsuarioService ->> fazer a persist lá no BD de testes com a senha criptografada
//
//@Test ->> executará um teste aqui no spring
//@Order(1) ->> será o primeiro a ser executado
//@DisplayName("Cadastrar um usuário") ->> vai configurar uma msg que sweerá exibida ao invés do nome do meu método
//testRestTemplate ->> é do próprio JUnit -> vai ser a response da minha requisição -> e vai ser recebido pelo obj ResponseEntity

//assertEquals(HttpStatus.CREATED, resposta.getStatusCode()); ->> mét de assercao assertEquals vai checar se a response é a resposta esperada 
//-> se sim, vai gerar um CREATED 201-> p obter o status da resposta, vai usar o mét .getStatusCode()
//
//ao inves de testar se o usuario for persistido, nós checaremos se ele NAO foi persistido -> badRequest 400 -> test deu certo! que é o teste de erro -> nao deixou duplicar o meu usuario
//precisa de uma preparaçao do ambiente, ou seja, 
//preciso ter um usuario já cadastrado pra fazer o teste de duplicaçao de Usuario!!!!!!!!


//pra ser aprovado, os 3 testes no proprio método usuario, devem ser aprovados caso contrario JUnit vai indicar que falhou

