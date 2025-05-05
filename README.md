**Expense Tracker Pro**

**Overview**
Expense Tracker Pro is a Java Swing application that helps users track their expenses with a clean, modern interface. It provides features for adding, editing, and deleting expenses, as well as visualizing spending patterns through charts and monthly breakdowns.

**Features**
- Expense Management: Add, edit, and delete expenses with category, amount, date, and description
- Visual Analytics: 
  - Pie chart showing expenses by category
  - Bar chart showing monthly spending trends
- Monthly View: Breakdown of expenses by month
- Responsive UI: Modern, clean interface with intuitive controls
- Data Persistence: All expenses are stored in memory while the application runs

 **Technologies Used**
- Java 8+
- Java Swing for GUI
- JFreeChart (jfreechart-1.5.3.jar) for chart visualizations
- JCommon (jcommon-1.0.23.jar) - dependency for JFreeChart

**Installation**
1. Ensure you have Java 8 or later installed
2. Clone this repository
3. Download the required JAR files:
   - [jfreechart-1.5.3.jar](https://www.jfree.org/jfreechart/)
   - [jcommon-1.0.23.jar](https://www.jfree.org/jcommon/)
4. Add the JAR files to your project's classpath

**Running the Application**
1. Compile all Java files:
   ```
   javac -cp .;jfreechart-1.5.3.jar;jcommon-1.0.23.jar main/Main.java
   ```
2. Run the application:
   ```
   java -cp .;jfreechart-1.5.3.jar;jcommon-1.0.23.jar main.Main
   ```
   (Use colons instead of semicolons for Linux/Mac)

 Usage
1. Adding an Expense:
   - Select a category from the dropdown
   - Enter the amount, date (YYYY-MM-DD), and description
   - Click "Add Expense"

2. Editing an Expense:
   - Select an expense from the table
   - Modify the fields as needed
   - Click "Edit Expense"

3. Deleting an Expense:
   - Select an expense from the table
   - Click "Delete Expense"

4. Viewing Charts:
   - Navigate to the "Analytics" tab
   - Use the buttons at the bottom to switch between pie and bar charts
   - Select a month from the dropdown to filter the pie chart data

**Project Structure**
```
src/
├── main/
│   └── Main.java                - Application entry point
├── model/
│   ├── Category.java            - Enum for expense categories
│   ├── Expense.java             - Expense data model
│   └── ExpenseTrackerModel.java - Business logic and data management
├── view/
│   └── ExpenseTrackerView.java  - GUI implementation
└── controller/
    └── ExpenseTrackerController.java - Mediates between view and model
```

 **Dependencies**
- JFreeChart 1.5.3 - For chart visualizations
- JCommon 1.0.23 - Required by JFreeChart

 **License**
This project is open-source and available under the [MIT License](LICENSE).

 **Future Enhancements**
- Add database persistence
- Implement user accounts
- Add export functionality (CSV, PDF)
- More advanced chart types and filtering options

 **Contributing**
Contributions are welcome! Please fork the repository and submit a pull request with your changes.

 **Acknowledgments**
- JFreeChart team for the excellent charting library
- Java Swing for providing the GUI framework
