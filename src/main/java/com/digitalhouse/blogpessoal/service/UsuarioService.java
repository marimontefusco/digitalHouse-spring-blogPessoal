package com.digitalhouse.blogpessoal.service;

import java.util.Optional;

import java.nio.charset.Charset;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.digitalhouse.blogpessoal.model.Usuario;
import com.digitalhouse.blogpessoal.model.UsuarioLogin;
import com.digitalhouse.blogpessoal.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository repository;
	
	//Mét cadastrar -> vai receber um usuário e vai devolver e salvar no UsuarioRepository com a senha já criptografada
	public Usuario cadastrarUsuario(Usuario usuario) {
		
		//instanciando a classe/obj encoder
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		//Criando variável que chama senhaEncoder q vai receber o q estiver dentro
		//do nosso usuário e vai guardar dentro dela
		String senhaEncoder = encoder.encode(usuario.getSenha());
		
		usuario.setSenha(senhaEncoder);	
		
		return repository.save(usuario);//senha q vou retornar criptografada e salvar já criptografada lá no repository
	}
	
	
	//Mét Logar usuario no sistema   -> vai ser utilizado com o UsuarioLogin, pq quero puxar o nome, usuario, token e login 
	public Optional<UsuarioLogin> Logar(Optional<UsuarioLogin> user) {
		
		//instanciando a classe/obj encoder
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		//Criando um OBJ do tipo usuário q vai receber o que fizemos lá no usuario através do .findByUsuario e
		Optional<Usuario> usuario = repository.findByUsuario(user.get().getUsuario());
	
		//Verificar se existe algo/tem algo presente dentro do usuário pra poder comparar a Senha q tenho CADASTRADA com a senha q o usuario DIGITOU pra logar
		if (usuario.isPresent()) {
			
			//vai pegar as duas senhas e verificar se são Senhas IGUAIS -> .matches()			 
			if (encoder.matches(user.get().getSenha(), usuario.get().getSenha())) {
					 //.matches(senha Login ususario passou, senha Cadastrada)
				
				//se true -> aplicar a regra de negócio de senha encriptografada e fazer a autorização
				//auth -> variável criada pra guardar o usuario e a senha do tipo String
				String auth = user.get().getUsuario() + ": " + user.get().getSenha();
				
				//Array byte q vai pegar a Base64 e vou passar qual formato de byte q eu quero ->. neste caso US-ASCII
				byte[] encodeAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
			
				//conversão p tipo String
				String authHeader = "Basic" + new String(encodeAuth);
				
				//pegando meu Optional<UsuarioLogin> user pra poder SETAR o meu token
				user.get().setToken(authHeader);
				user.get().setNome(usuario.get().getNome());
				
				return user;
				
			}
			
		}
		
		return null;
	
	}
	
}



//user e usuario -> não são do tipo classe Usuario e classe UsuarioLogin, são do tipo Optional então vamos trabalhar em cima 
//de Optional e não em direto cima da classe Usuario ou UsuarioLogin, por isso não conseguimos fazer o .getSenha() direto:

//.get() -> pega op objeto inteiro, pra daí pegar a propriedade que quero
//.getSenha -> pega a senha depois q usou o mét .get() pra entrar no Optional<Usuario>

//Optional<UsuarioLogin> user -> é do tipo Optional então pra pegar as coisas dentro dele preciso do .get().getSenha()





