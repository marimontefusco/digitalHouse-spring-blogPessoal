package com.digitalhouse.blogpessoal.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.digitalhouse.blogpessoal.model.Usuario;

public class UserDetailsImplementation implements UserDetails {

	private static final long serialVersionUID = 1L; //final =>> é uma constante
	//essa classe é so pro meu controle interno
	//é um atributo pra controlar a compatibilidade entre minha class e a desserializacáo
	//tecnica q permite tranformar o estado de um objeto numa sequencia de bites
	
	
	//Atributos da Classe
	private String userName;
	private String password;
	
	//mét CONSTRUTOR pra poder PUXAR OS ATRIBUTOS userName e password
	//Esse mét vai ter o parametro user do tipo Usuario e já vai popular esse user com um login e uma senha minha
	public UserDetailsImplementation (Usuario user) {
			//inicalizando os atributos que vem lá da UsuarioModel
		this.userName = user.getUsuario();
		this.password = user.getSenha(); //.getSenha() vem lá do meu model usuario
	}
	
	//CONSTRUTOR VAZIO pra ter como referência:
	public UserDetailsImplementation () {	
		
	}
	
	
	//Méts criados a partir do implements UserDetails:
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
		//return null; 
		//está fazendo uma ponte/conexão entre o obj Usuario e o UserDetailsImplementation, ou seja, 
		//criou na model o objetos com os valores (userName e o password) do usuario e faz com que o .getPassword()
		//e o .getUsername() deste UserDetailsImplementation seja o mesmo de lá (model Usuario)
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return userName;
		//return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
		//return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
		//return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
		//return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
		//return false;
	}
}
