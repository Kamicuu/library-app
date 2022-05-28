/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ksienica.library.services;

import com.ksienica.library.Definitions;
import com.ksienica.library.Messages;
import com.ksienica.library.dtos.UserDto;
import com.ksienica.library.entities.User;
import com.ksienica.library.exceptions.UserRegistrationException;
import com.ksienica.library.repositories.UserRepository;
import java.util.regex.Pattern;
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
    
    public void registerUser(UserDto user) throws UserRegistrationException {
        
        validateUser(user);
        
        if(userRepo.findByLogin(user.getLogin())!=null || userRepo.findByEmail(user.getEmail())!=null) {
            throw new UserRegistrationException(Messages.ERROR_USER_EXISTS);
        }
        
        User newUser = new User();
        
        newUser.setLogin(user.getLogin());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRole(Definitions.USER_DEFAULT_ROLE);
        userRepo.save(newUser);

    }
    
    private static void validateUser(UserDto user) throws UserRegistrationException {
                 
        if(!user.getPassword().equals(user.getPasswordAck())){
            throw new UserRegistrationException(Messages.ERROR_PASSWORD_DIFFERENT);
        }
        
        if(!Pattern.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$", user.getPassword())){
            throw new UserRegistrationException(Messages.ERROR_PASSWORD_NOT_SAFE);
        }
    
        if(!Pattern.matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", user.getEmail())){
            throw new UserRegistrationException(Messages.ERROR_EMAIL_FORMAT_INCORRECT);
        }
          
        if(!Pattern.matches("^[a-zA-Z0-9](_(?!(\\.|_))|\\.(?!(_|\\.))|[a-zA-Z0-9]){4,45}[a-zA-Z0-9]$", user.getLogin())){
            throw new UserRegistrationException(Messages.ERROR_LOGIN_FORMAT_INCORRECT);
        }
        
        if(!Pattern.matches("^(?=.*[0-9]).{9,9}$", user.getPhoneNumber())){
            throw new UserRegistrationException(Messages.ERROR_PHONE_NUMBER_FORMAT_INCORRECT);
        }

    }
 
}
