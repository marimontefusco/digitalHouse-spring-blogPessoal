package com.digitalhouse.blogpessoal.service;

import java.util.Optional;

import java.nio.charset.Charset;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.digitalhouse.blogpessoal.model.Usuario;
import com.digitalhouse.blogpessoal.model.UsuarioLogin;
import com.digitalhouse.blogpessoal.repository.UsuarioRepository;

/**
 *  A Classe UsuarioService implementa as regras de negócio do Recurso Usuario.
 *  
 *  Regras de negócio são as particularidades das funcionalidades a serem 
 *  implementadas no objeto, tais como:
 *  
 *  1) O Usuário não pode estar duplicado no Banco de dados
 *  2) A senha do Usuario deve ser criptografada
 *  
 *  Observe que toda a implementação dos metodos Cadastrar, Atualizar e 
 *  Logar estão implmentadas na classe de serviço, enquanto a Classe
 *  Controller se limitará a checar a resposta da requisição.
 */

 /**
 * A Anotação @Service indica que esta é uma Classe de Serviço, ou seja,
 * implementa todas regras de negócio do Recurso Usuário.
 */

@Service
public class UsuarioService {
	
	//ROTINA DE SEGURANÇA pra poder autorizar o nosso usuário que tentar logar

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	// MÉTODO cadastrarUsuario()
	public Optional<Usuario> cadastrarUsuario(Usuario usuario) {
		
		if (usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent()) {
			return Optional.empty();
		}
		
		usuario.setSenha(criptografarSenha(usuario.getSenha()));
		
		return Optional.of((usuarioRepository.save(usuario)));
	
	}	

			/*
		 	 *  Cadastrar Usuário
		 	 * 
		 	 *  Primeiro checa se o usuário já existe no Banco de Dados através do mét findByUsuario(), 
		 	 *  pq não pode existir 2 usuários com o mesmo email. 
		 	 *
		 	 *  Se não existir retorna um Optional vazio.
		 	 *  
		 	 *  Se o Usuário não existir no Banco de Dados, a senha será criptografada
		 	 * 	através do Método criptografarSenha.
		 	 *  
		 	 *  isPresent() -> Se um valor estiver presente retorna true, caso contrário
		 	 *  retorna false.
		 	 * 
		 	 *  empty -> Retorna uma instância de Optional vazia.
		 	 *  
		 	 *  Assim como na Expressão Lambda, o resultado do método save será retornado dentro
		 	 * de um Optional, com o Usuario persistido no Banco de Dados.
		 	 * 
		 	 * of​ -> Retorna um Optional com o valor fornecido, mas o valor não pode ser nulo. 
		 	 * Se não tiver certeza de que o valor não é nulo use ofNullable.
		 	 */
		
		
	
	// MÉTODO atualizarUsuario()
	public Optional<Usuario> atualizarUsuario(Usuario usuario) {
		
		if (usuarioRepository.findById(usuario.getId()).isPresent()) {
			
			//Criando um objeto do tipo Optional com o resultado do método findById
			Optional<Usuario> buscaUsuario = usuarioRepository.findByUsuario(usuario.getUsuario());
		
			if ( (buscaUsuario.isPresent()) && (buscaUsuario.get().getId() != usuario.getId())) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já existe", null);
				}
			
			
			usuario.setSenha(criptografarSenha(usuario.getSenha()));
			
			return Optional.ofNullable(usuarioRepository.save(usuario));
		}
		
