-- Library Management System Database Schema
-- SQLite Database

-- Users table for authentication
CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    role TEXT NOT NULL CHECK (role IN ('admin', 'librarian', 'member')),
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Books table
CREATE TABLE books (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    author TEXT NOT NULL,
    isbn TEXT NOT NULL UNIQUE,
    available INTEGER DEFAULT 1,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Members table
CREATE TABLE members (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    phone TEXT,
    address TEXT,
    membership_date DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Loans table for tracking book borrowing
CREATE TABLE loans (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    book_id INTEGER NOT NULL,
    member_id INTEGER NOT NULL,
    loan_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE,
    status TEXT DEFAULT 'active' CHECK (status IN ('active', 'returned', 'overdue')),
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE,
    FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE
);

-- Indexes for better performance
CREATE INDEX idx_books_title ON books(title);
CREATE INDEX idx_books_author ON books(author);
CREATE INDEX idx_books_isbn ON books(isbn);
CREATE INDEX idx_members_email ON members(email);
CREATE INDEX idx_loans_book_id ON loans(book_id);
CREATE INDEX idx_loans_member_id ON loans(member_id);
CREATE INDEX idx_loans_status ON loans(status);

-- Insert default admin user (password: admin123)
INSERT INTO users (username, password, role) VALUES 
('admin', '$2a$10$N9qo8uL3xhT5k1Uf5C1wz.K3wZ8xG7xN5jT1xhT5k1Uf5C1wz', 'admin');

-- Insert sample data
INSERT INTO books (title, author, isbn) VALUES 
('The Great Gatsby', 'F. Scott Fitzgerald', '978-0-7432-7356-5'),
('To Kill a Mockingbird', 'Harper Lee', '978-0-06-112008-4'),
('1984', 'George Orwell', '978-0-452-28423-4'),
('Pride and Prejudice', 'Jane Austen', '978-0-14-143951-8'),
('The Catcher in the Rye', 'J.D. Salinger', '978-0-316-76948-0');

INSERT INTO members (name, email, phone, address) VALUES 
('John Doe', 'john.doe@email.com', '123-456-7890', '123 Main St, City, State'),
('Jane Smith', 'jane.smith@email.com', '098-765-4321', '456 Oak Ave, City, State'),
('Mike Johnson', 'mike.johnson@email.com', '555-123-4567', '789 Pine Rd, City, State');