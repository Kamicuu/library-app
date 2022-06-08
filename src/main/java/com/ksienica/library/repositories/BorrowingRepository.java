/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksienica.library.repositories;

import com.ksienica.library.entities.Borrowing;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Kamil
 */
@Repository
public interface BorrowingRepository extends JpaRepository<Borrowing, Integer>{
    
    @Query(value = "SELECT * FROM LIBRARYDB.BORROWING WHERE LIBRARYDB.BORROWING.RETURNING_DATE IS NULL AND LIBRARYDB.BORROWING.BORROWING_DATE IS NOT NULL",
    nativeQuery = true)
    List<Borrowing> findAllNotFinished();
    
    @Query(value = "SELECT * FROM LIBRARYDB.BORROWING WHERE LIBRARYDB.BORROWING.RETURNING_DATE IS NULL AND LIBRARYDB.BORROWING.BORROWING_DATE IS NOT NULL",
    countQuery = "SELECT count(*) FROM LIBRARYDB.BORROWING WHERE LIBRARYDB.BORROWING.RETURNING_DATE IS NULL AND LIBRARYDB.BORROWING.BORROWING_DATE IS NOT NULL",
    nativeQuery = true)
    Page<Borrowing> findAllNotFinished(Pageable pageable);
    
    @Query(value = "SELECT LIBRARYDB.BORROWING.* FROM LIBRARYDB.BORROWING JOIN LIBRARYDB.USER_LOGIN UL ON LIBRARYDB.BORROWING.ID_USER = UL.ID WHERE LIBRARYDB.BORROWING.RETURNING_DATE IS NULL AND LIBRARYDB.BORROWING.BORROWING_DATE IS NOT NULL AND UL.LOGIN LIKE :user",
    nativeQuery = true)
    List<Borrowing> findAllNotFinishedByUser(String user);

    Page<Borrowing> findAllByOrderByReturningDate(Pageable paging);
    
}