		return Optional.empty();
		// empty -> Retorna uma instância de Optional vazia, caso o usuário não seja encontrado.
		
	}
	
			/*
			 *  Atualizar Usuário
			 * 
			 *  1 - Primeiro checa se o usuário já existe no Banco de Dados através do método findById, 
			 *  porquê não é possíve atualizar 1 usuário inexistente. 
			 *  Se não existir retorna um Optional vazio.
			 *  
			 *  isPresent() -> Se um valor estiver presente retorna true, caso contrário
			 *  retorna false.
			 *  
			 *  2 - Cria um Objeto Optional com o resultado do método findById
			 *  
			 *  3 - Se o Usuário existir no Banco de dados e o Id do Usuário encontrado no Banco for 
			 *  diferente do usuário do Id do Usuário enviado na requisição, a Atualização dos 
			 *  dados do Usuário não pode ser realizada.
			 * 
			 *  4 - Se o Usuário existir no Banco de Dados e o Id for o mesmo, a senha será criptografada
		 	 *  através do Método criptografarSenha.
			 * 
			 *  Assim como na Expressão Lambda, o resultado do método save será retornado dentro
		 	 *  de um Optional, com o Usuario persistido no Banco de Dados ou um Optional vazio,
			 *  caso aconteça algum erro.
			 * 
			 *  ofNullable​ -> Se um valor estiver presente, retorna um Optional com o valor, 
			 * caso contrário, retorna um Optional vazio.
			 * 
			 * 
			 * 
			 * *
			 *  Atualizar Usuário
			 * 
			 *  Checa se o usuário já existe no Banco de Dados através do método findById, 
			 *  porquê não é possíve atualizar 1 usuário inexistente. 
			 *  Se não existir retorna um Optional vazio.
			 *  
			 *  isPresent() -> Se um valor estiver presente retorna true, caso contrário
			 *  retorna false.
			 * 
			 */
		
	
	// MÉTODO logar()
	public Optional<UsuarioLogin> logar(Optional<UsuarioLogin> usuarioLogin) {
		
		Optional<Usuario> usuario = usuarioRepository.findByUsuario(usuarioLogin.get().getUsuario());
		
		if(usuario.isPresent()) {
			
			if(compararSenhas(usuarioLogin.get().getSenha(), usuario.get().getSenha())) {
				
				usuarioLogin.get().setId(usuario.get().getId());
				usuarioLogin.get().setNome(usuario.get().getNome());
				usuarioLogin.get().setFoto(usuario.get().getFoto());
				usuarioLogin.get().setTipo(usuario.get().getTipo());
				usuarioLogin.get().setToken(gerarBasicToken(usuarioLogin.get().getUsuario(), usuarioLogin.get().getSenha()));
				usuarioLogin.get().setSenha(usuario.get().getSenha());
				
				return usuarioLogin;
			}
			
		}
		return Optional.empty();
		
	}
	
	/**
	 *  A principal função do método autenticarUsuario, que é executado no endpoint logar,
	 *  é gerar o token do usuário codificado em Base64. O login prorpiamente dito é executado
	 *  pela BasicSecurityConfig em conjunto com as classes UserDetailsService e Userdetails
	 */
	
	
	// vai receber um usuário e vai devolver e salvar no UsuarioRepository com a senha já criptografada
	
	
