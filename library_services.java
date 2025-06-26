// LoanService.java - Handles book lending and returning operations
package service;

import dao.BookDAO;
import dao.MemberDAO;
import model.Book;
import model.Member;
import model.Loan;
import util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanService {
    private BookDAO bookDAO = new BookDAO();
    private MemberDAO memberDAO = new MemberDAO();

    public boolean lendBook(int bookId, int memberId) {
        String sql = "INSERT INTO loans(book_id, member_id, loan_date) VALUES(?, ?, ?)";
        String updateBookSql = "UPDATE books SET available = 0 WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            // Check if book is available
            if (!isBookAvailable(bookId)) {
                return false;
            }
            
            // Insert loan record
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, bookId);
                pstmt.setInt(2, memberId);
                pstmt.setDate(3, Date.valueOf(LocalDate.now()));
                pstmt.executeUpdate();
            }
            
            // Update book availability
            try (PreparedStatement pstmt = conn.prepareStatement(updateBookSql)) {
                pstmt.setInt(1, bookId);
                pstmt.executeUpdate();
            }
            
            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean returnBook(int bookId, int memberId) {
        String sql = "UPDATE loans SET return_date = ? WHERE book_id = ? AND member_id = ? AND return_date IS NULL";
        String updateBookSql = "UPDATE books SET available = 1 WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            // Update loan record with return date
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setDate(1, Date.valueOf(LocalDate.now()));
                pstmt.setInt(2, bookId);
                pstmt.setInt(3, memberId);
                int affectedRows = pstmt.executeUpdate();
                
                if (affectedRows == 0) {
                    conn.rollback();
                    return false;
                }
            }
            
            // Update book availability
            try (PreparedStatement pstmt = conn.prepareStatement(updateBookSql)) {
                pstmt.setInt(1, bookId);
                pstmt.executeUpdate();
            }
            
            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Loan> getActiveLoans() {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT l.*, b.title, b.author, m.name, m.email " +
                    "FROM loans l " +
                    "JOIN books b ON l.book_id = b.id " +
                    "JOIN members m ON l.member_id = m.id " +
                    "WHERE l.return_date IS NULL";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Loan loan = new Loan();
                loan.setId(rs.getInt("id"));
                loan.setBookId(rs.getInt("book_id"));
                loan.setMemberId(rs.getInt("member_id"));
                loan.setLoanDate(rs.getDate("loan_date").toLocalDate());
                loan.setBookTitle(rs.getString("title"));
                loan.setBookAuthor(rs.getString("author"));
                loan.setMemberName(rs.getString("name"));
                loan.setMemberEmail(rs.getString("email"));
                loans.add(loan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loans;
    }

    public List<Loan> getOverdueLoans(int daysOverdue) {
        List<Loan> overdueLoans = new ArrayList<>();
        String sql = "SELECT l.*, b.title, b.author, m.name, m.email " +
                    "FROM loans l " +
                    "JOIN books b ON l.book_id = b.id " +
                    "JOIN members m ON l.member_id = m.id " +
                    "WHERE l.return_date IS NULL AND l.loan_date < ?";
        
        LocalDate overdueDate = LocalDate.now().minusDays(daysOverdue);
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, Date.valueOf(overdueDate));
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Loan loan = new Loan();
                loan.setId(rs.getInt("id"));
                loan.setBookId(rs.getInt("book_id"));
                loan.setMemberId(rs.getInt("member_id"));
                loan.setLoanDate(rs.getDate("loan_date").toLocalDate());
                loan.setBookTitle(rs.getString("title"));
                loan.setBookAuthor(rs.getString("author"));
                loan.setMemberName(rs.getString("name"));
                loan.setMemberEmail(rs.getString("email"));
                overdueLoans.add(loan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return overdueLoans;
    }

    public List<Loan> getMemberLoanHistory(int memberId) {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT l.*, b.title, b.author " +
                    "FROM loans l " +
                    "JOIN books b ON l.book_id = b.id " +
                    "WHERE l.member_id = ? " +
                    "ORDER BY l.loan_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, memberId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Loan loan = new Loan();
                loan.setId(rs.getInt("id"));
                loan.setBookId(rs.getInt("book_id"));
                loan.setMemberId(rs.getInt("member_id"));
                loan.setLoanDate(rs.getDate("loan_date").toLocalDate());
                if (rs.getDate("return_date") != null) {
                    loan.setReturnDate(rs.getDate("return_date").toLocalDate());
                }
                loan.setBookTitle(rs.getString("title"));
                loan.setBookAuthor(rs.getString("author"));
                loans.add(loan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loans;
    }

    private boolean isBookAvailable(int bookId) {
        String sql = "SELECT available FROM books WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bookId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("available") == 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

// ReportService.java - Generates various reports for the library
package service;

import util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ReportService {

    public Map<String, Integer> getLibraryStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        
        try (Connection conn = DBConnection.getConnection()) {
            // Total books
            String bookSql = "SELECT COUNT(*) as total FROM books";
            try (PreparedStatement pstmt = conn.prepareStatement(bookSql);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    stats.put("totalBooks", rs.getInt("total"));
                }
            }
            
            // Available books
            String availableSql = "SELECT COUNT(*) as available FROM books WHERE available = 1";
            try (PreparedStatement pstmt = conn.prepareStatement(availableSql);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    stats.put("availableBooks", rs.getInt("available"));
                }
            }
            
            // Total members
            String memberSql = "SELECT COUNT(*) as total FROM members";
            try (PreparedStatement pstmt = conn.prepareStatement(memberSql);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    stats.put("totalMembers", rs.getInt("total"));
                }
            }
            
            // Active loans
            String loansSql = "SELECT COUNT(*) as active FROM loans WHERE return_date IS NULL";
            try (PreparedStatement pstmt = conn.prepareStatement(loansSql);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    stats.put("activeLoans", rs.getInt("active"));
                }
            }
            
            // Overdue books (more than 14 days)
            String overdueSql = "SELECT COUNT(*) as overdue FROM loans WHERE return_date IS NULL AND loan_date < ?";
            try (PreparedStatement pstmt = conn.prepareStatement(overdueSql)) {
                pstmt.setDate(1, Date.valueOf(LocalDate.now().minusDays(14)));
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    stats.put("overdueBooks", rs.getInt("overdue"));
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return stats;
    }

    public Map<String, Integer> getMostPopularBooks(int limit) {
        Map<String, Integer> popularBooks = new HashMap<>();
        String sql = "SELECT b.title, COUNT(l.id) as loan_count " +
                    "FROM books b " +
                    "LEFT JOIN loans l ON b.id = l.book_id " +
                    "GROUP BY b.id, b.title " +
                    "ORDER BY loan_count DESC " +
                    "LIMIT ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limit);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                popularBooks.put(rs.getString("title"), rs.getInt("loan_count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return popularBooks;
    }

    public Map<String, Integer> getMostActiveMembers(int limit) {
        Map<String, Integer> activeMembers = new HashMap<>();
        String sql = "SELECT m.name, COUNT(l.id) as loan_count " +
                    "FROM members m " +
                    "LEFT JOIN loans l ON m.id = l.member_id " +
                    "GROUP BY m.id, m.name " +
                    "ORDER BY loan_count DESC " +
                    "LIMIT ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limit);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                activeMembers.put(rs.getString("name"), rs.getInt("loan_count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return activeMembers;
    }

    public String generateMonthlyReport() {
        StringBuilder report = new StringBuilder();
        Map<String, Integer> stats = getLibraryStatistics();
        
        report.append("=== MONTHLY LIBRARY REPORT ===\n");
        report.append("Generated on: ").append(LocalDate.now()).append("\n\n");
        
        report.append("LIBRARY STATISTICS:\n");
        report.append("Total Books: ").append(stats.getOrDefault("totalBooks", 0)).append("\n");
        report.append("Available Books: ").append(stats.getOrDefault("availableBooks", 0)).append("\n");
        report.append("Total Members: ").append(stats.getOrDefault("totalMembers", 0)).append("\n");
        report.append("Active Loans: ").append(stats.getOrDefault("activeLoans", 0)).append("\n");
        report.append("Overdue Books: ").append(stats.getOrDefault("overdueBooks", 0)).append("\n\n");
        
        report.append("TOP 5 POPULAR BOOKS:\n");
        Map<String, Integer> popularBooks = getMostPopularBooks(5);
        int rank = 1;
        for (Map.Entry<String, Integer> entry : popularBooks.entrySet()) {
            report.append(rank++).append(". ").append(entry.getKey())
                  .append(" (").append(entry.getValue()).append(" loans)\n");
        }
        
        report.append("\nTOP 5 ACTIVE MEMBERS:\n");
        Map<String, Integer> activeMembers = getMostActiveMembers(5);
        rank = 1;
        for (Map.Entry<String, Integer> entry : activeMembers.entrySet()) {
            report.append(rank++).append(". ").append(entry.getKey())
                  .append(" (").append(entry.getValue()).append(" loans)\n");
        }
        
        return report.toString();
    }
}

