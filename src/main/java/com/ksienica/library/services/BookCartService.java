/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ksienica.library.services;

import com.ksienica.library.Messages;
import com.ksienica.library.entities.Book;
import com.ksienica.library.exceptions.BookCartExeption;
import java.util.ArrayList;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

/**
 *
 * @author Kamil
 */
@Service
public class BookCartService {
    
    public Book addBookToBorrowCart(Book book, HttpSession session) throws BookCartExeption{
                
        ArrayList<Book> books = (ArrayList<Book>) Optional.ofNullable(session.getAttribute("booksList")).orElse(new ArrayList<Book>());
        
        for(Book element : books){
            if(element.getId()==book.getId()){
                throw new BookCartExeption(Messages.ERROR_BOOK_ALREADY_EXISTS_IN_CART);
            }
        }
        
        //to be parametized
        if(books.size()>=3){
            throw new BookCartExeption(Messages.ERROR_TOO_MANY_BOOKS_IN_CART);
        }
        
        books.add(book);
        session.setAttribute("booksList", books);
        return book;
    
    }
    
    public ArrayList<Book> getBooksFromBorrowCart(HttpSession session) throws BookCartExeption{
    
        return (ArrayList<Book>) Optional.ofNullable(session.getAttribute("booksList")).orElseThrow(()->new BookCartExeption(Messages.ERROR_BOOK_NOT_FOUND_IN_CART));
    
    }
    
}
