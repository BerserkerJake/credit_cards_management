package com.shepherdmoney.interviewproject.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.MapKeyTemporal;
import jakarta.persistence.OneToMany;
import jakarta.persistence.TemporalType;

import java.time.LocalDate;
import java.util.TreeMap;
import java.util.SortedMap;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String issuanceBank;

    private String number;

    // Many instances of CreditCard can be associated with
    // one instance of User
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    // One instance of CreditCard can be associated with
    // many instances of BalanceHistory
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "creditCard")
    private SortedMap<LocalDate, BalanceHistory> balanceHistory = new TreeMap<>(); // used SortedMap<> for O(logn) 
                                                                                   // time complexity of insertion/deletion operations

}
