package com.mk.demo.controller;


import com.mk.demo.entity.Expense;
import com.mk.demo.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Expense REST API endpoints", description = "Operations related to expensions")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @Operation(summary = "Create new expense", description = "Create new expense for a logged in user")
    @PostMapping
    public Expense addExpense(@RequestBody Expense expense) {
        // TODO: fix
        return expenseService.addExpense(expense);
    }

    @Operation(summary = "Delete an expense", description = "Delete an expense based on expenseId")
    @DeleteMapping("/{expenseId}")
    public void deleteExpenseById(@PathVariable Long expenseId) {
        // TODO: check
        expenseService.deleteById(expenseId);
    }

    @Operation(summary = "Find an expense by id", description = "Fetch an expense based on expenseId")
    @GetMapping("/{expenseId}")
    public Expense findById(@PathVariable Long expenseId) {
        // TODO: fix
        return expenseService.findById(expenseId);
    }

    @Operation(summary = "List all expenses", description = "Fetch a list of all expenses for a logged in user")
    @GetMapping
    public List<Expense> findAllExpenses() {
        // TODO: fix
        return expenseService.findAll();
    }

    @Operation(summary = "Update expense by id", description = "Update expense by expenseId for a logged in user")
    @PutMapping("/{expenseId}")
    public Expense updateExpense(@PathVariable Long expenseId, @RequestBody Expense expense) {
        // TODO: fix
        return expenseService.updateExpense(expenseId, expense);
    }
}
