/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ksienica.library.services;

import com.ksienica.library.Definitions;
import com.ksienica.library.Messages;
import com.ksienica.library.entities.Book;
import com.ksienica.library.exceptions.BookServiceExeptions;
import com.ksienica.library.repositories.BookRepository;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Kamil
 */
@Service
@Transactional
public class BookService {
    
    @Autowired
    BookRepository bookRepo;
    
    private final Path root = Paths.get("uploads");
    
    public List<Book> getFreeBooks(String filter, String filterType, int page, int size){
    
        PageRequest paging = PageRequest.of(page, size);
        List<Book> books = new ArrayList<Book>();
       
        switch (filterType) {
            case Definitions.SEARCH_AUTHOR:
                books = bookRepo.findAllNotBorrowedByAuthor(filter, paging).toList();
                break;
            case Definitions.SEARCH_TITLE:
                books = bookRepo.findAllNotBorrowedByTitle(filter, paging).toList();
                break;
        }
        
        return books;
        
    }
    
    public List<Book> getFreeBooks(int page, int size){
        
        PageRequest paging = PageRequest.of(page, size);
    
        return bookRepo.findAllNotBorrowed(paging).toList();
    }
    
    public Book getBook(int bookId) throws BookServiceExeptions{
        
        Optional<Book> book = bookRepo.findById(bookId);
                
        return book.orElseThrow(()->new BookServiceExeptions(Messages.ERROR_BOOK_NOT_FOUND));
    
    }

    public void editBook(Book book) throws BookServiceExeptions {
        
        var finded = bookRepo.findById(book.getId());
               
        if(finded.isPresent()){
            book.setImageUrl(finded.get().getImageUrl());
            bookRepo.save(book);
        }else throw new BookServiceExeptions(Messages.ERROR_BOOK_NOT_FOUND);
           
    }

    public void saveBookImage(MultipartFile file, int bookId) throws BookServiceExeptions {
        
        var book = getBook(bookId);
        var bookFileName = "img_book_id_"+bookId+".jpg";
        book.setImageUrl("/uploads/images/"+bookFileName);
        
        try{
            Files.createDirectories(root);

            if(file.isEmpty()){
                throw new BookServiceExeptions(Messages.ERROR_INCORRECT_FILE);
            }

            try {
                Files.copy(file.getInputStream(), this.root.resolve(bookFileName));
                bookRepo.save(book);
            }catch (FileAlreadyExistsException ex){
                Files.delete(this.root.resolve(bookFileName));
                Files.copy(file.getInputStream(), this.root.resolve(bookFileName));
                bookRepo.save(book);
            }  
        }catch(IOException ex){
            throw new BookServiceExeptions(Messages.ERROR_SAVE_FILE);
        }
    }

    public Book addBook(Book book) throws BookServiceExeptions {
        
        if(bookRepo.findById(book.getId()).isPresent()){
            throw new BookServiceExeptions(Messages.ERROR_BOOK_EXISTS);
        }
        
        return bookRepo.save(book);
    }

    public void removeBook(int bookId) throws BookServiceExeptions {
        
        bookRepo.delete(getBook(bookId));
        
    }
      
}
