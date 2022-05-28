/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ksienica.library.services;

import com.ksienica.library.Definitions;
import com.ksienica.library.entities.User;
import com.ksienica.library.exceptions.UserAlreadyExistException;
import com.ksienica.library.repositories.UserRepository;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author Kamil
 */
@Service
@Transactional
public class LibraryUserService{
    
    @Autowired
    UserRepository userRepo;
    
    @Autowired 
    PasswordEncoder passwordEncoder;
    
    public void registerUser(User user) throws UserAlreadyExistException {
        
        if(userRepo.findByLogin(user.getLogin())!=null) {
            throw new UserAlreadyExistException(user.getLogin());
        }
        
        User newUser = new User();
        
        newUser.setLogin(user.getLogin());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRole(Definitions.USER_DEFAULT_ROLE);
        userRepo.save(newUser);

    }

    
}
