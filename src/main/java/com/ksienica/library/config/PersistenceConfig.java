/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksienica.library.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 *
 * @author Kamil
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.ksienica.library") 
@EntityScan(basePackages = "com.ksienica.library")
public class PersistenceConfig {
    
}
