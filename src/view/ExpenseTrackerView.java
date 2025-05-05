// ExpenseTrackerView.java
package view;

import model.Category;
import model.Expense;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class ExpenseTrackerView extends JFrame {
    // Colors
    private final Color MAIN_BG = new Color(245, 245, 250);
    private final Color CARD_BG = new Color(255, 255, 255);
    private final Color PRIMARY = new Color(101, 87, 255);
    private final Color SECONDARY = new Color(255, 107, 107);
    private final Color ACCENT = new Color(72, 219, 251);
    private final Color TERTIARY = new Color(0,0,0);
    
    // Input components
    private JComboBox<Category> categoryComboBox;
    private JTextField amountField;
    private JTextField dateField;
    private JTextField descriptionField;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    
    // Table components
    private JTable expenseTable;
    private JTabbedPane tabbedPane;
    
    // Chart components
    private JComboBox<YearMonth> monthComboBox;
    private ChartPanel pieChartPanel;
    private ChartPanel barChartPanel;
    private JPanel chartsPanel;
    private CardLayout chartsLayout;
    private boolean pieChartVisible = false;
    private boolean barChartVisible = false;

    private JButton showPieChartButton;
    private JButton showBarChartButton;
    
    public ExpenseTrackerView() {
        setTitle("💰 Expense Tracker Pro");
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(MAIN_BG);
        
        // Create header
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Create main content area
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(MAIN_BG);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        // Create input panel
        JPanel inputPanel = createInputPanel();
        contentPanel.add(inputPanel, BorderLayout.NORTH);
        
        // Create main tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(MAIN_BG);
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        
        // All expenses tab
        JPanel allExpensesPanel = new JPanel(new BorderLayout());
        allExpensesPanel.setBackground(CARD_BG);
        allExpensesPanel.setBorder(createCardBorder());
        
        expenseTable = new JTable();
        expenseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        expenseTable.setFillsViewportHeight(true);
        expenseTable.setRowHeight(30);
        expenseTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JScrollPane tableScrollPane = new JScrollPane(expenseTable);
        tableScrollPane.setBorder(null);
        allExpensesPanel.add(tableScrollPane, BorderLayout.CENTER);
        tabbedPane.addTab("📋 All Expenses", createTabIcon(Color.BLUE), allExpensesPanel);
        
        // Monthly expenses tab
        JPanel monthlyExpensesPanel = new JPanel(new BorderLayout());
        monthlyExpensesPanel.setBackground(CARD_BG);
        monthlyExpensesPanel.setBorder(createCardBorder());
        
        JTabbedPane monthlyTabs = new JTabbedPane();
        monthlyTabs.setBackground(MAIN_BG);
        monthlyExpensesPanel.add(monthlyTabs, BorderLayout.CENTER);
        tabbedPane.addTab("📅 Monthly View", createTabIcon(Color.ORANGE), monthlyExpensesPanel);
        
        // Charts panel
        chartsPanel = new JPanel();
        chartsPanel.setBackground(CARD_BG);
        chartsPanel.setBorder(createCardBorder());
        chartsLayout = new CardLayout();
        chartsPanel.setLayout(chartsLayout);
        
        // Initialize charts
        pieChartPanel = createPieChartPanel(new DefaultPieDataset());
        barChartPanel = createBarChartPanel(new DefaultCategoryDataset());
        
        chartsPanel.add(pieChartPanel, "Pie");
        chartsPanel.add(barChartPanel, "Bar");
        tabbedPane.addTab("📊 Analytics", createTabIcon(Color.GREEN), chartsPanel);
        
        contentPanel.add(tabbedPane, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);
        
        // Bottom panel with month selection
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Set application icon
        setIconImage(new ImageIcon(getClass().getResource("/icons/app_icon.png")).getImage());
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRIMARY);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("Expense Tracker Pro");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        
        JLabel totalLabel = new JLabel("Total: $0.00");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        totalLabel.setForeground(Color.WHITE);
        rightPanel.add(totalLabel);
        
        panel.add(titleLabel, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(CARD_BG);
        panel.setBorder(createCardBorder());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Category
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(createInputLabel("Category:"), gbc);
        
        gbc.gridx = 1;
        categoryComboBox = new JComboBox<>(Category.values());
        categoryComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, 
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value != null) {
                    setText(value.toString());
                    setIcon(getCategoryIcon((Category) value));
                }
                return this;
            }
        });
        categoryComboBox.setPreferredSize(new Dimension(200, 35));
        panel.add(categoryComboBox, gbc);
        
        // Amount
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(createInputLabel("Amount ($):"), gbc);
        
        gbc.gridx = 1;
        amountField = new JTextField();
        amountField.setPreferredSize(new Dimension(180, 30));
        panel.add(amountField, gbc);
        
        // Date
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(createInputLabel("Date (YYYY-MM-DD):"), gbc);
        
        gbc.gridx = 1;
        dateField = new JTextField();
        dateField.setPreferredSize(new Dimension(180, 30));
        panel.add(dateField, gbc);
        
        // Description
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(createInputLabel("Description:"), gbc);
        
        gbc.gridx = 1;
        descriptionField = new JTextField();
        descriptionField.setPreferredSize(new Dimension(180, 30));
        panel.add(descriptionField, gbc);
        
        // Buttons
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);
        
        addButton = createPrimaryButton("➕ Add Expense");
        editButton = createSecondaryButton("✏️ Edit Expense");
        deleteButton = createDangerButton("❌ Delete Expense");
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        
        panel.add(buttonPanel, gbc);
        
        return panel;
    }
    
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(MAIN_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));
        
        // Month selection
        JLabel monthLabel = new JLabel("Select Month:");
        monthLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        monthComboBox = new JComboBox<>();
        monthComboBox.setPreferredSize(new Dimension(180, 35));
        monthComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, 
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    setText("All Time");
                }
                return this;
            }
        });
        
        // Chart buttons
        showPieChartButton = createChartButton("🍕 Pie Chart", TERTIARY); // Changed to use class field
        showBarChartButton = createChartButton("📊 Bar Chart", TERTIARY); // Changed to use class field
        
        panel.add(monthLabel);
        panel.add(monthComboBox);
        panel.add(Box.createHorizontalStrut(30));
        panel.add(showPieChartButton);
        panel.add(showBarChartButton);
        
        return panel;
    }
    
    private ChartPanel createPieChartPanel(DefaultPieDataset dataset) {
        JFreeChart chart = ChartFactory.createPieChart(
            "Expenses by Category", 
            dataset, 
            true, true, false);
        
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionOutlinesVisible(false);
        plot.setBackgroundPaint(CARD_BG);
        plot.setLabelGenerator(null);
        chart.setBackgroundPaint(CARD_BG);
        
        // Custom colors for pie chart
        plot.setSectionPaint(0, new Color(101, 87, 255));
        plot.setSectionPaint(1, new Color(255, 107, 107));
        plot.setSectionPaint(2, new Color(72, 219, 251));
        plot.setSectionPaint(3, new Color(255, 159, 67));
        plot.setSectionPaint(4, new Color(46, 204, 113));
        
        
        ChartPanel chartPanel = new ChartPanel(chart) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(900, 650);
            }
        };
        chartPanel.setBackground(CARD_BG);
        return chartPanel;
    }
    
    private ChartPanel createBarChartPanel(DefaultCategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createBarChart(
            "Monthly Expenses", 
            "Month", 
            "Amount ($)", 
            dataset);
        
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, PRIMARY);
        
        plot.setBackgroundPaint(CARD_BG);
        plot.setRangeGridlinePaint(new Color(230, 230, 230));
        chart.setBackgroundPaint(CARD_BG);
        
        ChartPanel chartPanel = new ChartPanel(chart) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(900, 650);
            }
        };
        chartPanel.setBackground(CARD_BG);
        return chartPanel;
    }
    
    private JLabel createInputLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return label;
    }
    
    private JButton createPrimaryButton(String text) {
        return createStyledButton(text, PRIMARY, Color.BLACK);
    }
    
    private JButton createSecondaryButton(String text) {
        return createStyledButton(text, new Color(230, 230, 230), Color.DARK_GRAY);
    }
    
    private JButton createDangerButton(String text) {
        return createStyledButton(text, SECONDARY, Color.BLACK);
    }
    
    private JButton createChartButton(String text, Color bgColor) {
        JButton button = createStyledButton(text, bgColor, Color.BLACK);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(150, 40));
        return button;
    }
    
    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setBackground(TERTIARY);
        button.setForeground(fgColor);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(bgColor.darker(), 1),
            new EmptyBorder(8, 15, 8, 15)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    private Border createCardBorder() {
    return BorderFactory.createCompoundBorder(
        new LineBorder(new Color(230, 230, 230), 1), // outer border
        new EmptyBorder(15, 15, 15, 15)              // inner padding
    );
}

    private Icon createTabIcon(Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                g.setColor(color);
                g.fillOval(x, y, 10, 10);
            }
            
            @Override
            public int getIconWidth() { return 0; }
            @Override
            public int getIconHeight() { return 0; }
        };
    }
    
    private Icon getCategoryIcon(Category category) {
        // In a real app, you would use actual icons here
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                g.setColor(PRIMARY);
                g.fillOval(x, y, 12, 12);
            }
            
            @Override
            public int getIconWidth() { return 16; }
            @Override
            public int getIconHeight() { return 16; }
        };
    }
    
    // Getters for input fields
    public Category getSelectedCategory() {
        return (Category) categoryComboBox.getSelectedItem();
    }
    
    public String getAmountText() {
        return amountField.getText();
    }
    
    public String getDateText() {
        return dateField.getText();
    }
    
    public String getDescriptionText() {
        return descriptionField.getText();
    }
    
    // Table methods
    public int getSelectedExpenseRow() {
        return expenseTable.getSelectedRow();
    }
    
    public DefaultTableModel getExpenseTableModel() {
        return (DefaultTableModel) expenseTable.getModel();
    }
    
    public void updateExpenseTable(DefaultTableModel model) {
        expenseTable.setModel(model);
        expenseTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (column == 2) { // Amount column
                    setForeground(new Color(0, 128, 0));
                    setFont(getFont().deriveFont(Font.BOLD));
                } else {
                    setForeground(Color.DARK_GRAY);
                }
                
                if (isSelected) {
                    setBackground(new Color(220, 240, 255));
                } else {
                    setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
                }
                
                return c;
            }
        });
    }
    
    public void updateMonthlyTables(Map<YearMonth, List<Expense>> monthlyExpenses) {
        JTabbedPane monthlyTabs = (JTabbedPane) ((JPanel) tabbedPane.getComponentAt(1)).getComponent(0);
        monthlyTabs.removeAll();
        
        monthComboBox.removeAllItems();
        monthComboBox.addItem(null); // For "All time" selection
        
        for (YearMonth month : monthlyExpenses.keySet()) {
            DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Category", "Amount", "Date", "Description"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            
            for (Expense expense : monthlyExpenses.get(month)) {
                model.addRow(new Object[]{
                    expense.getId(),
                    expense.getCategory(),
                    String.format("$%.2f", expense.getAmount()),
                    expense.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                    expense.getDescription()
                });
            }
            
            JTable table = new JTable(model);
            table.setRowHeight(30);
            table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(null);
            
            monthlyTabs.addTab(month.toString(), scrollPane);
            monthComboBox.addItem(month);
        }
    }
    
    // Chart methods
    public void updatePieChart(Map<Category, Double> data) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        if (data != null) {
            data.forEach((category, amount) -> dataset.setValue(category, amount));
        }
        ((PiePlot) pieChartPanel.getChart().getPlot()).setDataset(dataset);
    }
    
    public void updateBarChart(Map<YearMonth, Double> monthlyTotals) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        if (monthlyTotals != null) {
            monthlyTotals.forEach((month, total) -> {
                dataset.addValue(total, "Total", month.toString());
            });
        }
        ((CategoryPlot) barChartPanel.getChart().getPlot()).setDataset(dataset);
    }
    
    public void showPieChart() {
        chartsLayout.show(chartsPanel, "Pie");
        tabbedPane.setSelectedIndex(2);
        pieChartVisible = true;
        barChartVisible = false;
    }
    
    public void showBarChart() {
        chartsLayout.show(chartsPanel, "Bar");
        tabbedPane.setSelectedIndex(2);
        pieChartVisible = false;
        barChartVisible = true;
    }

    public void updateTotal(double total) {
        JPanel headerPanel = (JPanel) getContentPane().getComponent(0);
        JPanel rightPanel = (JPanel) headerPanel.getComponent(1);
        JLabel totalLabel = (JLabel) rightPanel.getComponent(0);
        totalLabel.setText(String.format("Total: $%.2f", total));
    }
    
    public boolean isPieChartVisible() {
        return pieChartVisible;
    }
    
    public boolean isBarChartVisible() {
        return barChartVisible;
    }
    
    public YearMonth getSelectedMonth() {
        return (YearMonth) monthComboBox.getSelectedItem();
    }
    
    // Button action listeners
    public void addAddExpenseListener(ActionListener listener) {
        addButton.addActionListener(listener);
    }
    
    public void addEditExpenseListener(ActionListener listener) {
        editButton.addActionListener(listener);
    }
    
    public void addDeleteExpenseListener(ActionListener listener) {
        deleteButton.addActionListener(listener);
    }
    
    public void addShowPieChartListener(ActionListener listener) {
        for (ActionListener al : showPieChartButton.getActionListeners()) {
            showPieChartButton.removeActionListener(al);
        }
        showPieChartButton.addActionListener(listener);
    }
    
    public void addShowBarChartListener(ActionListener listener) {
        for (ActionListener al : showBarChartButton.getActionListeners()) {
            showBarChartButton.removeActionListener(al);
        }
        showBarChartButton.addActionListener(listener);
    }
    
    public void addMonthSelectionListener(ActionListener listener) {
        monthComboBox.addActionListener(listener);
    }
    
    // Utility methods
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public void clearInputFields() {
        amountField.setText("");
        dateField.setText("");
        descriptionField.setText("");
    }
    // Add these methods to ExpenseTrackerView.java
public void setSelectedCategory(Category category) {
    categoryComboBox.setSelectedItem(category);
}

public void setAmountText(String amount) {
    amountField.setText(amount);
}

public void setDateText(String date) {
    dateField.setText(date);
}

public void setDescriptionText(String description) {
    descriptionField.setText(description);
}
}