/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ksienica.library.services;

import com.ksienica.library.Definitions;
import com.ksienica.library.Messages;
import com.ksienica.library.entities.Book;
import com.ksienica.library.entities.Borrowing;
import com.ksienica.library.exceptions.BookCartExeption;
import com.ksienica.library.exceptions.BookServiceExeptions;
import com.ksienica.library.repositories.BorrowingRepository;
import com.ksienica.library.repositories.UserRepository;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
    
    @Autowired
    LibraryUserService userService;
    
    @Autowired
    UserRepository userRepo;
    
    @Autowired
    BorrowingRepository borrowRepo;
    
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
    
    public void clearBooksFromBorrowCart(HttpSession session){
    
        session.setAttribute("booksList", null);
        
    }

    public void makeBorrow(HttpSession session, String userLogin) throws BookCartExeption, BookServiceExeptions {
        
        var books = getBooksFromBorrowCart(session);
        var borrow = new Borrowing();
        var user = userRepo.findByLogin(userLogin);
        
        
        for(var book : books){
            if(!checkIfBookAvailableToBorrow(book))
                throw new BookCartExeption(Messages.ERROR_BOOK_NO_AVAILABLE+book.getTitle());
        }
        
        if(!userService.checkIfUserHasDetailsData(userLogin))
            throw new BookCartExeption(Messages.ERROR_USER_DETAILS_NOT_FOUND);
        
        if(!checkIfAvailableBorrow(userLogin))
            throw new BookCartExeption(Messages.ERROR_BORROWS_LIMIT_EXCEED);
        
        borrow.setBorrowingDate(Timestamp.valueOf(LocalDateTime.now()));
        borrow.setLikedBooks(books);
        borrow.setLikedUser(user);
        
        borrowRepo.save(borrow);
        
        clearBooksFromBorrowCart(session);
               
    }
    
    public List<Borrowing> getBorrows(String login, int page, int size, boolean showOnlyBorrowed, boolean showAllBorrows){
        
        //checkin roles
        var role = userService.getUserRole(login);
        
        if(role.equals(Definitions.USER_LIBRARIAN_ROLE) || role.equals(Definitions.USER_ADMIN_ROLE)){
            
            if(showAllBorrows&&showOnlyBorrowed){
                return getAllOnlyBorrowed(page, size);
            }
            else if(showAllBorrows){
                return getAllBorrows(page, size);
            }else if(showOnlyBorrowed){
                return getOnlyBorrowedByUser(login);
            } 
        }
        
        return getBorrowsByUser(login);
    }
    
    public List<Borrowing> getBorrowsByUser(String login){
    
        var borrows = userRepo.findByLogin(login).getLikedBorrowings();
        
        //trigger loading books
        borrows.forEach(borrow->borrow.getLikedBooks().size());
        
        return borrows;
    }
    
    public List<Borrowing> getOnlyBorrowedByUser(String login){
    
        var borrows = userRepo.findByLogin(login).getLikedBorrowings();
        var onlyNotFinished = new ArrayList<Borrowing>();
        
        for(var borrow : borrows){
            //trigger loading books
            borrow.getLikedBooks().size();
            
            if(borrow.getReturningDate()==null)
                onlyNotFinished.add(borrow);
        }
        
        return onlyNotFinished;
    }
    
    public List<Borrowing> getAllOnlyBorrowed(int page, int size){
    
        PageRequest paging = PageRequest.of(page, size);
    
        var borrows = borrowRepo.findAllNotFinished(paging);
        
        //trigger loading books
        borrows.forEach(borrow->borrow.getLikedBooks().size());
        
        return borrows.toList();
    }
    
    public List<Borrowing> getAllBorrows(int page, int size){
        
        PageRequest paging = PageRequest.of(page, size);
    
        var borrows = borrowRepo.findAllByOrderByReturningDate(paging);

        
        //trigger loading books
        borrows.forEach(borrow->borrow.getLikedBooks().size());
        
        return borrows.toList();
    }
    
    public boolean checkIfAvailableBorrow(String userLogin){
        
        var linkedBorrows = userRepo.findByLogin(userLogin).getLikedBorrowings();
        var numberOfBorrows = 0;
        
        //to be parametrized
        if(linkedBorrows.size()>2){
            for(int i=1; i>=0; i--){
                if(linkedBorrows.get(i).getReturningDate()==null){
                    numberOfBorrows++;
                }
            }
        }
        
        if(numberOfBorrows>=2){
            return false;
        }
        else return true;
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

    public void returnBorrow(int borrowId, String name) throws BookCartExeption {
        
        var role = userService.getUserRole(name);
        var borrows = new ArrayList<Borrowing>();
        
        if(role.equals(Definitions.USER_READER_ROLE)){
            
            borrows = (ArrayList<Borrowing>) borrowRepo.findAllNotFinishedByUser(name);
            
        }else if(role.equals(Definitions.USER_LIBRARIAN_ROLE) || role.equals(Definitions.USER_ADMIN_ROLE)){

            borrows = (ArrayList<Borrowing>) borrowRepo.findAllNotFinished();

        }
        
        for(var borrow : borrows){
            if(borrow.getId()==borrowId && borrow.getReturningDate()==null){
                borrow.setReturningDate(Timestamp.valueOf(LocalDateTime.now()));
                borrowRepo.saveAll(borrows);
                return;
            }
        }
        
        throw new BookCartExeption(Messages.ERROR_BORROW_NOT_FINISHED);  
    }
    
}
