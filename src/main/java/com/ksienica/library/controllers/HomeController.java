/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksienica.library.controllers;

import com.ksienica.library.Definitions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

/**
 *
 * @author Kamil
 */
@Controller
public class HomeController {

    @GetMapping
    public String getHomeViev(){
        
        return Definitions.VIEW_HOME;
       
    }
    
}
