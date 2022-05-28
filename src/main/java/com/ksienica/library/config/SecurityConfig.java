/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ksienica.library.config;

import com.ksienica.library.Definitions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 *
 * @author Kamil
 */

@Configuration
public class SecurityConfig {
    
    @Bean
    PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        
        http.formLogin(form -> form
			.loginPage("/login")
			.permitAll()
		).authorizeRequests()
                .antMatchers(Definitions.URL_CSS, Definitions.URL_JS)
                .permitAll()
                .antMatchers(Definitions.URL_REGISTER, Definitions.URL_HOME)
                .anonymous()
                .antMatchers("/**")
                .hasRole(Definitions.USER_READER_ROLE)
                .anyRequest()
                .authenticated();
        
        return http.build();
    }
    
}
