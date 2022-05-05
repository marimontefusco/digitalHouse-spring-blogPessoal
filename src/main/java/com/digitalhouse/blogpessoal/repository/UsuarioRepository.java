package com.digitalhouse.blogpessoal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.digitalhouse.blogpessoal.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository <Usuario, Long> {
	
	//Método pra pegar meu usuario pelo nome/username
	public Optional<Usuario>findByUsuario(String usuario);

	//Método pra pegar por nome -> Lista
	public List<Usuario>findAllByNomeContainingIgnoreCase(String nome);
	
}



//Repository -> Faz toda a comunicação da API Rest com nossa base de dados
//Primeiro cria uma HERANÇA entre essa classe UsuarioRepository com a JpaRepository

//Optional é uma collection -> recurso do Java pra RETORNAR algum VALOR pra nós, 
//inclusive pode retornar um NULL, caso ele não ache o objeto Usuario

//ele é um optional, pode achar ou não um objeto do tipo usuario. é bastante utilizado

//<Usuario, Long>como parametro, vai sempre vir com L maiusculo, se for com tipo de dado, ele vem com l minusculo

//Outros métodos que podemos/vamos fazer:
//criar um List de Usuario,
//um findAllByNAme
//Ignoring Case

//Criar outro Optional com Usuario and Senha -> pra testar os dois ao mesmo tempo
//e passar lá dois parametros String usuario e String senha