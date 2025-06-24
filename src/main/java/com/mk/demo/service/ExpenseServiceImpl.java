package com.mk.demo.service;

import com.mk.demo.entity.Expense;
import com.mk.demo.entity.User;
import com.mk.demo.repository.ExpenseRepository;
import com.mk.demo.request.ExpenseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserService userService;

    @Override
    @Transactional
    public void addExpense(ExpenseRequest expenseRequest) {
        // TODO: fix

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);

        expense.setUser(user);

        expenseRepository.save(expense);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        // TODO: check

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);

        if (id == null || id < 1) {
            throw new IllegalArgumentException("User id must be greater than 1 and not equal to null");
        }

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense with was not found with id: " + id));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied: cannot edit another user's expense");
        }

        expenseRepository.deleteById(id);
    }

    @Override
    public Expense findById(Long id) {
        // TODO: fix
        // TODO: make sure expense belongs to logged in user
        // make sure id is valid number
        if (id == null || id < 1) {
            throw new IllegalArgumentException("User id must be greater than 1 and not equal to null");
        }

        return expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Did not find expense with id: " + id));
    }

    @Override
    public List<Expense> findAll() {
        //TODO: return all expenses only for logged in user
        return expenseRepository.findAll();
    }

    @Override
    public Expense updateExpense(Long id, Expense expenseDetails) {
        // TODO: fix

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense with was not found with id: " + id));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied: cannot edit another user's expense");
        }

        expense.setAmount(expenseDetails.getAmount());
        expense.setCategory(expenseDetails.getCategory());
        expense.setDescription(expenseDetails.getDescription());
        expense.setDate(expenseDetails.getDate());

        return expenseRepository.save(expense);
    }

    @Override
    public List<Expense> findAllByDate(Date date) {
        //TODO: fix and implement
        return List.of();
    }
}
