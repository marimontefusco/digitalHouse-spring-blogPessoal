package com.digitalhouse.blogpessoal.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class BasicSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UserDetailsService userDetailsService;
		//variavel userDetailsService
	
	
	//AUTENTICAR/VALIDAR se meu usuário é válido ou não
		//agora vou criar um met sobrescrito pra poder pegar o UserServiceDetails e dar como resposta à minha autorização 
		//-> daí vou usar uma tratativa de erro pra ver se ele vai criar ou não essa dependencia 
		//e a partir daí eu vou sobreescrever o que está dentro dele, que é a minha configuração:
	
	@Override	
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {//vou criar uma autenticação Manager Buider e vou dar um valor pra ela pra poder puxar essa autenticaçao
		auth.userDetailsService(userDetailsService);
			//vai ser o Servico de Configuracao do meu Usuário
	}
	
	
	@Bean	//diz pro Spring q quer criar esse objeto e deixar ele disponível pra outras classes utilizarem ele como dependência
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
			//vou retornar uma nova senha criptografada (para o meu password), 
			//pq ele vai servir pra criptografar uma senha/codificação 
			//e as correspondencias da senha, correspondencias do serviço
	}
	
	
	//AUTORIZAR -> já Autenticou: SE meu Usuário tá cadastrado, próximo passo é Autorizar
		//assim q esse met for instanciado, ele vai retornar um objeto http security, 
		//ou seja, uma segurança pro meu Http -> ai vai chamar o http.authorizaçao:
	
	@Override	//agora vou sobrescrever um outro mét pra poder fazer a validaçao do meu HTTP Security
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests() //essa configuracao serve pra liberar os endpoints e alguns caminhos dentro do meu controller para q meu client tenha acesso sem precisar passar uma chave do tipo token 
			//vai autorizar que meu client possa acessar minha BD sem ter que ter um TOKEN pra poder fazer esse tipo de situação
		.antMatchers("/usuarios/login").permitAll()
		.antMatchers("/usuarios/cadastrar").permitAll()
		.anyRequest().authenticated()//demais requisições terão q ser autenticadas -> teremos q passar a chave pra ela senão ão vou permitir (se tiver get post put tem q autenticar)
		.and().httpBasic()//Basic -> é o padrão universal pra gerar essa chave token -> chave padrao do proprio sistema
		.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //criando um management de sessão -> indica qual o tipo de sessão que nós vamos utilizar: utilizamos uma Política do tipo STATELESS. 
																						  //como estamos falando de uma API, uma das finalidades é q ela seja STATELESS ->> que não guarda sessão, logo, vamos deixar implícito q nossa IDE não vai guardar nenhuma sessao dentro dela 
		.and().cors()//habilitar o cors -> Cross Origin Resource Sharing
		.and().csrf().disable();
	}
	
}



//@EnableWebSecurity ->> habilitar as configurações da web security, tdas q tiver necessidade
//@Bean ->> diz q quer criar esse objeto e deixar ele disponível pra outras classes utilizarem ele como dependência

//throws Exception -> tratativa de erro caso o usuário nao for válido

//Criptografia == Codificação
//qndo eu registro um usuario -> senha fica com valor do tipo Hash (Codificada) -> qndo insiro minha senha -> ele vai armazenar no BD a senha com codificação
//Correspondencias de senha ->. estágio de verificação da senha ->> qndo o usuário efetua o login e a senha não descriptografa pq é é do tipo HASH,
//Ou seja, uma senha criptografada não descriptografa, é irreversível, mas ele vai usar o mesmo algoritmo q o hash utilizou pra codificar a senha
//e comparar com a senha criptografada q está no BD.

//configure()-> é um polimorfismo, por isso ele pode ter o mesmo nome-> faz tratamentos diferentes (autenticaao ou autorização, dependendo do parametro q ele recebe)

//.antMatchers() ->> serve para todos os usuários que estão logados

//a nd.cors da W3C->> autorizar o cors -> cross origin resource -> autorização/especificação do W3C q qndo implementado pelo navegador, permite q meu site acesse recursos de outro site mesmo estando em dominios diferentes
//same origin police -> um recurso de um site so pode ser chamado por outro site se os dois sites estiverem sob o mesmo dominio -> mesmo endereco

//.csrf() é o tipo de ataque fraudulento pra ter privilegios e acessos de um usuario valido pra poder utilizar de alguns recursos
///é o ataque mais antigo da web -> o navegador j's habilita através dos cookies, se desablitr
//-> é um dos ataques mais da requisicao http entre sites na tentativa de usuarios se passar por um usuario legitimo do seu sitema
//usuarios q tem foco em ataque e que estejam autenticado no site entre a fraudulento pra ter mais acesso a daquele site
//a razao sdesse problema é como os navegadores usam os cookies 0--> é a manioulacao dos cookies q faz com q os osrf possam dar problemas
//por isso preciso desabilitar 