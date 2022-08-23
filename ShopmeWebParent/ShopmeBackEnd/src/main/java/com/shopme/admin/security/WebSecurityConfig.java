package com.shopme.admin.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new ShopmeUserDetailsService();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		
		//http.authorizeRequests().anyRequest().permitAll();
		
		http.authorizeRequests()
		.antMatchers("/users/**", "/settings/**", "/countries/**", "/states/**").hasAuthority("Admin")
		.antMatchers("/categories/**","/brands/**","/menus/**", "/articles/**").hasAnyAuthority("Admin", "Editor")
		.antMatchers("/products/new", "/products/delete/**").hasAnyAuthority("Admin", "Editor")
		.antMatchers("/products/edit/**", "/products/save", "/products/check_unique")
			.hasAnyAuthority("Admin", "Editor", "Salesperson")
		.antMatchers("/products", "/products/", "/products/detail/**", "/products/page/**")
			.hasAnyAuthority("Admin", "Editor", "Salesperson", "Shipper")
		.antMatchers("/products/**","/articles/**", "/menus/**", "/sections/**").hasAnyAuthority("Admin", "Editor")
		.antMatchers("/customers/**","/shipping/**","/articles/**", "/get_shipping_cost", "/reports/**").hasAnyAuthority("Admin", "Salesperson")
		.antMatchers("/orders", "/orders/", "/orders/page/**", "/orders/detail/**").hasAnyAuthority("Admin", "Salesperson", "Shipper")
		.antMatchers("/states/list_by_country/**").hasAnyAuthority("Admin", "Salesperson")
		.antMatchers("/orders_shipper/update/**").hasAuthority("Shipper")
		.antMatchers("/products/detail/**", "/customers/detail/**").hasAnyAuthority("Admin", "Editor", "Salesperson", "Assistant")
		.antMatchers("/reviews/**", "/questions/**").hasAnyAuthority("Admin", "Assistant")
		.anyRequest().authenticated()
		.and()
		.formLogin()			
			.loginPage("/login")
			.usernameParameter("email")
			.permitAll()
		.and()
		.logout().permitAll()
		.and()
		.rememberMe()
			.key("AbcDefgKLDSLmvop_0123456789")
			.tokenValiditySeconds(7 * 24 * 60 * 60); // 7 days 24 hours 60 minutes 60 seconds -> 7days 
			;
			
			
		http.headers().frameOptions().sameOrigin();	
		
	}
	
	// Before authenticated, all matchers can be ignored and all these are performed.
	// Like -> showing image in /login , all images,js and web jars files are detected  
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**" , "/css/**");
	}
	
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}
	
}