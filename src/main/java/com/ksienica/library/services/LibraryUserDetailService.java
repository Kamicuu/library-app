/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ksienica.library.services;

import com.ksienica.library.entities.User;
import com.ksienica.library.repositories.UserRepository;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author Kamil
 */
@Service
@Transactional
public class LibraryUserDetailService implements UserDetailsService {
    
    @Autowired
    UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       
        final User userPojo = userRepo.findByLogin(username);
        if (userPojo == null) {
            throw new UsernameNotFoundException(username);
        }
        
        UserDetails user = org.springframework.security.core.userdetails.User.withUsername(userPojo.getLogin())
               .password(userPojo.getPassword())
               .roles(userPojo.getRole())
               .build();

        return user;
    }
    
}