// NotificationService.java - Handles notifications for overdue books and reminders
package service;

import model.Loan;
import model.Member;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class NotificationService {
    private LoanService loanService = new LoanService();

    public void sendOverdueNotifications() {
        List<Loan> overdueLoans = loanService.getOverdueLoans(14);
        
        for (Loan loan : overdueLoans) {
            sendOverdueNotification(loan);
        }
    }

    public void sendDueReminders() {
        List<Loan> activeLoans = loanService.getActiveLoans();
        
        for (Loan loan : activeLoans) {
            long daysLoaned = ChronoUnit.DAYS.between(loan.getLoanDate(), LocalDate.now());
            
            // Send reminder 2 days before due date (assuming 14 days loan period)
            if (daysLoaned == 12) {
                sendDueReminder(loan);
            }
        }
    }

    private void sendOverdueNotification(Loan loan) {
        long daysOverdue = ChronoUnit.DAYS.between(loan.getLoanDate().plusDays(14), LocalDate.now());
        
        String message = String.format(
            "OVERDUE NOTICE\n" +
            "Dear %s,\n\n" +
            "The book '%s' by %s is %d days overdue.\n" +
            "Please return it as soon as possible to avoid additional charges.\n\n" +
            "Thank you,\nLibrary Management System",
            loan.getMemberName(),
            loan.getBookTitle(),
            loan.getBookAuthor(),
            daysOverdue
        );
        
        // In a real system, this would send an email or SMS
        System.out.println("Sending overdue notification to: " + loan.getMemberEmail());
        System.out.println(message);
    }

    private void sendDueReminder(Loan loan) {
        String message = String.format(
            "DUE REMINDER\n" +
            "Dear %s,\n\n" +
            "This is a friendly reminder that the book '%s' by %s is due in 2 days.\n" +
            "Please return it by the due date to avoid overdue charges.\n\n" +
            "Thank you,\nLibrary Management System",
            loan.getMemberName(),
            loan.getBookTitle(),
            loan.getBookAuthor()
        );
        
        // In a real system, this would send an email or SMS
        System.out.println("Sending due reminder to: " + loan.getMemberEmail());
        System.out.println(message);
    }

    public String generateOverdueReport() {
        List<Loan> overdueLoans = loanService.getOverdueLoans(14);
        StringBuilder report = new StringBuilder();
        
        report.append("=== OVERDUE BOOKS REPORT ===\n");
        report.append("Generated on: ").append(LocalDate.now()).append("\n");
        report.append("Total overdue books: ").append(overdueLoans.size()).append("\n\n");
        
        for (Loan loan : overdueLoans) {
            long daysOverdue = ChronoUnit.DAYS.between(loan.getLoanDate().plusDays(14), LocalDate.now());
            report.append("Book: ").append(loan.getBookTitle()).append("\n");
            report.append("Member: ").append(loan.getMemberName()).append("\n");
            report.append("Email: ").append(loan.getMemberEmail()).append("\n");
            report.append("Days Overdue: ").append(daysOverdue).append("\n");
            report.append("Loan Date: ").append(loan.getLoanDate()).append("\n");
            report.append("---\n");
        }
        
        return report.toString();
    }
}

