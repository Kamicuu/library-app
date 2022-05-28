/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ksienica.library.exceptions;

/**
 *
 * @author Kamil
 */
public class UserAlreadyExistException extends Exception {
    
    public UserAlreadyExistException(String username) {
        
        super("User with name "+username+" alredy exist!");
    }
        
}
