/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ksienica.library.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ksienica.library.Definitions;
import com.ksienica.library.Messages;
import com.ksienica.library.config.CaptchaSettings;
import com.ksienica.library.dtos.UserDto;
import com.ksienica.library.dtos.UserEditDto;
import com.ksienica.library.entities.User;
import com.ksienica.library.entities.UserDetails;
import com.ksienica.library.exceptions.UserServiceException;
import com.ksienica.library.repositories.UserRepository;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Kamil
 */
@Service
@Transactional
public class LibraryUserService{
    
    @Autowired
    UserRepository userRepo;
    
    @Autowired 
    PasswordEncoder passwordEncoder;
    
    @Autowired
    RestTemplate restTemplate;
    
    @Autowired
    CaptchaSettings captchaSettings;
    
    public void registerUser(UserDto user, String clientIp) throws UserServiceException {
        
        validateUserData(user);
        validateReCaptcha(user.getgRecaptchaResponse(), clientIp);
        
        if(userRepo.findByLogin(user.getLogin())!=null || userRepo.findByEmail(user.getEmail())!=null) {
            throw new UserServiceException(Messages.ERROR_USER_EXISTS);
        }
        
        User newUser = new User();
        
        newUser.setLogin(user.getLogin());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRole(Definitions.USER_DEFAULT_ROLE);
        userRepo.save(newUser);

    }
    
    public UserEditDto getUserData(String username){
        
        var basicUser = userRepo.findByLogin(username);
        var user = new UserEditDto(Optional.ofNullable(basicUser.getUserDetails()).orElse(new UserDetails()));
        
        user.setEmail(basicUser.getEmail());
        
        return user;
    }
    
    public List<User> getUserData(int page, int size){
        
        PageRequest paging = PageRequest.of(page, size);
        
        return userRepo.findAll(paging).toList();
    }
    
    public List<User> getUserData(String filter, int page, int size){
        
        PageRequest paging = PageRequest.of(page, size);
        
        return userRepo.findByLoginContaining(filter, paging);
    
    }
    
