package com.mk.demo.service;

import com.mk.demo.entity.Expense;
import com.mk.demo.entity.User;
import com.mk.demo.repository.ExpenseRepository;
import com.mk.demo.request.ExpenseRequest;
import com.mk.demo.response.ExpenseResponse;
import com.mk.demo.util.FindAuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserService userService;
    private final FindAuthenticatedUser findAuthenticatedUser;

    @Override
    @Transactional
    public void addExpense(ExpenseRequest expenseRequest) {

        User user = findAuthenticatedUser.getAuthenticatedUser();
        System.out.println("Authenticate user: " + user.getUsername());

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

//    @Override
//    @Transactional
//    public void deleteById(Long id) {
//        // TODO: check
//
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        User user = userService.findByUsername(username);
//
//        if (id == null || id < 1) {
//            throw new IllegalArgumentException("User id must be greater than 1 and not equal to null");
//        }
//
//        Expense expense = expenseRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Expense with was not found with id: " + id));
//
//        if (!expense.getUser().getId().equals(user.getId())) {
//            throw new RuntimeException("Access denied: cannot edit another user's expense");
//        }
//
//        expenseRepository.deleteById(id);
//    }
//
    @Override
    public ExpenseResponse findById(Long id) {
        // TODO: fix
        // TODO: make sure expense belongs to logged in user
        // make sure id is valid number
        if (id == null || id < 1) {
            throw new IllegalArgumentException("User id must be greater than 1 and not equal to null");
        }

        User user = findAuthenticatedUser.getAuthenticatedUser();

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense with ID " + id + " not found"));

        if (!expense.getUser().getUsername().equals(user.getUsername())) {
            throw new SecurityException("You do not have permission to access this expense");
        }

        return convertToExpenseResponse(expense);
    }
//
    @Override
    public List<ExpenseResponse> findAll() {
        //TODO: fix - not returning anything for some reason

        User user = findAuthenticatedUser.getAuthenticatedUser();
        System.out.println("Authenticated user: " + user.getUsername().toString());

        List<Expense> expenses = expenseRepository.findByUser(user);
        System.out.println("His expenses: " + expenses);

        return expenseRepository.findByUser(user).stream()
                .map(this::convertToExpenseResponse)
                .toList();

//        return expenseRepository.findAll();
    }
//
//    @Override
//    public Expense updateExpense(Long id, Expense expenseDetails) {
//        // TODO: fix
//
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        User user = userService.findByUsername(username);
//
//        Expense expense = expenseRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Expense with was not found with id: " + id));
//
//        if (!expense.getUser().getId().equals(user.getId())) {
//            throw new RuntimeException("Access denied: cannot edit another user's expense");
//        }
//
//        expense.setAmount(expenseDetails.getAmount());
//        expense.setCategory(expenseDetails.getCategory());
//        expense.setDescription(expenseDetails.getDescription());
//        expense.setDate(expenseDetails.getDate());
//
//        return expenseRepository.save(expense);
//    }
//
//    @Override
//    public List<Expense> findAllByDate(Date date) {
//        //TODO: fix and implement
//        return List.of();
//    }

    private ExpenseResponse convertToExpenseResponse(Expense expense) {
        return ExpenseResponse.builder()
                .id(expense.getId())
                .date(expense.getDate())
                .category(expense.getCategory())
                .description(expense.getDescription())
                .build();
    }

}
