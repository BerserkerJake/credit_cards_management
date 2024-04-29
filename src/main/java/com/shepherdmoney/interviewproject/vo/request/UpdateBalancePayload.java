package com.shepherdmoney.interviewproject.vo.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateBalancePayload {

    private String creditCardNumber;
    
    private LocalDate balanceDate;

    private double balanceAmount;
}
