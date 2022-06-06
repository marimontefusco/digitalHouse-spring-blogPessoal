package com.digitalhouse.blogpessoal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digitalhouse.blogpessoal.model.PostagemModel;
import com.digitalhouse.blogpessoal.repository.PostagemRepository;

@RestController
@RequestMapping("/postagens")
@CrossOrigin("*")
public class PostagemController {

	@Autowired
	private PostagemRepository repository;
	
	//Método GET 
	@GetMapping
	public ResponseEntity<List<PostagemModel>>GetAll()
	{
		return ResponseEntity.ok(repository.findAll());
	}
	
	@GetMapping("/{id}") //busco por id -> indicar qual met http pra poder chamar o id
	public ResponseEntity<PostagemModel> GetById(@PathVariable long id){
		return repository.findById(id)
				.map(resp -> ResponseEntity.ok(resp))
				.orElse(ResponseEntity.notFound().build());
	}
	
	@GetMapping("/titulo/{titulo}") 
	public ResponseEntity<List<PostagemModel>> GetByTitulo(@PathVariable String titulo) {
		return ResponseEntity.ok(repository.findAllByTituloContainingIgnoreCase(titulo));
	}
	

	@PostMapping
	public ResponseEntity<PostagemModel> post (@RequestBody PostagemModel postagem){
		return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(postagem));
	}
	
	@PutMapping
	public ResponseEntity<PostagemModel> put (@RequestBody PostagemModel postagem){
		return ResponseEntity.status(HttpStatus.OK).body(repository.save(postagem));
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable long id) {
		repository.deleteById(id);
	}
	
}



//@Annotations  -> vem para modificar alguma situação que tenho lá na minha classe
//Vou dar uma ação/contrato diferente pra ela, para poder gerar um resultado diferente para mim

//@RestController -> informa que essa Classe é uma Controller -> Controla todos os métodos de comunicação com o BD 
//Controler é um meio de campo, fica gerenciando tanto o Spring quanto o BD -> receber e dar devolutiva
//Controler -> Gerar todos os métodos para fazer alterações no meu banco de dados: FindAll, FindById...
//e faz o controle do fluxo do nosso sistema e passa pra frente. Depois recebe o resultado do FindAll, FindById 
//e vai retornar lá pra quem fez a requisição

//No Controller vamos configurar quais os ENDPOINTS que vamos expor na nossa aplicação para ser consumida e daí nós vamos 
//configurar o que cada Endpoint desse irá fazer (ex: /postagens -> retorna todas as postagens)

//@RequestMapping -> Indica em qual URI que essa Classe será acessada (parametro = "/postagens")
//parametro ("/postagens") -> a partir do momento que vier alguma REQUISIÇÃO dentro da /postagens, essa requisição vai passar a consumir minha Classe
//Ou seja, o @RequestaMapping -> aceitar as requisições e saber de onde a requisição está vindo

//@CrossOrigin(*) -> aceita as requisições de Qualquer Origem

//PostagemRepository é uma Interface e não pode ser instanciada -> teremos que fazer a Inversão de Controle pro Spring
//através da @Autowired


//@Autowired -> é uma injeção de dependências do Spring -> vai garantir que todos os serviços 
//dessa interface sejam acessados a partir da minha camada @Controller

//@GetMapping -> sempre q chegar uma Requisição Externa E for um met Get(), ele vai disparar essa requisição:
//public ResponseEntity<List<PostagemModel>>GetAll() {
	//return ResponseEntity.ok(repository.findAll());}	
//vou retornar uma Lista do tipo postagem e o nome do meu metodo vai ser getAll(), 
	//e nao receberemos nada como parametro pq eu vou retornar alguma coisa 
	//no retorno do método vou obter um objeto do tipo ResponseEntity

//Selecionar tudo o que tiver 
//Vai na minha Classe PostagemController e vou retornar uma Lista, e essa lista vai ser do tipo postagem POST
//e o nome do meu método vai ser getAll( e não receberemos nada como parâmetro)

//ResponseEntity -> 
//ok(parametro) -> ok indica que preciso da resposta do HTTP para poder validar
//(parametro) -> estamos fazendo a requisicao desse paramtro -> todas as postagens


//@PathVariable -> esta determinando q vou uma variavel q estamos querendo dentro da path vari e depois vamos retornar q injetamos uma autorizaçao pra fazer essa 




