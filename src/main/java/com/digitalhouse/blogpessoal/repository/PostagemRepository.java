package com.digitalhouse.blogpessoal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.digitalhouse.blogpessoal.model.PostagemModel;

@Repository 
public interface PostagemRepository extends JpaRepository <PostagemModel, Long> {

	//Consultando pelo título -> Buscar todas as Postagens pelo título
	public List<PostagemModel>findAllByTituloContainingIgnoreCase(String Titulo);
		//titulo -> nome do meu atributo (private String titulo) lá da @Entidade PostagemModel
		//(String Titulo) é o parametro que ele vai retornar
	
}



//@Repository -> Manipulações dentro do nosso BD -> Criando Consultas Personalizadas dando um padrão para a consulta
//Containing é == o LIKE do My sql
//Ignorecase -> ignorar letras maiúsculas e minúsculas