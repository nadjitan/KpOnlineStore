package com.tunnelnetwork.KpOnlineStore;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
// import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

@Configuration
@EnableWebSecurity 
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
	public InMemoryUserDetailsManager getInMemoryUserDetailsManager() {
    return new InMemoryUserDetailsManager();
	}

  // @Override
  // protected void configure(AuthenticationManagerBuilder auth) throws Exception {
  //     auth.inMemoryAuthentication()
  //       .withUser("admin").password(passwordEncoder().encode("admin")).authorities("ADMIN").and()
  //       .withUser("test").password(passwordEncoder().encode("123")).authorities("USER");         
  // }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .authorizeRequests()
        .antMatchers(
          "/crud/**",
          "/contact-us/**",
          "/about-us/**",
          "/store/**",
          "/profile/**",
          "/cart/**",
          "/checkout/**",
          "/products/**",
          "/js/**",
          "/css/**",
          "/img/**").permitAll()
        .antMatchers(HttpMethod.POST, "/signup").permitAll()
        .antMatchers(HttpMethod.POST, "/addproduct").permitAll()
        .antMatchers(HttpMethod.POST, "/removeProduct").permitAll()
        .antMatchers(HttpMethod.GET, "/signup").permitAll()
        .anyRequest().authenticated()
        .and()
        .formLogin()
          .loginPage("/login").permitAll()
          .and()
          .logout().permitAll();
    }
}
