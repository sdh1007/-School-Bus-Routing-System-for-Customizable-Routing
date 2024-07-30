package sru.edu.SchoolRouteMgt.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import sru.edu.SchoolRouteMgt.services.CustomUserDetailsService;

/*Designed by Group 3 - Bonus Project
 * CPSC-488-01
 * Main Author: Zack Freilano
 * This class handles security and customizes the security configurer provided by Maven.
 * */

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

		@Autowired
		private DataSource dataSource;
		// used to talk to database
		 
		@Bean
		public UserDetailsService userDetailsService() {
			return new CustomUserDetailsService();
		}
		
		@Bean
		public BCryptPasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		}
		
		@Bean
		public DaoAuthenticationProvider authenticationProvider() {
			// sets properties of the Dao Authentication Provider
			
			DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
			authProvider.setUserDetailsService(userDetailsService());
			authProvider.setPasswordEncoder(passwordEncoder());
			
			return authProvider;
		}
		
		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			// configures the Authentication provider as the authentication provider
			auth.authenticationProvider(authenticationProvider());
		}
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			
			// Configure http security and authentication
			
			http
				.authorizeRequests()
					.antMatchers("/", "/requestAccount", "/login", "/error").permitAll()
					.antMatchers("/css/**", "/js/**", "/images/**").permitAll()
					.anyRequest().authenticated()
					.and()
				.formLogin()
					.usernameParameter("email")
					.defaultSuccessUrl("/index")
					.permitAll()
					.and()
				.logout()
					.logoutSuccessUrl("/").permitAll();
		}
}

