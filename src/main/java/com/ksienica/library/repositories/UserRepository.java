/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksienica.library.repositories;

import com.ksienica.library.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Kamil
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    public User findByLogin(String username);

    public User findByEmail(String email);
    
}
