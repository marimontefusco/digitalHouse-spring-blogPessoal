package com.digitalhouse.blogpessoal.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digitalhouse.blogpessoal.model.Usuario;
import com.digitalhouse.blogpessoal.model.UsuarioLogin;
import com.digitalhouse.blogpessoal.repository.UsuarioRepository;
import com.digitalhouse.blogpessoal.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UsuarioController {

	@Autowired
	private UsuarioRepository repository;
	
	@Autowired 
	private UsuarioService usuarioService;
		//Injetando a camada de Serviço
	
	@GetMapping("/all")
	public ResponseEntity <List<Usuario>> getAll() {
		return ResponseEntity.ok(repository.findAll());
	}
	
	@GetMapping("/{id")
	public ResponseEntity<Usuario> getById(@PathVariable Long id) {
		return repository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.notFound().build());
	}
	
	@PostMapping("/logar")
	public ResponseEntity<UsuarioLogin> Authetication(@RequestBody Optional<UsuarioLogin> user) {
		return usuarioService.logar(user).map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
	}
	
	@PostMapping("/cadastrar")
	public ResponseEntity<Optional<Usuario>> Post(@RequestBody Usuario usuario) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(usuarioService.cadastrarUsuario(usuario));
	}
	
	@PutMapping("/atualizar")
	public ResponseEntity<Usuario> putUsuario(@Valid @RequestBody Usuario usuario) {
		return usuarioService.atualizarUsuario(usuario)
			.map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
			.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
}

//@PostMapping("/logar")
//public ResponseEntity<UsuarioLogin> Authetication(@RequestBody Optional<UsuarioLogin> user) {
//	return usuarioService.logar(user).map(resp -> ResponseEntity.ok(resp))
//			.orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
//}


//@PostMapping("/cadastrar")
//public ResponseEntity<Usuario> Post(@RequestBody Usuario usuario) {
//	return ResponseEntity.status(HttpStatus.CREATED)
//			.body(usuarioService.cadastrarUsuario(usuario));
//}
