/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ksienica.library.dtos;

import com.ksienica.library.entities.UserDetails;
import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 *
 * @author Kamil
 */
public class UserEditDto extends UserDetails {
    
    public UserEditDto(UserDetails details){
        this.setCity(details.getCity());
        this.setFirstName(details.getFirstName());
        this.setLastName(details.getLastName());
        this.setNumber(details.getNumber());
        this.setPhoneNumber(details.getPhoneNumber());
        this.setPostCode(details.getPostCode());
        this.setStreet(details.getStreet());
    }
    
    public UserEditDto(){
    }
    
    @Size(min = 4, max = 100)
    @NotBlank
    private String email;
    
    private String password;

    private String passwordAck;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordAck() {
        return passwordAck;
    }

    public void setPasswordAck(String passwordAck) {
        this.passwordAck = passwordAck;
    }

}
