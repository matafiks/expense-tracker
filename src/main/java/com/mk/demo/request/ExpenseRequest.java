package com.mk.demo.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class ExpenseRequest {

    @NotNull(message = "Date is mandatory")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @NotNull(message = "Amount is mandatory")
    @Digits(integer = 10, fraction = 2, message = "Maximum 10 integer digits and 2 decimal digits allowe")
    private BigDecimal amount;

    @NotBlank(message = "Category is mandatory")
    private String category;

    private String description;
}