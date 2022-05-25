/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksienica.library.entities;

import java.util.Date;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 *
 * @author Kamil
 */
@Entity
@Table(name = "Book")
public class Book {
    
    @Id
    @Column(name = "id")
    @Size(min = 1, max = 100)
    @NotBlank
    private String id;
    
    @Column(name = "isbn")
    @Size(min = 1, max = 100)
    private String isbn;
    
    @Column(name = "title")
    @Size(min = 3, max = 255)
    @NotBlank
    private String title;
    
    @Column(name = "author")
    @Size(min = 3, max = 255)
    @NotBlank
    private String author;
    
    @Column(name = "publication_date")
    @NotBlank
    @Temporal(TemporalType.DATE)
    private Date publicationDate;
    
    @Column(name = "language")
    @Size(min = 3, max = 100)
    @NotBlank
    private String language;
    
    @Column(name = "publishing_house")
    @Size(min = 3, max = 255)
    @NotBlank
    private String publishingHouse;
    
    @Column(name = "description")
    @Size(min = 10, max = 500)
    private String description;
    
    @Column(name = "image_url")
    @Size(min = 6, max = 255)
    private String imageUrl;
    
    @Column(name = "can_be_borrowed")
    @NotBlank
    private boolean canBeBorrowed;
    
    @ManyToMany(mappedBy = "likedBooks", fetch = FetchType.LAZY)
    private Set<Borrowing> likedBorrowings;
}
