package com.example.demo.security;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Example;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author bing
 * 自定义安全配置，跳过安全自动配置
 * 拓展了WebSecurityConfigurerAdapter类的两个configure()方法
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private Logger logger = org.slf4j.LoggerFactory.getLogger(SecurityConfig.class);
	
	@Autowired
	private ReaderRepository readerRepository;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		logger.info("configure(HttpSecurity http)");
		http.csrf().disable();
		http.authorizeRequests().antMatchers("/").access("hasRole('READER')") //要求登录者有READER角色
				.antMatchers("/**").permitAll().and().formLogin().loginPage("/login") //设置登录表单的路径
				.failureUrl("/login?error=true");
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		logger.info("configure(AuthenticationManagerBuilder auth)");
		auth.userDetailsService(new UserDetailsService() { //匿名内部类，定义自定义UserDetailsServices
	
			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				logger.info("loadUserByUsername(String username)");
				Reader reader = new Reader();
				reader.setUsername(username);
				Example<Reader> readerExample = Example.of(reader);
				return readerRepository.findOne(readerExample).orElse(null);
			}
		});
		
	}
}