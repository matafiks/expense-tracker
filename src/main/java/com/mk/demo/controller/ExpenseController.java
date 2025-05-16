package com.mk.demo.controller;


import com.mk.demo.entity.Expense;
import com.mk.demo.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @Operation(summary = "Create new expense for a logged in user")
    @PostMapping
    public Expense addExpense(@RequestBody Expense expense) {
        return expenseService.addExpense(expense);
    }

    @Operation(summary = "Delete an expense based on id")
    @DeleteMapping("/{expenseId}")
    public void deleteExpenseById(@PathVariable Long expenseId) {
        expenseService.deleteById(expenseId);
    }

    @GetMapping("/{expenseId}")
    public Expense findById(@PathVariable Long expenseId) {
        return expenseService.findById(expenseId);
    }

    @Operation(summary = "List all expenses")
    @GetMapping
    public List<Expense> findAllExpenses() {
        return expenseService.findAll();
    }

    @Operation(summary = "Update expense by id", description = "Only logged in user can update only his expenses")
    @PutMapping("/{expenseId}")
    public Expense updateExpense(@PathVariable Long expenseId, @RequestBody Expense expense) {
        return expenseService.updateExpense(expenseId, expense);
    }
}
