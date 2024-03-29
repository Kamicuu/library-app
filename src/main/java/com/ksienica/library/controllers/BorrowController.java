/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ksienica.library.controllers;

import com.ksienica.library.Definitions;
import com.ksienica.library.Messages;
import com.ksienica.library.exceptions.BookCartExeption;
import com.ksienica.library.exceptions.BookServiceExeptions;
import com.ksienica.library.services.BookBorrowService;
import com.ksienica.library.services.BookService;
import java.io.IOException;
import java.security.Principal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
public class BorrowController {
    
    @Autowired
    BookBorrowService bookCartService;
    
    @Autowired
    BookService bookService;
    
    @GetMapping(value = Definitions.URL_BOOK_CART)
    public String getBorrowCartView(HttpServletRequest request, final Model model){
    
        try {
            model.addAttribute("books", bookCartService.getBooksFromBorrowCart(request.getSession()));
        } catch (BookCartExeption ex) {
            model.addAttribute("error", ex.getMessage());
        }
        
        return Definitions.VIEW_BOOK_BORROW_CART;        
    }
    
    @GetMapping(value = Definitions.URL_ADD_BOOK_TO_BORROW)
    public String addBookToBorrow(HttpServletRequest request, @RequestParam int bookId, final Model model){
    
        try {
            model.addAttribute("book", bookCartService.addBookToBorrowCart(bookService.getBook(bookId), request.getSession()));
            model.addAttribute("sucess", Messages.SUCCESS_BOOK_ADDED_TO_CART);
        } catch (BookServiceExeptions|BookCartExeption ex) {
            try {
                model.addAttribute("book", bookService.getBook(bookId));
            } catch (BookServiceExeptions ex1) {
                model.addAttribute("error", ex.getMessage());
            }
            model.addAttribute("error", ex.getMessage());
        }
        return Definitions.VIEW_BOOK_DETAILS;    
    }
    
    @GetMapping(value = Definitions.URL_REMOVE_BOOK_TO_BORROW)
    public String removeBookToBorrow(HttpServletRequest request, @RequestParam int bookId, final Model model){
    
        try {
            bookCartService.removeBookFromBorrowCart(bookId, request.getSession());
            model.addAttribute("books", bookCartService.getBooksFromBorrowCart(request.getSession()));
        } catch (BookCartExeption ex) {
           model.addAttribute("error", ex.getMessage());
        }
        return Definitions.VIEW_BOOK_BORROW_CART;    
    }
    
    @GetMapping(value = Definitions.URL_MAKE_BORROW)
    public String borrowBooks(HttpServletRequest request, Principal principal, final Model model){
    
        try {
           bookCartService.makeBorrow(request.getSession(), principal.getName());
           model.addAttribute("sucess", Messages.SUCCESS_BOOKS_BORROWED);
        } catch (BookCartExeption|BookServiceExeptions ex) {
            try {
                model.addAttribute("books", bookCartService.getBooksFromBorrowCart(request.getSession()));
            } catch (BookCartExeption ex1) {
                model.addAttribute("error", ex.getMessage());
            }
           model.addAttribute("error", ex.getMessage());
        }
        return Definitions.VIEW_BOOK_BORROW_CART;    
    }
    
    @GetMapping(value = Definitions.URL_BORROWS)
    public String getBorrowsView(HttpServletRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "false") boolean showOnlyBorrowed,
            @RequestParam(defaultValue = "false") boolean showAllBorrows,
            Principal principal,
            final Model model){
        
        model.addAttribute("showOnlyBorrowed", showOnlyBorrowed);
        model.addAttribute("showAllBorrows", showAllBorrows);
        model.addAttribute("pageNum", page);
        
        model.addAttribute("borrows", bookCartService.getBorrows(principal.getName(), page, size, showOnlyBorrowed, showAllBorrows));;
    
        return Definitions.VIEW_BORROWS;
    }
    
    @GetMapping(value = Definitions.URL_RETURN_BORROW)
    public String returnBorrow(@RequestParam int borrowId, HttpServletRequest request, HttpServletResponse resp, Principal principal, final Model model) throws IOException{
    
        try {
            bookCartService.returnBorrow(borrowId, principal.getName());
            resp.sendRedirect(request.getHeader("referer"));
        } catch (BookCartExeption ex) {
            model.addAttribute("error", ex.getMessage());
        }
    
        return Definitions.VIEW_BORROWS;
        
    }
      
}
