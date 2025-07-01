package com.mk.demo.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
@Setter
public class ExpenseResponse {

    private Long id;

    private LocalDate date;

    private BigDecimal amount;

    private String category;

    private String description;
}
