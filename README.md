# HospitalJDBC

A small Java console application that manages doctors and patients of a hospital with plain JDBC and MySQL. It was built as a first-year project for the *Programación* module of the Higher Vocational Degree in Web Application Development (DAW), and later improved on its own.

The goal of the project is to practise a layered data-access design (model, DAO, repository) without any framework.

## Features

- Full CRUD for doctors and patients
- A patient can be loaded together with its assigned doctor
- Parameterised queries (`PreparedStatement`) everywhere, so no SQL injection
- Database credentials read from a configuration file, not from the code
- Database errors surface as a single `DataAccessException` that keeps the original cause

## Tech stack

- Java 21
- Maven
- MySQL 8 and the MySQL Connector/J driver
- JDBC

## Requirements

- JDK 21
- Maven 3.9 or later (or an IDE with Maven support, such as IntelliJ IDEA)
- A running MySQL 8 server

## Getting started

1. **Clone the repository**

```bash
   git clone https://github.com/MK-1379/hospital-jdbc.git
   cd hospital-jdbc
```

2. **Create the database.** Run [`database/poo_hospital.sql`](database/poo_hospital.sql) with MySQL Workbench or the `mysql` client. It creates the `poo_hospital` database with two doctors and two patients. It can be run again at any time to reset the data.

3. **Create a database user** with only the permissions the app needs:

```sql
   CREATE USER 'hospital_app'@'localhost' IDENTIFIED BY 'choose-a-password';
   GRANT SELECT, INSERT, UPDATE, DELETE ON poo_hospital.* TO 'hospital_app'@'localhost';
```

4. **Create your configuration file.** Copy `src/main/resources/db.properties.example` to `src/main/resources/db.properties` and fill in your password. The real file is ignored by Git.

5. **Run it.** From your IDE, run `org.mk13.App`. From the command line:

```bash
   mvn compile exec:java -Dexec.mainClass="org.mk13.App"
```

`App` inserts, reads, updates and deletes a sample doctor and a sample patient and prints each step.

## Project structure

```
database/                    SQL script (schema and sample data)
src/main/java/org/mk13/
  model/                     Doctor