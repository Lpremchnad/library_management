# Library Management System

Desktop Swing application with dark violet theme. File-based persistence (library_data.dat).

## Features
- **Admin**: Add/manage books & users, issue/return books, view stats
- **User**: Browse catalog, borrow/return books, search
- **Console**: Full CLI alternative
- Modern UI with tables, forms, gradients

## Prerequisites
- Java 11+
- Maven (optional, for build)

## Quick Start (No Maven)
```
# Compile manually (if no mvn)
javac -cp "lib/*" src/main/java/console_lms/**/*.java

# Run GUI
java -cp "lib/*:src/main/java:target/classes" console_lms.Main

# Run Console
java -cp "lib/*:src/main/java:target/classes" console_lms.ui.ConsoleApp
```

## Maven Build
```
mvn clean compile
mvn exec:java -Dexec.mainClass="console_lms.Main"
# or
mvn package
java -jar target/LibraryManagementSystem-1.0.0.jar
```

## Default Credentials
```
Admin: admin / admin123
User:  user / user123
```

## Data
- Persisted in `library_data.dat`
- Sample books/users auto-created on first run

## Structure
```
src/main/java/console_lms/
├── gui/          # Swing UI
├── models/       # Account, Book, etc.
├── service/      # Business logic
└── ui/           # Console UI
lib/              # External jars
legacy/           # Old DB code (future?)
```

## Development
- Edit source in `src/main/java/console_lms/`
- Recent fixes: AdminDashboard AddBookPanel, text field focus
