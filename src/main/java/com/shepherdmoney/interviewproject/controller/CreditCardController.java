package com.shepherdmoney.interviewproject.controller;

import com.shepherdmoney.interviewproject.model.BalanceHistory;
import com.shepherdmoney.interviewproject.model.CreditCard;
import com.shepherdmoney.interviewproject.model.User;
import com.shepherdmoney.interviewproject.repository.CreditCardRepository;
import com.shepherdmoney.interviewproject.repository.UserRepository;
import com.shepherdmoney.interviewproject.vo.request.AddCreditCardToUserPayload;
import com.shepherdmoney.interviewproject.vo.request.UpdateBalancePayload;
import com.shepherdmoney.interviewproject.vo.response.CreditCardView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
import java.util.SortedMap;

@RestController
public class CreditCardController {

    @Autowired
    CreditCardRepository creditCardRepository;
    @Autowired
    UserRepository userRepository;

    /**
     * Add a credit card to a user
     * 
     * @param payload the payload containing the user id, card number, and card
     *                issuance bank
     * @return the id of the credit card if it is successfully added, otherwise
     *         return not found or bad request
     */
    @PostMapping("/credit-card")
    public ResponseEntity<Integer> addCreditCardToUser(@RequestBody AddCreditCardToUserPayload payload) {

        // Check if the user exists
        // If not, return not found
        Optional<User> userOptional = userRepository.findById(payload.getUserId());
        if (!userOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        // Check if the credit card already exists
        // If it does, return bad request
        CreditCard creditCard = creditCardRepository.findByNumber(payload.getCardNumber());
        if (creditCard != null) {
            return ResponseEntity.badRequest().build();
        }

        // Create the credit card and associate it with the user
        User user = userOptional.get();
        creditCard = new CreditCard();
        creditCard.setIssuanceBank(payload.getCardIssuanceBank());
        creditCard.setNumber(payload.getCardNumber());
        creditCard.setOwner(user);
        creditCard = creditCardRepository.save(creditCard);

        // Return the credit card id
        return ResponseEntity.ok(creditCard.getId());
    }

    /**
     * Get all credit cards of a user
     * 
     * @param userId the id of the user
     * @return a list of all credit card associated with the given userId, using
     *         CreditCardView class
     */
    @GetMapping("/credit-card:all")
    public ResponseEntity<List<CreditCardView>> getAllCardOfUser(@RequestParam int userId) {

        // Check if the user exists
        // If not, return not found
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        List<CreditCard> creditCards = creditCardRepository.findByOwnerId(userId);
        List<CreditCardView> creditCardViews = creditCards.stream()
                .map(card -> new CreditCardView(card.getIssuanceBank(), card.getNumber()))
                .toList();

        return ResponseEntity.ok(creditCardViews);
    }

    /**
     * Get the user id associated with a credit card
     * 
     * @param creditCardNumber the number of the credit card
     * @return the user id associated with the credit card if it exists, otherwise
     *         return bad request
     */
    @GetMapping("/credit-card:user-id")
    public ResponseEntity<Integer> getUserIdForCreditCard(@RequestParam String creditCardNumber) {
        CreditCard creditCard = creditCardRepository.findByNumber(creditCardNumber);
        if (creditCard != null && creditCard.getOwner() != null) {
            return new ResponseEntity<>(creditCard.getOwner().getId(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Update the balance of a credit card
     * 
     * @param payload the payload containing the credit card number, balance date,
     *               and balance amount
     * @return OK if the balance is successfully updated, otherwise return bad request
     */
    @PostMapping("/credit-card:update-balance")
    public ResponseEntity<Integer> updateBalanceFromPayload(@RequestBody UpdateBalancePayload[] payload) {

        // Check if the credit card exists
        // If not, return bad request
        for (UpdateBalancePayload payloadItem : payload) {
            String cardNumber = payloadItem.getCreditCardNumber();
            CreditCard creditCard = creditCardRepository.findByNumber(cardNumber);
            if (creditCard == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        // Iterate over the payload and update the balance history of each credit card
        for (UpdateBalancePayload payloadItem : payload) { 
            CreditCard creditCard = creditCardRepository.findByNumber(payloadItem.getCreditCardNumber());
            SortedMap<LocalDate, BalanceHistory> balanceHistory = creditCard.getBalanceHistory();
            LocalDate balanceDate = payloadItem.getBalanceDate(); 
            double balanceAmount = payloadItem.getBalanceAmount();
            
            // If the balance date is before the first date, set the first date to the
            // balance date
            LocalDate firstDate = balanceDate;
            if (balanceHistory.size() > 0 && balanceHistory.firstKey().isBefore(balanceDate)) {
                firstDate = balanceHistory.firstKey();
            }
            LocalDate lastDate = LocalDate.now();
            double previousBalance = 0;
            double diffBalance = 0;

            // Iterate over the range from the first date to the last date
            for (LocalDate date = firstDate; !date.isAfter(lastDate); date = date.plusDays(1)) {
                BalanceHistory newHistory = new BalanceHistory();
                // If the date exists in the original balance history, use its history
                if (balanceHistory.containsKey(date)) {
                    newHistory = balanceHistory.get(date);
                    previousBalance = newHistory.getBalance();
                } else {
                    // Otherwise, create a new history with the date and credit card
                    newHistory.setDate(date);
                    newHistory.setCreditCard(creditCard);
                }

                // If the date is the balance date, calculate the difference between the balance
                // amount and the previous balance
                if (date.isEqual(balanceDate)) {
                    diffBalance = balanceAmount - previousBalance;
                }
                // Update the balance history with the previous balance plus the difference
                newHistory.setBalance(previousBalance + diffBalance);
                // Save the updated balance history
                balanceHistory.put(date, newHistory); 
            }

            // Save the updated credit card
            creditCard.setBalanceHistory(balanceHistory);
            creditCardRepository.save(creditCard);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
