package com.mk.demo.repository;

import com.mk.demo.entity.Expense;
import com.mk.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUser(User user);
    Optional<Expense> findByIdAndUser(Long id, User user);

    List<Expense> findByUserAndCategoryIgnoreCase(User user, String category);

    List<Expense> findByUserAndDate(User user, LocalDate date);
}
