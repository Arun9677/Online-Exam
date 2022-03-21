package Exam.Online.Exam;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecuirtyConfig extends WebSecurityConfigurerAdapter
{
	@Autowired
	DataSource dataSource;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception
	{
		auth.jdbcAuthentication().dataSource(dataSource).
		passwordEncoder(new BCryptPasswordEncoder()).
		usersByUsernameQuery("SELECT username, password, enable FROM user WHERE username = ? ").
		authoritiesByUsernameQuery("SELECT username, role FROM user WHERE username = ? ")
		;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		http.authorizeRequests()
			.antMatchers("/", "/signUp", "/mailVerify", "/verify", "/nullUser").permitAll()
			.antMatchers("/edit/*", "/delete/*").hasRole("FACULTY")
			.anyRequest().authenticated()
			.and()
			.httpBasic()
			.and()
			.formLogin().permitAll()
			.and()
			.logout().logoutSuccessUrl("/").permitAll()
			.and()
			.exceptionHandling().accessDeniedPage("/403")
			;			
			
	}
	
}
