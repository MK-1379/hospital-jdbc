# HospitalJDBC

A small Java console application that manages doctors and patients of a hospital with plain JDBC and MySQL. It was built as a first-year project for the *Programación* module of the Higher Vocational Degree in Web Application Development (DAW), and later improved on its own.

The goal of the project is to practise a layered data-access design (model, DAO, repository) without any framework.

## Features

- Full CRUD for doctors and patients
- A patient can be loaded together with its assigned doctor
- Parameterised queries (`PreparedStatement`) everywhere, so no SQL injection
- Database credentials read from a configuration file, not from the code
- Database errors surface as a single `DataAccessException` that keeps the original cause
- Salaries stored as `DECIMAL(10,2)` and handled as `BigDecimal`, so money keeps exact decimals
- DAOs receive their `DataSource` through the constructor (dependency injection), which makes them easy to test
- Automated tests with JUnit 5 against an in-memory H2 database

## Tech stack

- Java 21
- Maven
- MySQL 8 and the MySQL Connector/J driver
- JDBC
- JUnit 5 and H2 (tests)

## Requirements

- JDK 21
- Maven is optional: the project includes the Maven Wrapper (`mvnw`). An IDE with Maven support, such as IntelliJ IDEA, also works.
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
   ./mvnw compile exec:java -Dexec.mainClass="org.mk13.App"
```

`App` inserts, reads, updates and deletes a sample doctor and a sample patient and prints each step.

## Running the tests

```bash
./mvnw test        # Linux / macOS / Git Bash
.\mvnw.cmd test    # Windows (PowerShell or CMD)
```

You do not need Maven installed: the Maven Wrapper (`mvnw`) downloads the right version the first time. You only need a JDK 21 with `JAVA_HOME` set. In IntelliJ IDEA you can also right-click `src/test/java` and choose **Run 'All Tests'**.

The tests do not need MySQL. Each test creates its own empty H2 database in memory (in MySQL compatibility mode) from [`src/test/resources/schema.sql`](src/test/resources/schema.sql), so tests never share data and can run in any order.

They cover:

- `DoctorDaoImplTest`: CRUD, missing ids, `BigDecimal` salaries, and that a doctor with patients cannot be deleted (foreign key).
- `PatientDaoImplTest`: CRUD, a patient without a doctor, and a patient pointing to a doctor that does not exist.
- `PatientRepositoryImplTest`: loading a patient together with its doctor.

## Project structure

```
database/                    SQL script (schema and sample data)
src/main/java/org/mk13/
  model/                     Doctor and Patient
  idao/                      DAO interfaces
  dao/                       JDBC implementations of the DAOs
  repositories/              PatientRepository (patient + doctor)
  exception/                 DataAccessException
  util/                      DatabaseConnection (builds the DataSource from db.properties)
  App.java                   Demo of every operation
src/main/resources/          db.properties.example
src/test/java/org/mk13/      JUnit 5 tests and the H2 test database helper
src/test/resources/          schema.sql for the tests
```

## Design notes

- **Dependency injection without a framework.** `App` builds the `DataSource` once and passes it to the DAOs, and the DAOs to the repository. Production code uses MySQL; the tests pass an H2 `DataSource` instead, without changing a single line of the DAOs.
- **One connection per operation.** Each DAO method opens a connection, runs one statement and closes everything with try-with-resources.
