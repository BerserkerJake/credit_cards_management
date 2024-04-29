package com.shepherdmoney.interviewproject.vo.request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CreateUserPayload {

    private String name;

    private String email;

    private LocalDate dateOfBirth;

}
