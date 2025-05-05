// Main.java
package main;

import model.ExpenseTrackerModel;
import view.ExpenseTrackerView;
import controller.ExpenseTrackerController;
import javax.swing.UIManager;


public class Main {
    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Create and show the GUI
            ExpenseTrackerModel model = new ExpenseTrackerModel();
            ExpenseTrackerView view = new ExpenseTrackerView();
            new ExpenseTrackerController(model, view);
            
            // Center the window
            view.setLocationRelativeTo(null);
            view.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}