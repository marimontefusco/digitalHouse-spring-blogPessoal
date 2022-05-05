package com.digitalhouse.blogpessoal.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.digitalhouse.blogpessoal.model.Usuario;
import com.digitalhouse.blogpessoal.repository.UsuarioRepository;

@Service
public class UserDetailsServiceImplementation implements UserDetailsService {

	//Criando o Objeto userRepository pra poder injetar 
	@Autowired
	private UsuarioRepository userRepository;
	
	//Mét que vamos usar como uma sobrescrita de outro mét caso tenha erro ele vai disparar o Exception UsernameNotFoundException
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		
		//Criando um OBJ do tipo Optional chamado user que vai entrar no userRepository e através do método .findByUsuario() 
		//e vai nos devolver um username, que poderá ser acessavo com a palavra user -> user.metodoQueQuero()
		Optional <Usuario> user = userRepository.findByUsuario(username);
		
		//caso nao ache o nome do usuário, retorno uma Exception de erro -> NomeUsuarionot found
		//user.metodoQueQuero() com função lambda
		user.orElseThrow(() -> new UsernameNotFoundException(username + "not found"));
		return user.map(UserDetailsImplementation :: new).get();
		//user.map -> vou mapear o UserDetailsImplementation
		//::new -> e criar um novo UserDetailsImplementation
		//.get() -> e vou extrair/buscar o que tenho dentro desse objeto
		//return -> vou retornar um user mapeado dentro do UserDetailsImplementation e ele vai nos entregar um novo 
		//UserDetailsImplementation, e como ele é um Optional, uso o get() para pegar/extrair o que tenho dentro do meu objeto
	
	}

}
