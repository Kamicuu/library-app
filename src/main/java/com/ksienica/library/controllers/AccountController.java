/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ksienica.library.controllers;

import com.ksienica.library.Definitions;
import com.ksienica.library.Messages;
import com.ksienica.library.dtos.UserDto;
import com.ksienica.library.dtos.UserEditDto;
import com.ksienica.library.exceptions.UserServiceException;
import com.ksienica.library.services.LibraryUserService;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String registerUser(HttpServletRequest request, @Valid UserDto user, final Model model){
        
        try {
            
           userService.registerUser(user, request.getRemoteAddr());
           model.addAttribute("sucess", Messages.SUCCESS_USER_REGISTERED);
           
           return Definitions.VIEW_REGISTER;
           
        } catch (UserServiceException ex) {
            
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
    
    //Edit user handling
    @GetMapping(path = Definitions.URL_EDIT_USER)
    public String getEditUserViev(Principal principal, final Model model){

        model.addAttribute("user", userService.getUserData(principal.getName()));
    
        return Definitions.VIEW_EDIT_USER;
    }
    
    @PostMapping(path = Definitions.URL_EDIT_USER, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String editUser(UserEditDto user, Principal principal, final Model model){
    
        try {
            
            userService.editUser(user, principal.getName());
            model.addAttribute("sucess", Messages.SUCCESS_USER_EDITED);
            model.addAttribute("user", userService.getUserData(principal.getName()));
            
        } catch (UserServiceException ex) {

            model.addAttribute("error", ex.getMessage());
            model.addAttribute("user", user);

        }
        
        return Definitions.VIEW_EDIT_USER;
    }
    
    //priviliges edit handling
    @GetMapping(path = Definitions.URL_EDIT_USER_PRIVILIGES)
    public String getEditUserPriviligesViev(@RequestParam(defaultValue = "") String searchText,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            final Model model){

        var roles = new ArrayList<String>();
        
        roles.add(Definitions.USER_READER_ROLE);
        roles.add(Definitions.USER_LIBRARIAN_ROLE);
        roles.add(Definitions.USER_ADMIN_ROLE);
        
        model.addAttribute("roles",roles);
        model.addAttribute("pageNum", page);
        
        if(searchText.equals("")){
            model.addAttribute("users", userService.getUserData(page, size));
        }else{
            model.addAttribute("users", userService.getUserData(searchText, page, size));  
        }
    
        return Definitions.VIEW_EDIT_USER_PRIVILIGES;
    }
    
    @PostMapping(path = Definitions.URL_EDIT_USER_PRIVILIGES, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String editPriviliges(@RequestParam int userId, 
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam Map<String, String> formData, 
            final Model model){
        
        var roles = new ArrayList<String>();
        
        roles.add(Definitions.USER_READER_ROLE);
        roles.add(Definitions.USER_LIBRARIAN_ROLE);
        roles.add(Definitions.USER_ADMIN_ROLE);
        
        model.addAttribute("roles",roles);
        model.addAttribute("pageNum", page);
    
        try {
            
            userService.editUserRole(userId, formData.get("role"));
            model.addAttribute("sucess", Messages.SUCCESS_USER_EDITED);
            model.addAttribute("users", userService.getUserData(page, size));
            
            
        } catch (UserServiceException ex) {
            
            model.addAttribute("users", userService.getUserData(page, size));
            model.addAttribute("error", ex.getMessage());
            
        }
        
        return Definitions.VIEW_EDIT_USER_PRIVILIGES;
    }
}
