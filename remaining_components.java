// Loan.java - Missing model class for loan operations
package model;

import java.time.LocalDate;

public class Loan {
    private int id;
    private int bookId;
    private int memberId;
    private LocalDate loanDate;
    private LocalDate returnDate;
    
    // Additional fields for joined data from reports
    private String bookTitle;
    private String bookAuthor;
    private String memberName;
    private String memberEmail;

    // Default constructor
    public Loan() {}

    // Constructor with essential fields
    public Loan(int bookId, int memberId, LocalDate loanDate) {
        this.bookId = bookId;
        this.memberId = memberId;
        this.loanDate = loanDate;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }

    public boolean isActive() {
        return returnDate == null;
    }

    public boolean isOverdue(int loanPeriodDays) {
        if (returnDate != null) return false;
        return loanDate.plusDays(loanPeriodDays).isBefore(LocalDate.now());
    }

    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", bookId=" + bookId +
                ", memberId=" + memberId +
                ", loanDate=" + loanDate +
                ", returnDate=" + returnDate +
                ", bookTitle='" + bookTitle + '\'' +
                ", memberName='" + memberName + '\'' +
                '}';
    }
}

// Complete User.java with all getters and setters
package model;

public class User {
    private int id;
    private String username;
    private String password;
    private String role;

    // Default constructor
    public User() {}

    // Constructor
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(role);
    }

    public boolean isLibrarian() {
        return "librarian".equalsIgnoreCase(role);
    }

    public boolean isMember() {
        return "member".equalsIgnoreCase(role);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}

// Complete Book.java with all getters and setters
package model;

public class Book {
    private int id;
    private String title;
    private String author;
    private String isbn;
    private boolean available;

    // Default constructor
    public Book() {
        this.available = true; // Default to available when created
    }

    // Constructor
    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.available = true;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                ", available=" + available +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Book book = (Book) obj;
        return isbn != null ? isbn.equals(book.isbn) : book.isbn == null;
    }

    @Override
    public int hashCode() {
        return isbn != null ? isbn.hashCode() : 0;
    }
}

// Complete Member.java with all getters and setters
package model;

public class Member {
    private int id;
    private String name;
    private String email;

    // Default constructor
    public Member() {}

    // Constructor
    public Member(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Member member = (Member) obj;
        return email != null ? email.equals(member.email) : member.email == null;
    }

    @Override
    public int hashCode() {
        return email != null ? email.hashCode() : 0;
    }
}

// DatabaseInitializer.java - Utility to initialize database with sample data
package util;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseInitializer {

    public static void initializeDatabase() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // Create tables if they don't exist
            createTables(stmt);
            
            // Insert sample data
            insertSampleData(stmt);
            
            System.out.println("Database initialized successfully!");
            
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createTables(Statement stmt) throws SQLException {
        // Users table
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL,
                role TEXT NOT NULL CHECK (role IN ('admin', 'librarian', 'member'))
            )
        """);

        // Books table
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS books (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                author TEXT NOT NULL,
                isbn TEXT NOT NULL UNIQUE,
                available INTEGER DEFAULT 1
            )
        """);

        // Members table
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS members (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                email TEXT NOT NULL UNIQUE
            )
        """);

        // Loans table
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS loans (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                book_id INTEGER,
                member_id INTEGER,
                loan_date DATE,
                return_date DATE,
                FOREIGN KEY (book_id) REFERENCES books(id),
                FOREIGN KEY (member_id) REFERENCES members(id)
            )
        """);
    }

    private static void insertSampleData(Statement stmt) throws SQLException {
        // Insert admin user (password: admin123)
        stmt.execute("""
            INSERT OR IGNORE INTO users (username, password, role) 
            VALUES ('admin', '$2a$10$N.zmdr8zg7M1QqAYqPzG.uX.Kv/7RJlIlBVNb5X5Y.0MzpJKZhx2u', 'admin')
        """);

        // Insert librarian user (password: lib123)
        stmt.execute("""
            INSERT OR IGNORE INTO users (username, password, role) 
            VALUES ('librarian', '$2a$10$fH1c4jGsJxHGUPTZkh8dSuMGJqCNLkT5VYcEQh3GJM8Z7vxZN2x3S', 'librarian')
        """);

        // Insert sample books
        stmt.execute("""
            INSERT OR IGNORE INTO books (title, author, isbn, available) VALUES
            ('The Great Gatsby', 'F. Scott Fitzgerald', '978-0-7432-7356-5', 1),
            ('To Kill a Mockingbird', 'Harper Lee', '978-0-06-112008-4', 1),
            ('1984', 'George Orwell', '978-0-452-28423-4', 1),
            ('Pride and Prejudice', 'Jane Austen', '978-0-14-143951-8', 1),
            ('Harry Potter and the Sorcerer''s Stone', 'J.K. Rowling', '978-0-439-70818-8', 1)
        """);

        // Insert sample members
        stmt.execute("""
            INSERT OR IGNORE INTO members (name, email) VALUES
            ('John Doe', 'john.doe@email.com'),
            ('Jane Smith', 'jane.smith@email.com'),
            ('Bob Johnson', 'bob.johnson@email.com'),
            ('Alice Brown', 'alice.brown@email.com')
        """);
    }
}

// ValidationUtils.java - Utility class for input validation
package util;

import java.util.regex.Pattern;

public class ValidationUtils {
    
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    private static final Pattern ISBN_PATTERN = 
        Pattern.compile("^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$");

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidISBN(String isbn) {
        return isbn != null && ISBN_PATTERN.matcher(isbn.replaceAll("[- ]", "")).matches();
    }

    public static boolean isValidUsername(String username) {
        return username != null && 
               username.length() >= 3 && 
               username.length() <= 20 && 
               username.matches("^[a-zA-Z0-9_]+$");
    }

    public static boolean isValidPassword(String password) {
        return password != null && 
               password.length() >= 6 && 
               password.length() <= 50;
    }

    public static boolean isValidName(String name) {
        return name != null && 
               name.trim().length() >= 2 && 
               name.trim().length() <= 100 &&
               name.matches("^[a-zA-Z\\s.'-]+$");
    }

    public static boolean isValidBookTitle(String title) {
        return title != null && 
               title.trim().length() >= 1 && 
               title.trim().length() <= 200;
    }

    public static boolean isValidAuthorName(String author) {
        return author != null && 
               author.trim().length() >= 2 && 
               author.trim().length() <= 100 &&
               author.matches("^[a-zA-Z\\s.'-]+$");
    }

    public static String sanitizeInput(String input) {
        if (input == null) return null;
        return input.trim().replaceAll("\\s+", " ");
    }
}

// Main.java - Updated main class with database initialization
package main;

import ui.LoginUI;
import util.DatabaseInitializer;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Initialize database with sample data
        DatabaseInitializer.initializeDatabase();
        
        // Set look and feel for better UI appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
        } catch (Exception e) {
            // Fall back to default look and feel
            System.out.println("Could not set system look and feel, using default.");
        }
        
        // Launch the application
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginUI().setVisible(true);
            }
        });
    }
}