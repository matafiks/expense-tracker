package com.mk.demo.service;

import com.mk.demo.request.ExpenseRequest;
import com.mk.demo.response.ExpenseResponse;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseService {

    ExpenseResponse addExpense(ExpenseRequest expenseRequest);

    ExpenseResponse findById(Long id);

    List<ExpenseResponse> findAll();

    List<ExpenseResponse> findAllByCategory(String category);

    List<ExpenseResponse> findAllByDate(LocalDate localDate);

    void deleteById(Long id);

    ExpenseResponse updateExpense(Long id, ExpenseRequest expenseRequest);
}
