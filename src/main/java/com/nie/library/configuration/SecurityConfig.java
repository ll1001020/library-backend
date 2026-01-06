package com.nie.library.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest()
                .permitAll()
                .and()
                .formLogin()
                .permitAll()
                .and()
                .httpBasic()
                .and()
                //支持跨域访问
                .cors()
                .and()
                .csrf()
                .disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 设置强度，默认为10，可以设置区间为【4，31】
           return new BCryptPasswordEncoder();
    }
}
