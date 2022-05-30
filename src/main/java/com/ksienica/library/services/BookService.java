/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ksienica.library.services;

import com.ksienica.library.Definitions;
import com.ksienica.library.entities.Book;
import com.ksienica.library.repositories.BookRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 *
 * @author Kamil
 */
@Service
public class BookService {
    
    @Autowired
    BookRepository bookRepo;
    
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
    
    
    
}
