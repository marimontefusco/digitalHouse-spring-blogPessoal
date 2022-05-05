package com.digitalhouse.blogpessoal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.digitalhouse.blogpessoal.model.TemaModel;

@Repository
public interface TemaRepository extends JpaRepository<TemaModel, Long> {

	//criar uma lista e fazer por descrição:(métodos personalizados)
	//pela lista de tema, sera publica, vai tretornar umal=ista de tema com o nome list e  por descricao
	//public List<@Entidade> findAllNOMEPARAMETROContainingIgnoreCase(
	public List<TemaModel> findAllByDescricaoContainingIgnoreCase(String descricao);
	
}
