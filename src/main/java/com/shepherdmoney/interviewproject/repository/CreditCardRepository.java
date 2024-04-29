package com.shepherdmoney.interviewproject.repository;

import com.shepherdmoney.interviewproject.model.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Crud repository to store credit cards
 */
@Repository("CreditCardRepo")
public interface CreditCardRepository extends JpaRepository<CreditCard, Integer> {
    
    // By defining our own methods to directly pull out the credit card,
    // we don't need to use the Optional<> data structure
    // or get() methods to access the actual cards
    List<CreditCard> findByOwnerId(Integer ownerId);
    CreditCard findByNumber(String number);
}
