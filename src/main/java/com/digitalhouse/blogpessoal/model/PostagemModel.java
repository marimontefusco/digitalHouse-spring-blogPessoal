package com.digitalhouse.blogpessoal.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="tb_postagem")
public class PostagemModel {

	//Atributos da classe postagem & Annotations dos meus atributos:
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotNull
	@Size(min=5,max=10)
	private String titulo;
	
	@NotNull
	@Size(min=10,max=500)
	private String texto;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date date = new java.sql.Date(System.currentTimeMillis());

	@ManyToOne
	@JsonIgnoreProperties("postagem") //ignorar a recursividade -> decorrencia de varios grupos de repetição 
	private TemaModel tema; //função definida pra ela mesmo -> base pra linguagens funcionais -> repete pra ela mesma q tem uma relacao blabla
	
	//Getters & Setters
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public TemaModel getTema() {
		return tema;
	}

	public void setTema(TemaModel tema) {
		this.tema = tema;
	}
	
}



//@Entity -> Indica q essa minha classe sera uma Entidade da minha JPA Hibernate
//@Table(name = "postagem") -> Indica q essa minha entidade(q é minha classe), dentro do meu BD vai criar uma tabela e o nome será postagem
//@Id -> Indica q ele vai ser minha chave primária la da minha tabela Postagem
//@GeneratedValue(strategy = GenerationType.IDENTITY) ->. valor gerado e a estrátegia será q eu vou criar o numerable do tipo identidade 
	//ou seja, vai ser chave primaria e nao pode ter duplicidade
//@Temporal(TemporalType.TIMESTAMP) -> indica q meu atributo é temporal de tempo, e qual tipo de tempo quero nesse  
	//ou seja -> é o formato de data e hora do sistema

//private Date date = new java.sql.Date(System.currentTimeMillis()); -> data do sistema -> pra poder mostrar a data da postagem
//assim q eu passar algum dado por ess meu atributo da minha classe
//ele vai capturar exatamente a data, a hora, o minuto q ele passou pela minha pg
