/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksienica.library.repositories;

import com.ksienica.library.entities.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Kamil
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Integer>{
    
    @Query(value = "SELECT LIBRARYDB.BOOK.* FROM LIBRARYDB.BOOK LEFT JOIN LIBRARYDB.BORROWING_BOOK BB on LIBRARYDB.BOOK.ID = BB.ID_BOOK LEFT JOIN LIBRARYDB.BORROWING B on B.ID = BB.ID_BORROWING WHERE B.RETURNING_DATE IS NULL",
    countQuery = "SELECT count(*) FROM LIBRARYDB.BOOK LEFT JOIN LIBRARYDB.BORROWING_BOOK BB on LIBRARYDB.BOOK.ID = BB.ID_BOOK LEFT JOIN LIBRARYDB.BORROWING B on B.ID = BB.ID_BORROWING WHERE B.RETURNING_DATE IS NULL;",
    nativeQuery = true)
    Page<Book> findAllNotBorrowed(Pageable pageable);

    @Query(value = "SELECT LIBRARYDB.BOOK.* FROM LIBRARYDB.BOOK LEFT JOIN LIBRARYDB.BORROWING_BOOK BB on LIBRARYDB.BOOK.ID = BB.ID_BOOK LEFT JOIN LIBRARYDB.BORROWING B on B.ID = BB.ID_BORROWING WHERE B.RETURNING_DATE IS NULL AND LIBRARYDB.BOOK.AUTHOR LIKE %:filter%",
    countQuery = "SELECT count(*) FROM LIBRARYDB.BOOK LEFT JOIN LIBRARYDB.BORROWING_BOOK BB on LIBRARYDB.BOOK.ID = BB.ID_BOOK LEFT JOIN LIBRARYDB.BORROWING B on B.ID = BB.ID_BORROWING WHERE B.RETURNING_DATE IS NULL AND LIBRARYDB.BOOK.AUTHOR LIKE %:filter%",
    nativeQuery = true)
    Page<Book> findAllNotBorrowedByAuthor(@Param("filter") String filter, Pageable paging);

    @Query(value = "SELECT LIBRARYDB.BOOK.* FROM LIBRARYDB.BOOK LEFT JOIN LIBRARYDB.BORROWING_BOOK BB on LIBRARYDB.BOOK.ID = BB.ID_BOOK LEFT JOIN LIBRARYDB.BORROWING B on B.ID = BB.ID_BORROWING WHERE B.RETURNING_DATE IS NULL AND LIBRARYDB.BOOK.TITLE LIKE %:filter%",
    countQuery = "SELECT count(*) FROM LIBRARYDB.BOOK LEFT JOIN LIBRARYDB.BORROWING_BOOK BB on LIBRARYDB.BOOK.ID = BB.ID_BOOK LEFT JOIN LIBRARYDB.BORROWING B on B.ID = BB.ID_BORROWING WHERE B.RETURNING_DATE IS NULL AND LIBRARYDB.BOOK.TITLE LIKE %:filter%",
    nativeQuery = true)
    Page<Book> findAllNotBorrowedByTitle(@Param("filter") String filter, PageRequest paging);
    
}
