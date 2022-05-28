/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ksienica.library.controllers;

import com.ksienica.library.Definitions;
import com.ksienica.library.Messages;
import com.ksienica.library.dtos.UserDto;
import com.ksienica.library.exceptions.UserRegistrationException;
import com.ksienica.library.services.LibraryUserService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author Kamil
 */
@Controller
public class AccountController {
    
    @Autowired
    LibraryUserService userService;
    
    //Register handling
    @GetMapping(path = Definitions.URL_REGISTER)
    public String getRegisterView(){
    
        return Definitions.VIEW_REGISTER;
    }
    
    @PostMapping(path = Definitions.URL_REGISTER, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String registerUser(@Valid UserDto user, final Model model){
        
        try {
            
           userService.registerUser(user);
           model.addAttribute("sucess", Messages.SUCCESS_USER_REGISTERED);
           
           return Definitions.VIEW_REGISTER;
           
        } catch (UserRegistrationException ex) {
            
           model.addAttribute("error", ex.getMessage());
           model.addAttribute(user);
           
           return Definitions.VIEW_REGISTER;
           
        }
        
    }
    
    //Login handling
    @GetMapping(path = Definitions.URL_LOGIN)
    public String getLoginView(){

        return Definitions.VIEW_LOGIN;
    }
}
