package com.digitalhouse.blogpessoal.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.digitalhouse.blogpessoal.model.Usuario;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) //-> é uma classe spring bootTeste 
@TestInstance(TestInstance.Lifecycle.PER_CLASS)//indica ciclo de vida da sua classe, será por classe
public class UsuarioRepositoryTest {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@BeforeAll //o mét start vai apagar os dados da minha tabela 
	void start() {
		usuarioRepository.deleteAll(); //vai apagar tds os dados da tabela e depois vai inserir as infos e cadastrar através do mét salvar() pra pdoer garar minha saída
		usuarioRepository.save(new Usuario(0L, "Zé Colméia", "roba_lanche@email.com", "123567890", "httpps://image1.com/fotoze.jpg"));
		usuarioRepository.save(new Usuario(0L, "Catatau", "catatauau@email.com", "0987654321", "httpps://image1.com/catataufoto.jpg"));
		usuarioRepository.save(new Usuario(0L, "Barnabé", "barnabe@email.com", "890123567", "httpps://image1.com/minhafoto.jpg"));
		usuarioRepository.save(new Usuario(0L, "Manda chuva", "manda-chuva@email.com", "1235679999", "httpps://image1.com/minhalindafoto.jpg"));
	}
	
	//Método pra retornar o usuario
	@Test //referente ao JUnit -> indica q executará um teste
	@DisplayName("Retorna 1 usuário") //msg que vai ser exibida ao invés do nome do método -> como se fosse um apelido
	public void deveRetornarUMUsuario() { //indicar o q quero retornar executando este teste:
		Optional<Usuario> usuario = usuarioRepository.findByUsuario("catatauau@email.com");
		//vai receber o resultado do método findByUsuario
		
		assertTrue(usuario.get().getUsuario().equals("catatauau@email.com")); //é um assertivo
		//vai verificar se meu usuario cujo email é "catatauau@email.com" for encontrado, se sim o reultado sera APROVADO, caso nao encontre, o resultado vai ser FALOU
	}
	
	@Test //referente ao JUnit -> indica q executará um teste
	@DisplayName("Retorna 3 usuárioS") //msg que vai ser exibida ao invés do nome do método -> como se fosse um apelido
	public void deveRetornarTresUsuario() {
		List<Usuario> listaDeUsuarios = usuarioRepository.findAllByNomeContainingIgnoreCase("Zé Colméia");
		
		assertEquals(3,listaDeUsuarios.size());//atraves desse mét vai retornar o tamanho da list, se é ==3 e vai verificar se o sobrenome é colmeia
		assertTrue(listaDeUsuarios.get(0).getNome().equals("Zé Colméia"));
		assertTrue(listaDeUsuarios.get(1).getNome().equals("Catatau"));
		assertTrue(listaDeUsuarios.get(2).getNome().equals("Barnabé"));
		// .get(int index) -> mét de posição
		//vai receber o resultado do mét find all  
		//verificar se foram gravados na mesma sequencia e inseridos no meu BD na mesma seuqencia q está inserida ali
	}
	
}

//assertes garantem q os valores retornados sejam aqueles, que os testes funcionem

//webEnvironment = WebEnvironment.RANDOM_PORT -> caso a porta principal :8080 esteja ocupada, o spring vai atribuir uma outra porta automaticamente
//0l -> 0 indica q meu atributi id será preenchido automaticamente pelo BD -- L:  indica q meu atributo é o Long lá da minha classe
//sem o L ele daria problema -> questão de tipagem, tem q mostrar q é um 0Long