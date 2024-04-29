package com.shepherdmoney.interviewproject.model;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "MyUser")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;

    private String email;

    private LocalDate dateOfBirth;

    // One instance of User can be associated with
    // many instances of CreditCard
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<CreditCard> creditCards = new ArrayList<>();
}