    public void editUser(UserEditDto user, String username) throws UserServiceException{
        

        var basicUser = userRepo.findByLogin(username);
        user.setUserId(basicUser.getId());
        user.setUser(basicUser);
        
        if(basicUser==null){
            throw new UserServiceException(Messages.ERROR_USER_NOT_FOUND);
        }
        
        if(!user.getCity().equals("") 
                || !user.getFirstName().equals("") 
                || !user.getLastName().equals("") 
                || !user.getNumber().equals("")
                || !user.getPhoneNumber().equals("")
                || !user.getPostCode().equals("")
                || !user.getStreet().equals("")){
        
            validateContactData(user);
            basicUser.setUserDetails(user);
        }
        
        if(!user.getPassword().equals("")
                || !user.getPasswordAck().equals("")
                ){
            validatePasswordData(user);
            basicUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
                
        validateEmailData(user);
        basicUser.setEmail(user.getEmail());
        
        userRepo.save(basicUser);
        
    }
    
    public void editUserRole(int userId, String userRole) throws UserServiceException{
    
        if(userRole.equals(Definitions.USER_ADMIN_ROLE) || userRole.equals(Definitions.USER_LIBRARIAN_ROLE) || userRole.equals(Definitions.USER_READER_ROLE)){
            var user = userRepo.findById(userId).orElseThrow(() -> {
                return new UserServiceException(Messages.ERROR_USER_NOT_FOUND);
            });
            
            var currentAdmins = userRepo.findAllByRole(Definitions.USER_ADMIN_ROLE);
            
            if(currentAdmins.size()<=1){
                if(currentAdmins.get(0).getId()==userId && !userRole.equals(Definitions.USER_ADMIN_ROLE)){
                    throw new UserServiceException(Messages.ERROR_ONE_ADMIN_REQUIRED);
                }
            }
            
            user.setRole(userRole);
            userRepo.save(user);
            
        }else throw new UserServiceException(Messages.ERROR_USER_INVALID_ROLE);
    
    }
            
    private static void validateUserData(UserDto user) throws UserServiceException {
                 
        if(!user.getPassword().equals(user.getPasswordAck())){
            throw new UserServiceException(Messages.ERROR_PASSWORD_DIFFERENT);
        }
        
        if(!Pattern.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$", user.getPassword())){
            throw new UserServiceException(Messages.ERROR_PASSWORD_NOT_SAFE);
        }
    
        if(!Pattern.matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", user.getEmail())){
            throw new UserServiceException(Messages.ERROR_EMAIL_FORMAT_INCORRECT);
        }
          
        if(!Pattern.matches("^[a-zA-Z0-9](_(?!(\\.|_))|\\.(?!(_|\\.))|[a-zA-Z0-9]){4,45}[a-zA-Z0-9]$", user.getLogin())){
            throw new UserServiceException(Messages.ERROR_LOGIN_FORMAT_INCORRECT);
        }
        
        if(!Pattern.matches("^(?=.*[0-9]).{9,9}$", user.getPhoneNumber())){
            throw new UserServiceException(Messages.ERROR_PHONE_NUMBER_FORMAT_INCORRECT);
        }

    }
    
    private void validateReCaptcha(String reCaptchaResponse, String clientIp) throws UserServiceException{
    
        String uri = String.format("https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s", 
                captchaSettings.getSecret(), 
                reCaptchaResponse, 
                clientIp);
        
        
        JsonNode googleResponse = restTemplate.getForObject(URI.create(uri), ObjectNode.class);
        if(!googleResponse.get("success").asBoolean()){
            throw new UserServiceException(Messages.ERROR_CAPTCHA_VALIDATION_FAILED);
        }
            
    }

    private static void validateContactData(UserEditDto user) throws UserServiceException {
        
        if(user.getCity().equals("") 
             || user.getFirstName().equals("") 
             || user.getLastName().equals("") 
             || user.getNumber().equals("")
             || user.getPhoneNumber().equals("")
             || user.getPostCode().equals("")
             || user.getStreet().equals("")){
         throw new UserServiceException(Messages.ERROR_ALL_FIELDS_REQUIRED);
        }
           
        if(!Pattern.matches("^[\\p{Lu}\\p{M}][\\p{L}\\p{M},.'-]+(?: [\\p{L}\\p{M},.'-]+)*$", user.getFirstName())
                || !Pattern.matches("^[\\p{Lu}\\p{M}][\\p{L}\\p{M},.'-]+(?: [\\p{L}\\p{M},.'-]+)*$", user.getLastName())){
            throw new UserServiceException(Messages.ERROR_FIRST_OR_LAST_NAME_INCORRECT_FORMAT);
        }
               
        if(!Pattern.matches("^(?=.*[0-9]).{9,9}$", user.getPhoneNumber())){
            throw new UserServiceException(Messages.ERROR_PHONE_NUMBER_FORMAT_INCORRECT);
        }
        
        if(!Pattern.matches("^[\\s\\p{L}]+$", user.getCity())){
            throw new UserServiceException(Messages.ERROR_CITY_FORMAT_INCORRECT);
        }
        
        if(!Pattern.matches("^\\w+(\\s|\\/)?\\w*$", user.getNumber())){
            throw new UserServiceException(Messages.ERROR_HOME_NUMBER_INCORRECT);
        }
        
        if(!Pattern.matches("^[0-9]{2}-[0-9]{3}", user.getPostCode())){
            throw new UserServiceException(Messages.ERROR_POST_CODE_INCORRECT);
        }
        
        if(!Pattern.matches("^[\\s\\p{L}]+$", user.getStreet())){
            throw new UserServiceException(Messages.ERROR_STREET_FORMAT_INCORRECT);
        }
         
    }

    private static void validatePasswordData(UserEditDto user) throws UserServiceException {
        
        if(!user.getPassword().equals(user.getPasswordAck())){
            throw new UserServiceException(Messages.ERROR_PASSWORD_DIFFERENT);
        }
        
        if(!Pattern.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$", user.getPassword())){
            throw new UserServiceException(Messages.ERROR_PASSWORD_NOT_SAFE);
        }
    
    }
    
    private static void validateEmailData(UserEditDto user) throws UserServiceException {
        
        if(!Pattern.matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", user.getEmail())){
            throw new UserServiceException(Messages.ERROR_EMAIL_FORMAT_INCORRECT);
        }
    
    }
 
}
