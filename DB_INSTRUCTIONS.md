# Database Setup Instructions

To fully run the Library Management System, you need to set up a MySQL database.

## 1. Install MySQL
If you don't have MySQL installed, please install it (e.g., MySQL Community Server).

## 2. Run the Setup Script
I have created a file named `setup.sql` in your project folder.
Run this script in your MySQL client (Workbench, Command Line, etc.) to create the database and tables.

```sql
source c:/Users/lprem/OneDrive/Desktop/ProjectGurukul-LibraryManagementSystem/setup.sql
```

## 3. Update Application Config
The application now uses a configuration file named `db.properties`.
**IF YOUR DATABASE CONFIGURATION IS DIFFERENT:**
1. Open `db.properties` in the project folder.
2. Edit the values to match your MySQL setup:
   ```properties
   db.url=jdbc:mysql://localhost:3308/library
   db.user=root
   db.password=Narend-10
   ```
3. Save the file. **No need to recompile!**

## 4. Compile and Run
After making changes, use this command to compile and run:

**Compile:**
```powershell
javac -d target/classes -cp ".;C:\Users\lprem\.m2\repository\mysql\mysql-connector-java\5.1.39\mysql-connector-java-5.1.39.jar;C:\Users\lprem\.m2\repository\no\tornado\databinding\jxdatepicker-support\1.0\jxdatepicker-support-1.0.jar;C:\Users\lprem\.m2\repository\org\swinglabs\swingx\1.6\swingx-1.6.jar" src\main\java\MainFrame.java
```

**Run:**
```powershell
java -cp "target/classes;C:\Users\lprem\.m2\repository\mysql\mysql-connector-java\5.1.39\mysql-connector-java-5.1.39.jar;C:\Users\lprem\.m2\repository\no\tornado\databinding\jxdatepicker-support\1.0\jxdatepicker-support-1.0.jar;C:\Users\lprem\.m2\repository\org\swinglabs\swingx\1.6\swingx-1.6.jar" MainFrame
```

## Default Login
Once setup is complete, you can login with:
- **Username**: admin
- **Password**: password123
