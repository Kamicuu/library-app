/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksienica.library.entities;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author Kamil
 */
@Entity
@Table(name = "Book")
public class Book {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
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
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
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
    private boolean canBeBorrowed;
    
    @ManyToMany(mappedBy = "likedBooks", fetch = FetchType.LAZY)
    @OrderBy("returningDate")
    private List<Borrowing> likedBorrowings;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPublishingHouse() {
        return publishingHouse;
    }

    public void setPublishingHouse(String publishingHouse) {
        this.publishingHouse = publishingHouse;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isCanBeBorrowed() {
        return canBeBorrowed;
    }

    public void setCanBeBorrowed(boolean canBeBorrowed) {
        this.canBeBorrowed = canBeBorrowed;
    }

    public List<Borrowing> getLikedBorrowings() {
        return likedBorrowings;
    }

    public void setLikedBorrowings(List<Borrowing> likedBorrowings) {
        this.likedBorrowings = likedBorrowings;
    }
    
}