// Enhanced LibraryService.java - Main service orchestrator
package service;

import dao.BookDAO;
import dao.MemberDAO;
import model.Book;
import model.Member;
import model.Loan;

import java.util.List;
import java.util.Map;

public class LibraryService {
    private BookDAO bookDAO = new BookDAO();
    private MemberDAO memberDAO = new MemberDAO();
    private LoanService loanService = new LoanService();
    private ReportService reportService = new ReportService();
    private NotificationService notificationService = new NotificationService();

    // Book operations
    public boolean addBook(Book book) {
        return bookDAO.addBook(book);
    }

    public boolean updateBook(Book book) {
        return bookDAO.updateBook(book);
    }

    public boolean deleteBook(int id) {
        return bookDAO.deleteBook(id);
    }

    public List<Book> searchBooks(String keyword) {
        return bookDAO.searchBooks(keyword);
    }

    public List<Book> getAllBooks() {
        return bookDAO.searchBooks(""); // Empty search returns all books
    }

    // Member operations
    public boolean addMember(Member member) {
        return memberDAO.addMember(member);
    }

    public boolean updateMember(Member member) {
        return memberDAO.updateMember(member);
    }

    public boolean deleteMember(int id) {
        return memberDAO.deleteMember(id);
    }

    public List<Member> searchMembers(String keyword) {
        return memberDAO.searchMembers(keyword);
    }

