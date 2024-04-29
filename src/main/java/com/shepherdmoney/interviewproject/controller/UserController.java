package com.shepherdmoney.interviewproject.controller;

import com.shepherdmoney.interviewproject.model.User;
import com.shepherdmoney.interviewproject.repository.UserRepository;
import com.shepherdmoney.interviewproject.vo.request.CreateUserPayload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired UserRepository userRepository;

    /**
     * Create a user
     * 
     * @param payload the payload containing the user's name, email, and date of birth
     * @return the id of the user if it is successfully created
     */
    @PutMapping("/user")
    public ResponseEntity<Integer> createUser(@RequestBody CreateUserPayload payload) {
        User user = new User();
        user.setName(payload.getName());
        user.setEmail(payload.getEmail());
        user.setDateOfBirth(payload.getDateOfBirth());
    
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser.getId());
    }

    /**
     * Delete a user
     * 
     * @param userId the id of the user to delete
     * @return a message indicating whether the user was deleted successfully or if the user does not exist
     */
    @DeleteMapping("/user")
    public ResponseEntity<String> deleteUser(@RequestParam int userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return ResponseEntity.ok("User deleted successfully");
        } else {
            return ResponseEntity.badRequest().body("User with id " + userId + " does not exist");
        }
    }
}
