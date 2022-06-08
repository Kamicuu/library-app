/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ksienica.library;

/**
 *
 * @author Kamil
 */
public final class Messages {
    
    //Success messages
    public final static String SUCCESS_USER_REGISTERED = "Użytkownik został zarejestorwany!";
    public final static String SUCCESS_USER_EDITED = "Edycja użytkownika zakończona pomyślnie!";
    public final static String SUCCESS_BOOK_EDITED = "Edycja książki zakończona pomyślnie!";
    public final static String SUCCESS_BOOK_REMOVED = "Książka została usunięta";
    public final static String SUCCESS_BOOK_ADDED_TO_CART= "Książka została dodana do koszyka wypożyczeń!";
    public final static String SUCCESS_BOOKS_BORROWED= "Książki zostały wypożyczone";
    public final static String SUCCESS_BOOKS_RETURNED= "Książki zostały zwrócone";
    
    //Error messages
    public static final String ERROR_PASSWORD_DIFFERENT = "Podane hasła nie są takie same!";
    public static final String ERROR_PASSWORD_NOT_SAFE = "Podane hasło nie spełnia wymogów bezpieczeństwa!";
    public static final String ERROR_EMAIL_FORMAT_INCORRECT = "Format adresu e-mail jest niepoprawny!";
    public static final String ERROR_LOGIN_FORMAT_INCORRECT = "Format loginu jest niepoprawny!";
    public static final String ERROR_PHONE_NUMBER_FORMAT_INCORRECT = "Podany numer telefonu jest niepoprawny!";
    public static final String ERROR_USER_EXISTS = "Użytkownik o podanym email lub loginie już istnieje!";
    public static final String ERROR_CAPTCHA_VALIDATION_FAILED = "Walidacja captcha niepoprawna, spróbuj jeszcze raz.";
    public static final String ERROR_ALL_FIELDS_REQUIRED = "Wszystkie pola muszą zostać wypełnione.";
    public static final String ERROR_FIRST_OR_LAST_NAME_INCORRECT_FORMAT = "Imię lub nazwisko jest w nieprawidłowym formacie.";
    public static final String ERROR_HOME_NUMBER_INCORRECT = "Numier mieszkania jest w nieprawidłowym formacie.";
    public static final String ERROR_POST_CODE_INCORRECT = "Kod pocztowy jest w nieprawidłowym formacie.";
    public static final String ERROR_STREET_FORMAT_INCORRECT = "Ulica jest w nieprawidłowym formacie.";
    public static final String ERROR_USER_NOT_FOUND = "Użytkownik nie został zanleziony.";
    public static final String ERROR_CITY_FORMAT_INCORRECT = "Miasto jest w nieprawidłowym formacie.";
    public static final String ERROR_USER_INVALID_ROLE = "Podana rola nie istnieje w systemie.";
    public static final String ERROR_ONE_ADMIN_REQUIRED = "W systemie musi istnieć przynajmniej jeden administrator!";
    public static final String ERROR_BOOK_NOT_FOUND = "Nie znaleziono książki.";
    public static final String ERROR_INCORRECT_FILE = "Plik jest niepoprawny.";
    public static final String ERROR_SAVE_FILE = "Wystąpił błąd podczas zapisu pliku.";
    public static final String ERROR_BOOK_EXISTS = "Książka o podanym Id już istnieje";
    public static final String ERROR_BOOK_ALREADY_EXISTS_IN_CART = "Książka o podanym Id została dodana do koszyka wypożyczeń!";
    public static final String ERROR_BOOK_NOT_FOUND_IN_CART = "Brak książek w koszyku wypożyczeń!";
    public static final String ERROR_TOO_MANY_BOOKS_IN_CART = "Limit książek do wypożyczenia został osiągnięty.";
    public static final String ERROR_BOOK_NO_AVAILABLE= "Książka nie jest dostępna. Pozycja: ";
    public static final String ERROR_USER_DETAILS_NOT_FOUND= "Wypełnij szczegóły użytkownika w opcjach edycji użytkownika!";
    public static final String ERROR_BORROWS_LIMIT_EXCEED= "Limit aktywnych wypożyczeń został osiągnięty!";
    public static final String ERROR_BORROW_NOT_FINISHED= "Wypożyczenie nie zostało zamknięte, być może zostało zamknięte wcześniej lub nie masz uprawnień do zamknięcia tego wypożyczenia.";
    
}
