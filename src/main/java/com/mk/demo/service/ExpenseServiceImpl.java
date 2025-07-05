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

import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final FindAuthenticatedUser findAuthenticatedUser;

    /**
     * Dodanie nowego wydatku w formie ExpenseRequest przez zalogowanego użytkownika
     * @param expenseRequest
     * @return Zwraca dane podane przez użytkownika oraz potwierdzenie
     */
    @Override
    @Transactional
    public ExpenseResponse addExpense(ExpenseRequest expenseRequest) {

        User user = findAuthenticatedUser.getAuthenticatedUser();

        Expense newExpense = Expense.builder()
                .date(expenseRequest.getDate())
                .amount(expenseRequest.getAmount())
                .category(expenseRequest.getCategory())
                .description(expenseRequest.getDescription())
                .user(user)
                .build();

        expenseRepository.save(newExpense);
        return convertToExpenseResponse(newExpense);
    }

    /**
     * Usunięcie dodatku po jego id z bazy danych przez zalogowanego użytkownika
     * @param id
     */
    @Override
    @Transactional
    public void deleteById(Long id) {

        User user = findAuthenticatedUser.getAuthenticatedUser();

        Expense expense = expenseRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense not found"));

        expenseRepository.delete(expense);
    }

    /**
     * Wyszukiwanie wydatku po id przez zalogowanego użytkownika
     * @param id
     * @return Zwraca wskazany wydatek
     */
    @Override
    public ExpenseResponse findById(Long id) {

        User user = findAuthenticatedUser.getAuthenticatedUser();

        Expense expense = expenseRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense not found"));

        return convertToExpenseResponse(expense);
    }

    /**
     * Wyszukiwanie wszystkich wydatków zalogowanego użytkownika
     * @return Zwraca listę wydatków użytkownika
     */
    @Override
    public List<ExpenseResponse> findAll() {

        User user = findAuthenticatedUser.getAuthenticatedUser();

        return expenseRepository.findByUser(user).stream()
                .map(this::convertToExpenseResponse)
                .toList();
    }

    /**
     * Wyszukiwanie wszystkich wydatków zalogowanego użytkownika (filtrowanie po kategorii)
     * @param category
     * @return Zwraca listę wydatków użytkownika (tylko z konkretnej kategorii)
     */
    @Override
    public List<ExpenseResponse> findAllByCategory(String category) {

        User user = findAuthenticatedUser.getAuthenticatedUser();

        return expenseRepository.findByUserAndCategoryIgnoreCase(user, category).stream()
                .map(this::convertToExpenseResponse)
                .toList();
    }

    /**
     * Wyszukiwanie wszystkich wydatków zalogowanego użytkownika (filtrowanie po dacie)
     * @param localDate
     * @return Zwraca listę wydatków użytkownika (tylko o konkretnej dacie)
     */
    @Override
    public List<ExpenseResponse> findAllByDate(LocalDate localDate) {

        User user = findAuthenticatedUser.getAuthenticatedUser();

        return expenseRepository.findByUserAndDate(user, localDate).stream()
                .map(this::convertToExpenseResponse)
                .toList();
    }

    /**
     * Wyszukiwanie wszystkich wydatków zalogowanego użytkownika (filtrowanie po kategorii oraz dacie)
     * @param category
     * @param localDate
     * @return Zwraca listę wydatków użytkownika (tylko ze wskazanej kategorii oraz konkretnej dacie)
     */
    public List<ExpenseResponse> findAllByCategoryAndDate(String category, LocalDate localDate) {

        User user = findAuthenticatedUser.getAuthenticatedUser();

        return expenseRepository.findByUserAndCategoryAndDate(user, category, localDate).stream()
                .map(this::convertToExpenseResponse)
                .toList();
    }

    /**
     * Edycja wskazanego przez id wydatku
     * @param id
     * @param expenseRequest
     * @return Zwraca dane edytowanego wydatku
     */
    @Override
    @Transactional
    public ExpenseResponse updateExpense(Long id, ExpenseRequest expenseRequest) {

        User user = findAuthenticatedUser.getAuthenticatedUser();

        Expense expense = expenseRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense not found"));

        expense.setAmount(expenseRequest.getAmount());
        expense.setCategory(expenseRequest.getCategory());
        expense.setDescription(expenseRequest.getDescription());
        expense.setDate(expenseRequest.getDate());

        expenseRepository.save(expense);
        
        return convertToExpenseResponse(expense);
    }

    /**
     * Metoda pomocnicza do konwersji encji Expense na ExpenseResponse (ukrycie niektórych pól)
     * @param expense
     * @return Zwraca ExpenseResponse
     */
    private ExpenseResponse convertToExpenseResponse(Expense expense) {
        return ExpenseResponse.builder()
                .id(expense.getId())
                .date(expense.getDate())
                .amount(expense.getAmount())
                .category(expense.getCategory())
                .description(expense.getDescription())
                .build();
    }

}
