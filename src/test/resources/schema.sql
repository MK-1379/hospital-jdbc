-- Misma estructura que database/poo_hospital.sql, para la base de datos H2 de los tests.
CREATE TABLE doctor (
    id         INT           NOT NULL AUTO_INCREMENT,
    name       VARCHAR(60)   NOT NULL,
    lastname   VARCHAR(60)   NOT NULL,
    dni        VARCHAR(10)   NOT NULL,
    salary     DECIMAL(10,2) NOT NULL,
    speciality VARCHAR(60)   NOT NULL,
    PRIMARY KEY (id)
);

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
);