    public List<Member> getAllMembers() {
        return memberDAO.searchMembers(""); // Empty search returns all members
    }

    // Loan operations
    public boolean lendBook(int bookId, int memberId) {
        return loanService.lendBook(bookId, memberId);
    }

    public boolean returnBook(int bookId, int memberId) {
        return loanService.returnBook(bookId, memberId);
    }

    public List<Loan> getActiveLoans() {
        return loanService.getActiveLoans();
    }

    public List<Loan> getOverdueLoans() {
        return loanService.getOverdueLoans(14);
    }

    public List<Loan> getMemberLoanHistory(int memberId) {
        return loanService.getMemberLoanHistory(memberId);
    }

    // Report operations
    public Map<String, Integer> getLibraryStatistics() {
        return reportService.getLibraryStatistics();
    }

    public String generateMonthlyReport() {
        return reportService.generateMonthlyReport();
    }

    public String generateOverdueReport() {
        return notificationService.generateOverdueReport();
    }

    // Notification operations
    public void sendOverdueNotifications() {
        notificationService.sendOverdueNotifications();
    }

    public void sendDueReminders() {
        notificationService.sendDueReminders();
    }

    // Utility methods
    public boolean isBookAvailable(int bookId) {
        List<Book> books = bookDAO.searchBooks("");
        return books.stream()
                   .filter(book -> book.getId() == bookId)
                   .findFirst()
                   .map(Book::isAvailable)
                   .orElse(false);
    }

    public int getTotalBooksCount() {
        return getLibraryStatistics().getOrDefault("totalBooks", 0);
    }

    public int getTotalMembersCount() {
        return getLibraryStatistics().getOrDefault("totalMembers", 0);
    }

    public int getActiveLoansCount() {
        return getLibraryStatistics().getOrDefault("activeLoans", 0);
    }
}