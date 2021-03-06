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

import com.digitalhouse.blogpessoal.model.TemaModel;
import com.digitalhouse.blogpessoal.repository.TemaRepository;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/tema")//vai acessar o controller e o endpoint sera acessado através do meu "/tema" pq /tema é meu endpoint
public class TemaController {
	
	@Autowired
	private TemaRepository repository;
	
	@GetMapping //indica que esse é um metodo de get, e enqdo chamar um get lá, ele vai chamar esse método aqui
	public ResponseEntity<List<TemaModel>> getAll() 
	{
		return ResponseEntity.ok(repository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<TemaModel> getById(@PathVariable long id) {
		return repository.findById(id)
				.map(resp -> ResponseEntity.ok(resp))
				.orElse(ResponseEntity.notFound().build());
	}
	
	@GetMapping("/nome/{nome}")
	public ResponseEntity<List<TemaModel>> getByName(@PathVariable String name) {
		return ResponseEntity.ok(repository.findAllByDescricaoContainingIgnoreCase(name));
	}
	
	@PostMapping
	public ResponseEntity<TemaModel> post (@RequestBody TemaModel tema) {
		return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(tema));
	}
	
	
	@PutMapping
	public ResponseEntity<TemaModel> put (@RequestBody TemaModel tema) {
		return ResponseEntity.ok(repository.save(tema));
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable long id) {
		repository.deleteById(id);
	}
	
	//no CONSTRUTOR -> legal utilizar qndo tiver mais repositories -> contrutor
	
	
}



//allowedHeaders = "*" -> indica quais oa cabelhos HTTP q posso estar utilizando durante a requisição efetiva
//mapear a partir do Lambda Expression e fazer uma validação, mapear a resposta -> se corretta beleza, orElse -> senao

//lambda expression -> resp