//		//Criando variável que chama senhaEncoder q vai receber o q estiver dentro
//		//do nosso usuário e vai guardar dentro dela
//		String senhaEncoder = encoder.encode(usuario.getSenha());
//		
//		usuario.setSenha(senhaEncoder);	
//		
//		return repository.save(usuario);
//		//return repository.save(usuario);//senha q vou retornar criptografada e salvar já criptografada lá no repository



	
	// MÉTODO criptografarSenha()
	private String criptografarSenha(String senha) {
		
		//Primeiro instancia o obj encoder do tipo BCryptPasswordEncoder p/ criptografar a senha do usuário
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		return encoder.encode(senha);
			//o mét encoder retorna a senha criptografada no formato BCrypt.
		
	}

			/**
			 *  Método Criptografar Senhas.
			 *   
			 *  Instancia um objeto da Classe BCryptPasswordEncoder para criptografar
			 *  a senha do usuário.
			 *
			 *  O método encode retorna a senha criptografada no formato BCrypt. Para mais detalhes,
			 *  consulte a documentação do BCryptPasswordEncoder.
			 * 
			 */
	
	
	// MÉTODO compararSenhas()
	private boolean compararSenhas(String senhaDigitada, String senhaBanco) {
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		return encoder.matches(senhaDigitada,senhaBanco);
		
	}
	
		/**
		 *  Método Comparar Senhas.
		 *   
		 *  Checa se a senha enviada, depois de criptografada, é igual a senha
		 *  gravada no Banco de Dados.
		 * 
		 *  Instancia um objeto da Classe BCryptPasswordEncoder para comparar
		 *  a senha do usuário com a senha gravad no Banco de dados.
		 *
		 *  matches -> Verifca se a senha codificada obtida do banco de dados corresponde à 
		 *  senha enviada depois que ela também for codificada. Retorna verdadeiro se as 
		 *  senhas coincidem e falso se não coincidem.  
		 * 
		 */
	
	
	// MÉTODO gerarBasicToken()
	private String gerarBasicToken(String usuario, String senha) {
		
		String token = usuario + ":" + senha;
		
		byte[] tokenBase64 = Base64.encodeBase64(token.getBytes(Charset.forName("US-ASCII")));
		
		return "Basic" + new String (tokenBase64);
		
	}
	
}
	
		/**
		 * Método Gerar Basic Token
		 * 
		 * A primeira linha, monta uma String (token) seguindo o padrão Basic, através 
		 * da concatenação de caracteres que será codificada (Não criptografada) no formato 
		 * Base64, através da Dependência Apache Commons Codec. 
		 * 
		 * Essa String tem o formato padrão: <username>:<password> que não pode ser
		 * alterado
		 *
		 * Na segunda linha, faremos a codificação em Base 64 da String. 
		 * 
		 * Observe que o vetor tokenBase64 é do tipo Byte para receber o 
		 * resultado da codificação, porquê durante o processo é necessário trabalhar
		 * diretamente com os bits (0 e 1) da String
		 * 
		 * Base64.encodeBase64 -> aplica o algoritmo de codificação do Código Decimal para Base64, 
		 * que foi gerado no próximo método. Para mais detalhes, veja Codificação 64 bits na 
		 * Documentação.
		 * 
		 * Charset.forName("US-ASCII") -> Retorna o codigo ASCII (formato Decimal) de cada 
		 * caractere da String. Para mais detalhes, veja a Tabela ASCII na Documentação.
		 *
		 * Na terceira linha, acrescenta a palavra Basic acompanhada de um espaço em branco (Obrigatório),
		 * além de converter o vetor de Bytes novamente em String e concatenar tudo em uma única String.
		 * 
		 * O espaço depois da palavra Basic é obrigatório. Caso não seja inserrido, o Token não
		 * será reconhecido.
		 */
	
	
	
	
	
	
//	//Mét Logar usuario no sistema   -> vai ser utilizado com o UsuarioLogin, pq quero puxar o nome, usuario, token e login 
//	public Optional<UsuarioLogin> Logar(Optional<UsuarioLogin> user) {
//		
//		//instanciando a classe/obj encoder
//		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//		
//		//Criando um OBJ do tipo usuário q vai receber o que fizemos lá no usuario através do .findByUsuario e
//		Optional<Usuario> usuario = repository.findByUsuario(user.get().getUsuario());
//	
//		//Verificar se existe algo/tem algo presente dentro do usuário pra poder comparar a Senha q tenho CADASTRADA com a senha q o usuario DIGITOU pra logar
//		if (usuario.isPresent()) {
//			
//			//vai pegar as duas senhas e verificar se são Senhas IGUAIS -> .matches()			 
//			if (encoder.matches(user.get().getSenha(), usuario.get().getSenha())) {
//					 //.matches(senha Login ususario passou, senha Cadastrada)
//				
//				//se true -> aplicar a regra de negócio de senha encriptografada e fazer a autorização
//				//auth -> variável criada pra guardar o usuario e a senha do tipo String
//				String auth = user.get().getUsuario() + ": " + user.get().getSenha();
//				
//				//Array byte q vai pegar a Base64 e vou passar qual formato de byte q eu quero ->. neste caso US-ASCII
//				byte[] encodeAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
//			
//				//conversão p tipo String
//				String authHeader = "Basic" + new String(encodeAuth);
//				
//				//pegando meu Optional<UsuarioLogin> user pra poder SETAR o meu token
//				user.get().setToken(authHeader);
//				user.get().setNome(usuario.get().getNome());
//				
//				return user;
//				
//			}
//			
//		}
//		
//		return null;
//	
//	}
	




//user e usuario -> não são do tipo classe Usuario e classe UsuarioLogin, são do tipo Optional então vamos trabalhar em cima 
//de Optional e não em direto cima da classe Usuario ou UsuarioLogin, por isso não conseguimos fazer o .getSenha() direto:

//.get() -> pega op objeto inteiro, pra daí pegar a propriedade que quero
//.getSenha -> pega a senha depois q usou o mét .get() pra entrar no Optional<Usuario>

//Optional<UsuarioLogin> user -> é do tipo Optional então pra pegar as coisas dentro dele preciso do .get().getSenha()

