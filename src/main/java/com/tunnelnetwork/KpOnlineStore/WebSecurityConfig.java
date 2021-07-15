package com.tunnelnetwork.KpOnlineStore;

import com.tunnelnetwork.KpOnlineStore.User.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

  private final UserService userService;

  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService)
				.passwordEncoder(bCryptPasswordEncoder);
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
          "/",
          "/product/**",
          "/fonts/**",
          "/removeFromWishlist/**",
          "/addToWishlist/**",
          "/forgot-password/**",
          "/change-password/**",
          "/sign-up/**",
          "/crud/**",
          "/faq/**",
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
        .antMatchers(HttpMethod.POST, "/addproduct").permitAll()
        .antMatchers(HttpMethod.POST, "/removeProduct").permitAll()
        .anyRequest().authenticated()
        .and()
        .formLogin()
          .loginPage("/login").permitAll()
          .and()
          .logout().permitAll();
    }
}
