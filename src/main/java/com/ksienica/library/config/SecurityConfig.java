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
                .defaultSuccessUrl(Definitions.URL_HOME)
                .permitAll()
            ).authorizeRequests()
            //Permit all
            .antMatchers(Definitions.URL_CSS, Definitions.URL_JS, Definitions.URL_HOME, Definitions.URL_IMAGES, Definitions.URL_BOOKS, Definitions.URL_BOOK_DETAILS)
            .permitAll()
            //Permit anonymus
            .antMatchers(Definitions.URL_REGISTER)
            .anonymous()
            //Permit users
            .antMatchers(Definitions.URL_EDIT_USER, Definitions.URL_ADD_BOOK_TO_BORROW, Definitions.URL_BOOK_CART)
            .hasAnyRole(Definitions.USER_READER_ROLE, Definitions.USER_LIBRARIAN_ROLE, Definitions.USER_ADMIN_ROLE)
            //Permit librarian
            .antMatchers(Definitions.URL_EDIT_BOOKS, Definitions.URL_EDIT_BOOK, Definitions.URL_ADD_BOOK)
            .hasAnyRole(Definitions.USER_ADMIN_ROLE, Definitions.USER_LIBRARIAN_ROLE)
            //Permit admin
            .antMatchers(Definitions.URL_EDIT_USER_PRIVILIGES)
            .hasRole(Definitions.USER_ADMIN_ROLE)
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
