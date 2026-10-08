-- Base de datos del proyecto HospitalJDBC.
-- Se puede ejecutar varias veces: borra las tablas y las vuelve a crear con datos de ejemplo.

CREATE DATABASE IF NOT EXISTS poo_hospital CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE poo_hospital;

DROP TABLE IF EXISTS patient;
DROP TABLE IF EXISTS doctor;

CREATE TABLE doctor (
                        id         INT         NOT NULL AUTO_INCREMENT,
                        name       VARCHAR(60) NOT NULL,
                        lastname   VARCHAR(60) NOT NULL,
                        dni        VARCHAR(10) NOT NULL,
                        salary     DECIMAL(10,2) NOT NULL,
                        speciality VARCHAR(60) NOT NULL,
                        PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE patient (
                         id        INT          NOT NULL AUTO_INCREMENT,
                         name      VARCHAR(60)  NOT NULL,
                         lastname  VARCHAR(60)  NOT NULL,
                         dni       VARCHAR(10)  NOT NULL,
                         age       INT          NOT NULL,
                         phone     VARCHAR(15)  NOT NULL,
                         disease   VARCHAR(100) NOT NULL,
                         doctor_id INT          NOT NULL,
                         PRIMARY KEY (id),
                         CONSTRAINT fk_patient_doctor FOREIGN KEY (doctor_id) REFERENCES doctor (id)
) ENGINE=InnoDB;

-- Datos inventados, solo para probar.
INSERT INTO doctor (name, lastname, dni, salary, speciality) VALUES
                                                                 ('Laura',  'Martín', '11111111A', 3200, 'Cardiology'),
                                                                 ('Andrés', 'Ruiz',   '22222222B', 3600, 'Neurology');

INSERT INTO patient (name, lastname, dni, age, phone, disease, doctor_id) VALUES
                                                                              ('Carlos', 'Gómez', '33333333C', 45, '600000001', 'Flu',      1),
                                                                              ('Marta',  'López', '44444444D', 32, '600000002', 'Migraine', 2);