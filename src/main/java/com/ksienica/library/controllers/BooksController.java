/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ksienica.library.controllers;

import com.ksienica.library.Definitions;
import com.ksienica.library.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Kamil
 */
@Controller
public class BooksController {
    
    @Autowired
    BookService bookService;
    
    //Display books list
    @GetMapping(path = Definitions.URL_BOOKS)
    public String getBooksView(@RequestParam(defaultValue = "") String searchText, 
            @RequestParam(defaultValue = "") String searchType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            final Model model){
    
        model.addAttribute("pageNum", page);
        model.addAttribute("searchText", searchText);
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchAuthor", Definitions.SEARCH_AUTHOR);
        model.addAttribute("searchTitle", Definitions.SEARCH_TITLE);
        
        if(searchText.equals("") && searchType.equals("")){
            model.addAttribute("books", bookService.getFreeBooks(page, size));

        }else{
            model.addAttribute("books", bookService.getFreeBooks(searchText, searchType, page, size));
        }

        
        return Definitions.VIEW_BOOKS;
    }
}
