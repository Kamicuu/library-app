/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksienica.library.entities;

import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.PreUpdate;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 *
 * @author Kamil
 */
@Entity
@Table(name = "User_details")
public class UserDetails {
    
    @Id
    @Column(name = "user_id")
    private int userId;
    
    @Column(name = "first_name")
    @Size(min = 3, max = 100)
    @NotBlank
    private String firstName;
    
    @Column(name = "last_name")
    @Size(min = 3, max = 100)
    @NotBlank
    private String lastName;
    
    @Column(name = "city")
    @Size(min = 3, max = 100)
    @NotBlank
    private String city;
    
    @Column(name = "post_code")
    @Size(min = 6, max = 6)
    @NotBlank
    private String postCode;
    
    @Column(name = "street")
    @Size(min = 3, max = 100)
    @NotBlank
    private String street;
    
    @Column(name = "number")
    @Size(min = 1, max = 15)
    @NotBlank
    private String number;
    
    @Column(name = "updated_at")
    private Timestamp updatedAt;
    
    @Column(name = "created_at")
    @NotBlank
    private Timestamp createdAt;
    
    @OneToOne(fetch = FetchType.LAZY) 
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
      
    @PreUpdate
    protected void onUpdate() {
       updatedAt = new Timestamp(new Date().getTime());
    }
    
    @PrePersist
    protected void onInsert() {
       createdAt = new Timestamp(new Date().getTime());
    }
}
