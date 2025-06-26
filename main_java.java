package main;

import ui.LoginUI;
import util.DBConnection;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Main class to launch the Library Management System application
 */
public class Main {
    
    public static void main(String[] args) {
        // Initialize database on first run
        initializeDatabase();
        
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Launch the application
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginUI().setVisible(true);
            }
        });
    }
    
    /**
     * Initialize database with tables if they don't exist
     */
    private static void initializeDatabase() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Create users table
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT NOT NULL UNIQUE," +
                    "password TEXT NOT NULL," +
                    "role TEXT NOT NULL CHECK (role IN ('admin', 'librarian', 'member'))," +
                    "created_date DATETIME DEFAULT CURRENT_TIMESTAMP)");
            
            // Create books table
            stmt.execute("CREATE TABLE IF NOT EXISTS books (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "title TEXT NOT NULL," +
                    "author TEXT NOT NULL," +
                    "isbn TEXT NOT NULL UNIQUE," +
                    "available INTEGER DEFAULT 1," +
                    "created_date DATETIME DEFAULT CURRENT_TIMESTAMP)");
            
            // Create members table
            stmt.execute("CREATE TABLE IF NOT EXISTS members (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "email TEXT NOT NULL UNIQUE," +
                    "phone TEXT," +
                    "address TEXT," +
                    "membership_date DATETIME DEFAULT CURRENT_TIMESTAMP)");
            
            // Create loans table
            stmt.execute("CREATE TABLE IF NOT EXISTS loans (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "book_id INTEGER NOT NULL," +
                    "member_id INTEGER NOT NULL," +
                    "loan_date DATE NOT NULL," +
                    "due_date DATE NOT NULL," +
                    "return_date DATE," +
                    "status TEXT DEFAULT 'active' CHECK (status IN ('active', 'returned', 'overdue'))," +
                    "FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE," +
                    "FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE)");
            
            System.out.println("Database initialized successfully!");
            
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}