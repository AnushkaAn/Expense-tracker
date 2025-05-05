// ExpenseTrackerModel.java
package model;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ExpenseTrackerModel {
    private List<Expense> expenses;
    private int nextId;
    
    public ExpenseTrackerModel() {
        expenses = new ArrayList<>();
        nextId = 1;
    }
    
    public void addExpense(Expense expense) {
        expense.setId(nextId++);
        expenses.add(expense);
    }
    
    public boolean updateExpense(int id, Category category, double amount, LocalDate date, String description) {
        for (Expense expense : expenses) {
            if (expense.getId() == id) {
                expense.setCategory(category);
                expense.setAmount(amount);
                expense.setDate(date);
                expense.setDescription(description);
                return true;
            }
        }
        return false;
    }
    
    public boolean deleteExpense(int id) {
        return expenses.removeIf(expense -> expense.getId() == id);
    }
    
    public List<Expense> getExpenses() {
        return new ArrayList<>(expenses);
    }
    
    public Map<YearMonth, List<Expense>> getExpensesByMonth() {
        Map<YearMonth, List<Expense>> monthMap = new TreeMap<>();
        for (Expense expense : expenses) {
            YearMonth yearMonth = YearMonth.from(expense.getDate());
            monthMap.computeIfAbsent(yearMonth, k -> new ArrayList<>()).add(expense);
        }
        return monthMap;
    }
    
    public double getTotalExpenses() {
        return expenses.stream().mapToDouble(Expense::getAmount).sum();
    }
    
    public Map<Category, Double> getExpensesByCategory() {
        Map<Category, Double> categoryMap = new HashMap<>();
        for (Expense expense : expenses) {
            categoryMap.merge(expense.getCategory(), expense.getAmount(), Double::sum);
        }
        return categoryMap;
    }
    
    public Map<YearMonth, Double> getMonthlyTotalExpenses() {
        Map<YearMonth, Double> monthlyTotals = new TreeMap<>();
        for (Expense expense : expenses) {
            YearMonth yearMonth = YearMonth.from(expense.getDate());
            monthlyTotals.merge(yearMonth, expense.getAmount(), Double::sum);
        }
        return monthlyTotals;
    }
    
    public Map<YearMonth, Map<Category, Double>> getMonthlyExpensesByCategory() {
        Map<YearMonth, Map<Category, Double>> result = new TreeMap<>();
        for (Expense expense : expenses) {
            YearMonth yearMonth = YearMonth.from(expense.getDate());
            Map<Category, Double> categoryMap = result.computeIfAbsent(yearMonth, k -> new HashMap<>());
            categoryMap.merge(expense.getCategory(), expense.getAmount(), Double::sum);
        }
        return result;
    }
}