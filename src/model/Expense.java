// Expense.java
package model;

import java.time.LocalDate;

public class Expense {
    private int id;
    private Category category;
    private double amount;
    private LocalDate date;
    private String description;
    
    public Expense(int id, Category category, double amount, LocalDate date, String description) {
        this.id = id;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }
    
    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    @Override
    public String toString() {
        return String.format("%s - $%.2f - %s - %s", 
                category, amount, date, description);
    }
}