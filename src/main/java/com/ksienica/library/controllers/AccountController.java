/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ksienica.library.controllers;

import com.ksienica.library.Definitions;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author Kamil
 */
@Controller
public class AccountController {
    
    //Register handling
    @GetMapping(path = Definitions.URL_REGISTER)
    public String getRegisterView(){
    
        return Definitions.VIEW_REGISTER;
    }
    
    @PostMapping(path = Definitions.URL_REGISTER, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String registerUser(){
        return null;
    }
    
    //Login handling
    @GetMapping(path = Definitions.URL_LOGIN)
    public String getLoginView(){

        return Definitions.VIEW_LOGIN;
    }
}
