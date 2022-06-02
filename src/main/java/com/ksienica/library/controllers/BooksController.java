/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ksienica.library.controllers;

import com.ksienica.library.Definitions;
import com.ksienica.library.Messages;
import com.ksienica.library.entities.Book;
import com.ksienica.library.exceptions.BookCartExeption;
import com.ksienica.library.exceptions.BookServiceExeptions;
import com.ksienica.library.services.BookCartService;
import com.ksienica.library.services.BookService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Kamil
 */
@Controller
public class BooksController {
    
    @Autowired
    BookService bookService;
    
    @Autowired
    BookCartService bookCartService;
    
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
    
    @GetMapping(path = Definitions.URL_BOOK_DETAILS)
    public String getBookDetailsView(@RequestParam int bookId, final Model model){
        
        try {
            model.addAttribute("book", bookService.getBook(bookId));
        } catch (BookServiceExeptions ex) {
            model.addAttribute("error", ex.getMessage());
        }
    
        return Definitions.VIEW_BOOK_DETAILS;
    }
    
    @GetMapping(path = Definitions.URL_EDIT_BOOKS)
    public String getEditBooksView(@RequestParam(defaultValue = "") String searchText, 
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
    
        return Definitions.VIEW_EDIT_BOOKS;
    }
    
    @GetMapping(path = Definitions.URL_EDIT_BOOK)
    public String getEditBookView(@RequestParam int bookId, final Model model){
        
        try {
            model.addAttribute("book", bookService.getBook(bookId));
        } catch (BookServiceExeptions ex) {
            model.addAttribute("error", ex.getMessage());
        }
    
        return Definitions.VIEW_EDIT_BOOK;
    }
    
    @PostMapping(path = Definitions.URL_EDIT_BOOK, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String editBook(Book book, final Model model){
        
        try {
            bookService.editBook(book);
            model.addAttribute("book", bookService.getBook(book.getId()));
            model.addAttribute("sucess", Messages.SUCCESS_BOOK_EDITED);
        } catch (BookServiceExeptions ex) {
            model.addAttribute("error", ex.getMessage());
        }
    
        return Definitions.VIEW_EDIT_BOOK;
    }
    
    @PostMapping(path = Definitions.URL_EDIT_BOOK_PHOTO)
    public String editBook(@RequestParam("photo") MultipartFile file, @RequestParam("bookId") int bookId, final Model model){
    
        try {
            bookService.saveBookImage(file, bookId);
            model.addAttribute("book", bookService.getBook(bookId));
            model.addAttribute("sucess", Messages.SUCCESS_BOOK_EDITED);
        } catch (BookServiceExeptions ex) {
            model.addAttribute("error", ex.getMessage());
        }
        return Definitions.VIEW_EDIT_BOOK;    
    }
    
    @GetMapping(path = Definitions.URL_ADD_BOOK)
    public String getAddBookView(final Model model){
            
        return Definitions.VIEW_EDIT_BOOK;
    }
    
    @PostMapping(path = Definitions.URL_ADD_BOOK)
    public String addBook(@Valid Book book, final Model model){
    
        try {
            model.addAttribute("book", bookService.addBook(book));
            model.addAttribute("sucess", Messages.SUCCESS_BOOK_EDITED);
        } catch (BookServiceExeptions ex) {
            model.addAttribute("error", ex.getMessage());
        }
        return Definitions.VIEW_EDIT_BOOK;    
    }
    
    @GetMapping(path = Definitions.URL_DELETE_BOOK)
    public String deleteBook(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam("bookId") int bookId, 
            final Model model){

        model.addAttribute("pageNum", page);
        
        try {
            bookService.removeBook(bookId);
            model.addAttribute("sucess", Messages.SUCCESS_BOOK_REMOVED);
            model.addAttribute("books", bookService.getFreeBooks(page, size));
        } catch (BookServiceExeptions ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("books", bookService.getFreeBooks(page, size));
        }
         return Definitions.VIEW_EDIT_BOOKS;    
    }
    
    @GetMapping(value = Definitions.URL_ADD_BOOK_TO_BORROW)
    public String addBookToBorrow(HttpServletRequest request, @RequestParam int bookId, final Model model){
    
        try {
            model.addAttribute("book", bookCartService.addBookToBorrowCart(bookService.getBook(bookId), request.getSession()));
            model.addAttribute("sucess", Messages.SUCCESS_BOOK_ADDED_TO_CART);
        } catch (BookServiceExeptions|BookCartExeption ex) {
            model.addAttribute("error", ex.getMessage());
        }
        return Definitions.VIEW_BOOK_DETAILS;    
    }
    
    @GetMapping(value = Definitions.URL_BOOK_CART)
    public String getBorrowCartView(HttpServletRequest request, final Model model){
    
        try {
            model.addAttribute("book", bookCartService.getBooksFromBorrowCart(request.getSession()));
        } catch (BookCartExeption ex) {
            model.addAttribute("error", ex.getMessage());
        }
        
        return Definitions.VIEW_BOOK_BORROW_CART;        
    }
}
