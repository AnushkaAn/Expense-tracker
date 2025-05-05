// ExpenseTrackerController.java
package controller;

import model.*;
import view.ExpenseTrackerView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

import javax.swing.table.DefaultTableModel;
import java.util.HashMap;
import java.util.List;

public class ExpenseTrackerController {
    private ExpenseTrackerModel model;
    private ExpenseTrackerView view;
    
    public ExpenseTrackerController(ExpenseTrackerModel model, ExpenseTrackerView view) {
        this.model = model;
        this.view = view;
        
        // Add action listeners
        view.addAddExpenseListener(new AddExpenseListener());
        view.addEditExpenseListener(new EditExpenseListener());
        view.addDeleteExpenseListener(new DeleteExpenseListener());
        view.addShowPieChartListener(new ShowPieChartListener());
        view.addShowBarChartListener(new ShowBarChartListener());
        view.addMonthSelectionListener(new MonthSelectionListener());
        
        updateView();
    }
    
    private void updateView() {
        updateExpenseTable();
        updateMonthlyTables();
        updateCharts(null);
        view.updateTotal(model.getTotalExpenses()); // Add this line
    }
    
    private void updateExpenseTable() {
        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"ID", "Category", "Amount", "Date", "Description"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        for (Expense expense : model.getExpenses()) {
            tableModel.addRow(new Object[]{
                expense.getId(),
                expense.getCategory(),
                String.format("$%.2f", expense.getAmount()),
                expense.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                expense.getDescription()
            });
        }
        view.updateExpenseTable(tableModel);
    }
    
    private void updateMonthlyTables() {
        Map<YearMonth, List<Expense>> monthlyExpenses = model.getExpensesByMonth();
        view.updateMonthlyTables(monthlyExpenses);
    }
    
    private void updateCharts(YearMonth selectedMonth) {
        if (view.isPieChartVisible()) {
            updatePieChart(selectedMonth);
        }
        if (view.isBarChartVisible()) {
            updateBarChart();
        }
    }
    
    private void updatePieChart(YearMonth selectedMonth) {
        Map<Category, Double> data;
        if (selectedMonth == null) {
            data = model.getExpensesByCategory();
        } else {
            data = model.getMonthlyExpensesByCategory().getOrDefault(selectedMonth, new HashMap<>());
        }
        view.updatePieChart(data);
    }
    
    private void updateBarChart() {
        Map<YearMonth, Double> monthlyTotals = model.getMonthlyTotalExpenses();
        view.updateBarChart(monthlyTotals);
    }
    
    class AddExpenseListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                Category category = view.getSelectedCategory();
                double amount = Double.parseDouble(view.getAmountText());
                if (amount <= 0) {
                    throw new IllegalArgumentException("Amount must be positive");
                }
                
                LocalDate date = LocalDate.parse(view.getDateText(), DateTimeFormatter.ISO_LOCAL_DATE);
                String description = view.getDescriptionText();
                
                if (description.isEmpty()) {
                    throw new IllegalArgumentException("Description cannot be empty");
                }
                
                Expense expense = new Expense(0, category, amount, date, description);
                model.addExpense(expense);
                
                updateView();
                view.clearInputFields();
            } catch (NumberFormatException ex) {
                view.showError("Invalid amount format. Please enter a valid number.");
            } catch (DateTimeParseException ex) {
                view.showError("Invalid date format. Please use YYYY-MM-DD.");
            } catch (IllegalArgumentException ex) {
                view.showError(ex.getMessage());
            }
        }
    }
    
    class EditExpenseListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int selectedRow = view.getSelectedExpenseRow();
                if (selectedRow == -1) {
                    view.showError("Please select an expense to edit");
                    return;
                }
                
                DefaultTableModel model = view.getExpenseTableModel();
                int id = (int) model.getValueAt(selectedRow, 0);
                
                // Get the expense from the model
                Expense expenseToEdit = null;
                for (Expense expense : ExpenseTrackerController.this.model.getExpenses()) {
                    if (expense.getId() == id) {
                        expenseToEdit = expense;
                        break;
                    }
                }
                
                if (expenseToEdit != null) {
                    // Use the new setter methods to populate the fields
                    view.setSelectedCategory(expenseToEdit.getCategory());
                    view.setAmountText(String.valueOf(expenseToEdit.getAmount()));
                    view.setDateText(expenseToEdit.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
                    view.setDescriptionText(expenseToEdit.getDescription());
                } else {
                    view.showError("Expense not found");
                }
            } catch (Exception ex) {
                view.showError("Error selecting expense: " + ex.getMessage());
            }
        }
    }
    
    class DeleteExpenseListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int selectedRow = view.getSelectedExpenseRow();
                if (selectedRow == -1) {
                    view.showError("Please select an expense to delete");
                    return;
                }
                
                DefaultTableModel model = view.getExpenseTableModel();
                int id = (int) model.getValueAt(selectedRow, 0);
                
                if (ExpenseTrackerController.this.model.deleteExpense(id)) {
                    updateView();
                    view.clearInputFields();
                } else {
                    view.showError("Failed to delete expense");
                }
            } catch (Exception ex) {
                view.showError("Error deleting expense: " + ex.getMessage());
            }
        }
    }
    
    class ShowPieChartListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.showPieChart();
            updatePieChart(view.getSelectedMonth());
        }
    }
    
    class ShowBarChartListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.showBarChart();
            updateBarChart();
        }
    }
    
    class MonthSelectionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            YearMonth selectedMonth = view.getSelectedMonth();
            if (view.isPieChartVisible()) {
                updatePieChart(selectedMonth);
            }
        }
    }
}