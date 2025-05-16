package com.mk.demo.service;

import com.mk.demo.entity.Expense;

import java.util.Date;
import java.util.List;

public interface ExpenseService {

    Expense addExpense(Expense expense);

    Expense findById(Long id);

    List<Expense> findAll();

    void deleteById(Long id);

    Expense updateExpense(Long id, Expense expenseDetails);

    List<Expense> findAllByDate(Date date);
}
