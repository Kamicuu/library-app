/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ksienica.library.services;

import com.ksienica.library.Messages;
import com.ksienica.library.entities.Book;
import com.ksienica.library.exceptions.BookCartExeption;
import com.ksienica.library.exceptions.BookServiceExeptions;
import java.util.ArrayList;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Kamil
 */
@Service
@Transactional
public class BookCartService {
    
    @Autowired
    BookService bookService;
    
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
    
    public void removeBookFromBorrowCart(int bookId, HttpSession session) throws BookCartExeption{
    
        var books = getBooksFromBorrowCart(session);
        
        for(var book : books){
            if(book.getId()==bookId){
                    books.remove(book);
                    break;
           }
        }
        session.setAttribute("booksList", books);
    }
    
    public ArrayList<Book> getBooksFromBorrowCart(HttpSession session) throws BookCartExeption{
    
        return (ArrayList<Book>) Optional.ofNullable(session.getAttribute("booksList")).orElseThrow(()->new BookCartExeption(Messages.ERROR_BOOK_NOT_FOUND_IN_CART));
    
    }

    public void makeBorrow(HttpSession session) throws BookCartExeption, BookServiceExeptions {
        
       var books = getBooksFromBorrowCart(session);
       
        for(var book : books){
            if(!checkIfBookAvailableToBorrow(book))
                throw new BookCartExeption(Messages.ERROR_BOOK_NO_AVAILABLE+book.getTitle());
        }
       
    }
    
    public boolean checkIfBookAvailableToBorrow(Book book) throws BookServiceExeptions{
        
        var bookFromRepo = bookService.getBook(book.getId());
        var linkedBorrows = bookFromRepo.getLikedBorrowings();
        
        if(linkedBorrows.size()>0){
            
            var lastBorrowing = bookFromRepo.getLikedBorrowings().get(0);
            
            if(lastBorrowing.getReturningDate()==null){
                return false;
            }

        }
        
        if(!bookFromRepo.isCanBeBorrowed()){
            return false;
        }else return true;
    
    }
    
}
