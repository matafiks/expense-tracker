package com.mk.demo.controller;


import com.mk.demo.entity.Expense;
import com.mk.demo.request.ExpenseRequest;
import com.mk.demo.response.ExpenseResponse;
import com.mk.demo.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Expense REST API endpoints", description = "Operations related to expensions")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @Operation(summary = "Create new expense", description = "Create new expense for a logged in user")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<String> addExpense(@Valid @RequestBody ExpenseRequest expenseRequest) {
        expenseService.addExpense(expenseRequest);
        return ResponseEntity.ok("Expense added successfully");
    }

//    @Operation(summary = "Delete an expense", description = "Delete an expense based on expenseId")
//    @DeleteMapping("/{expenseId}")
//    public void deleteExpenseById(@PathVariable Long expenseId) {
//        // TODO: check
//        expenseService.deleteById(expenseId);
//    }
//
    @Operation(summary = "Find an expense by id", description = "Fetch an expense based on expenseId")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{expenseId}")
    public ResponseEntity<ExpenseResponse> findById(@PathVariable /*@Min(value = 1)*/ Long expenseId) {
        // TODO: fix
        return ResponseEntity.ok(expenseService.findById(expenseId));
    }
//
    @Operation(summary = "List all expenses", description = "Fetch a list of all expenses for a logged in user")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<ExpenseResponse> findAllExpenses() {
        return expenseService.findAll();
    }
//
//    @Operation(summary = "Update expense by id", description = "Update expense by expenseId for a logged in user")
//    @PutMapping("/{expenseId}")
//    public Expense updateExpense(@PathVariable Long expenseId, @RequestBody Expense expense) {
//        // TODO: fix
//        return expenseService.updateExpense(expenseId, expense);
//    }
}
