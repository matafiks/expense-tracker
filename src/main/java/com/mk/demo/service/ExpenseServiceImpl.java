package com.mk.demo.service;

import com.mk.demo.entity.Expense;
import com.mk.demo.entity.User;
import com.mk.demo.repository.ExpenseRepository;
import com.mk.demo.request.ExpenseRequest;
import com.mk.demo.response.ExpenseResponse;
import com.mk.demo.util.FindAuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final FindAuthenticatedUser findAuthenticatedUser;

    @Override
    @Transactional
    public void addExpense(ExpenseRequest expenseRequest) {

        User user = findAuthenticatedUser.getAuthenticatedUser();

        Expense newExpense = Expense.builder()
                .date(expenseRequest.getDate())
                .amount(expenseRequest.getAmount())
                .category(expenseRequest.getCategory())
                .description(expenseRequest.getDescription())
                .user(user)
                .build();

        System.out.println("New expense: " + newExpense);

        expenseRepository.save(newExpense);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {

        User user = findAuthenticatedUser.getAuthenticatedUser();

        //TODO: replace with findByIdAndUser
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense with was not found with id: " + id));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied: cannot edit another user's expense");
        }

        expenseRepository.deleteById(id);
    }

    @Override
    public ExpenseResponse findById(Long id) {
        User user = findAuthenticatedUser.getAuthenticatedUser();

        //TODO: replace with findByIdAndUser
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense with ID " + id + " not found"));

        if (!expense.getUser().getUsername().equals(user.getUsername())) {
            throw new SecurityException("You do not have permission to access this expense");
        }

        return convertToExpenseResponse(expense);
    }

    @Override
    public List<ExpenseResponse> findAll() {

        User user = findAuthenticatedUser.getAuthenticatedUser();

        return expenseRepository.findByUser(user).stream()
                .map(this::convertToExpenseResponse)
                .toList();
    }

    @Override
    @Transactional
    public ExpenseResponse updateExpense(Long id, ExpenseRequest expenseRequest) {

        User user = findAuthenticatedUser.getAuthenticatedUser();

        Optional<Expense> expense = expenseRepository.findByIdAndUser(id, user);

        if (expense.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense not found");
        }

        expense.get().setAmount(expenseRequest.getAmount());
        expense.get().setCategory(expenseRequest.getCategory());
        expense.get().setDescription(expenseRequest.getDescription());
        expense.get().setDate(expenseRequest.getDate());

        Expense savedExpense = expenseRepository.save(expense.get());
        
        return convertToExpenseResponse(savedExpense);
    }

    private ExpenseResponse convertToExpenseResponse(Expense expense) {
        return ExpenseResponse.builder()
                .id(expense.getId())
                .date(expense.getDate())
                .category(expense.getCategory())
                .description(expense.getDescription())
                .build();
    }

}
