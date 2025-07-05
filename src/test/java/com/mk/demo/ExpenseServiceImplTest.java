package com.mk.demo;

import com.mk.demo.entity.Expense;
import com.mk.demo.entity.User;
import com.mk.demo.repository.ExpenseRepository;
import com.mk.demo.response.ExpenseResponse;
import com.mk.demo.service.ExpenseServiceImpl;
import com.mk.demo.util.FindAuthenticatedUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ExpenseServiceImplTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private FindAuthenticatedUser findAuthenticatedUser;

    @InjectMocks
    private ExpenseServiceImpl expenseService;

    private User mockUser;
    private Expense mockExpense;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockUser = new User();
        mockExpense = Expense.builder()
                .id(1L)
                .amount(BigDecimal.valueOf(50.0))
                .category("food")
                .description("lunch")
                .date(LocalDate.of(2025, 7, 2))
                .user(mockUser)
                .build();
    }

    @Test
    void shouldReturnExpensesByCategory() {
        when(findAuthenticatedUser.getAuthenticatedUser()).thenReturn(mockUser);
        when(expenseRepository.findByUserAndCategoryIgnoreCase(mockUser, "food"))
                .thenReturn(List.of(mockExpense));

        List<ExpenseResponse> result = expenseService.findAllByCategory("food");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCategory()).isEqualTo("food");

        verify(expenseRepository).findByUserAndCategoryIgnoreCase(mockUser, "food");
    }

    @Test
    void shouldReturnExpensesByDate() {
        LocalDate date = LocalDate.of(2025, 7, 2);
        when(findAuthenticatedUser.getAuthenticatedUser()).thenReturn(mockUser);
        when(expenseRepository.findByUserAndDate(mockUser, date))
                .thenReturn(List.of(mockExpense));

        List<ExpenseResponse> result = expenseService.findAllByDate(date);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDate()).isEqualTo(date);
    }

    @Test
    void shouldReturnExpensesByCategoryAndDate() {
        LocalDate date = LocalDate.of(2025, 7, 2);
        when(findAuthenticatedUser.getAuthenticatedUser()).thenReturn(mockUser);
        when(expenseRepository.findByUserAndCategoryAndDate(mockUser, "food", date))
                .thenReturn(List.of(mockExpense));

        List<ExpenseResponse> result = expenseService.findAllByCategoryAndDate("food", date);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCategory()).isEqualTo("food");
        assertThat(result.get(0).getDate()).isEqualTo(date);
    }
}
