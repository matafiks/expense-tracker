package com.mk.demo.service;

import com.mk.demo.entity.Expense;
import com.mk.demo.entity.User;
import com.mk.demo.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserService userService;

    @Override
    @Transactional
    public Expense addExpense(Expense expense) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);

        expense.setUser(user);

        return expenseRepository.save(expense);
    }
}
