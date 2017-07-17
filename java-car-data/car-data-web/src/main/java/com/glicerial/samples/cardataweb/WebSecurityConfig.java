package com.glicerial.samples.cardataweb;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable();
        http.antMatcher("/**")
            .authorizeRequests()
            .anyRequest()
            .authenticated().and()
            .logout().logoutSuccessUrl("/login").and()
            .formLogin()
            .loginPage("/login")
            .permitAll()
            .defaultSuccessUrl("/");
    }
}
