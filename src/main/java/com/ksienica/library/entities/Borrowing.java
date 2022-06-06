/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksienica.library.entities;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.Table;

/**
 *
 * @author Kamil
 */
@Entity
@Table(name = "Borrowing")
public class Borrowing {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    @Column(name = "borrowing_date")
    private Timestamp borrowingDate;
    
    @Column(name = "returning_date")
    private Timestamp returningDate;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
      name = "borrowing_book", 
      joinColumns = @JoinColumn(name = "id_borrowing"), 
      inverseJoinColumns = @JoinColumn(name = "id_book"))
    private List<Book> likedBooks;
        
    @PrePersist
    protected void onInsert() {
       borrowingDate = new Timestamp(new Date().getTime());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getBorrowingDate() {
        return borrowingDate;
    }

    public void setBorrowingDate(Timestamp borrowingDate) {
        this.borrowingDate = borrowingDate;
    }

    public Timestamp getReturningDate() {
        return returningDate;
    }

    public void setReturningDate(Timestamp returningDate) {
        this.returningDate = returningDate;
    }

    public List<Book> getLikedBooks() {
        return likedBooks;
    }

    public void setLikedBooks(List<Book> likedBooks) {
        this.likedBooks = likedBooks;
    }
}
