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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
        
        http
            //login
            .formLogin(form -> form
                .loginPage("/login")
                .permitAll()
            ).authorizeRequests()
            //Permit all
            .antMatchers(Definitions.URL_CSS, Definitions.URL_JS, Definitions.URL_HOME, Definitions.URL_BOOKS)
            .permitAll()
            //Permit anonymus
            .antMatchers(Definitions.URL_REGISTER)
            .anonymous()
            //Permit users
            .antMatchers(Definitions.URL_EDIT_USER+"/*")
            .hasRole(Definitions.USER_READER_ROLE)
            .anyRequest()
            .authenticated()
            //logout
            .and()
            .logout()
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl(Definitions.URL_HOME)
            .deleteCookies("JSESSIONID")
            .invalidateHttpSession(true); 
        
        return http.build();
    }
    
}
