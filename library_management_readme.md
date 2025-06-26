# Library Management System

A comprehensive Java-based Library Management System with a Swing GUI interface, designed to efficiently manage library operations including book management, member management, and loan tracking.

## Features

- **User Authentication**: Secure login system with role-based access (Admin, Librarian, Member)
- **Book Management**: Add, update, delete, and search books
- **Member Management**: Manage library member information
- **Loan Tracking**: Track book loans and returns
- **Database Integration**: SQLite database for data persistence
- **User-Friendly GUI**: Swing-based graphical interface

## Project Structure

```
LibraryManagementSystem/
├── src/
│   ├── main/
│   │   └── Main.java
│   ├── model/
│   │   ├── Book.java
│   │   ├── Member.java
│   │   └── User.java
│   ├── dao/
│   │   ├── BookDAO.java
│   │   ├── MemberDAO.java
│   │   └── UserDAO.java
│   ├── service/
│   │   ├── AuthService.java
│   │   └── LibraryService.java
│   ├── ui/
│   │   ├── LoginUI.java
│   │   ├── MainUI.java
│   │   ├── BookUI.java
│   │   └── MemberUI.java
│   └── util/
│       └── DBConnection.java
├── lib/
│   ├── sqlite-jdbc-3.36.0.3.jar
│   └── jbcrypt-0.4.jar
├── resources/
│   └── library.db
├── database/
│   └── schema.sql
├── README.md
└── .gitignore
```

## Prerequisites

- Java 8 or higher
- SQLite JDBC Driver
- jBCrypt library for password hashing

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/library-management-system.git
   cd library-management-system
   ```

2. Compile the project:
   ```bash
   javac -cp "lib/*:src" -d bin src/**/*.java
   ```

3. Run the application:
   ```bash
   java -cp "lib/*:bin" main.Main
   ```

## Database Setup

The application uses SQLite database. The schema is automatically created when you first run the application. The database file `library.db` will be created in the `resources` directory.

### Default Users

You can register new users through the application or manually insert admin users into the database.

## Usage

1. **Login**: Start the application and login with your credentials
2. **Register**: New users can register through the login screen
3. **Manage Books**: Add, update, delete, and search for books
4. **Manage Members**: Handle member registration and information
5. **Book Loans**: Track book borrowing and returns (feature in development)

## Technologies Used

- **Java**: Core programming language
- **Swing**: GUI framework
- **SQLite**: Database
- **JDBC**: Database connectivity
- **jBCrypt**: Password hashing

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Future Enhancements

- [ ] Complete loan management functionality
- [ ] Report generation
- [ ] Email notifications for overdue books
- [ ] Web-based interface
- [ ] Advanced search filters
- [ ] Book reservation system

## Contact

Your Name - your.email@example.com

Project Link: [https://github.com/yourusername/library-management-system](https://github.com/yourusername/library-management-system)