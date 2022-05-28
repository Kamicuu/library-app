/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ksienica.library.thyemleaf;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;



/**
 *
 * @author Kamil
 */
public class ThymeleafLayoutInterceptor implements HandlerInterceptor {

    private static final String DEFAULT_LAYOUT = "layouts/default";
    private static final String DEFAULT_VIEW_ATTRIBUTE_NAME = "view";  
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
         
        if (modelAndView == null || !modelAndView.hasView()) {
            return;
        }
        String originalViewName = modelAndView.getViewName();
        modelAndView.setViewName(DEFAULT_LAYOUT);
        modelAndView.addObject(DEFAULT_VIEW_ATTRIBUTE_NAME, "content/"+originalViewName);
    }
 
    
}
