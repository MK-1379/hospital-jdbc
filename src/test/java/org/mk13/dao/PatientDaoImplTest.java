package org.mk13.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mk13.TestDatabase;
import org.mk13.exception.DataAccessException;
import org.mk13.model.Doctor;
import org.mk13.model.Patient;

class PatientDaoImplTest {

    private PatientDaoImpl patientDao;
    private Doctor doctor;

    @BeforeEach
    void setUp() {
        DataSource dataSource = TestDatabase.create();
        patientDao = new PatientDaoImpl(dataSource);

        // Todo paciente necesita un doctor (doctor_id es NOT NULL).
        doctor = new Doctor("11111111A", 0, "Martín", "Laura", new BigDecimal("3200.00"), "Cardiology");
        new DoctorDaoImpl(dataSource).add(doctor);
    }

    private Patient newPatient(String dni) {
        Patient patient = new Patient(45, "600000001", "Carlos", "Gómez", 0, dni, "Flu");
        patient.setDoctor(doctor);
        return patient;
    }

    @Test
    @DisplayName("add guarda el paciente y le asigna el id generado")
    void addAssignsGeneratedId() {
        Patient patient = newPatient("33333333C");

        assertTrue(patientDao.add(patient));
        assertTrue(patient.getId() > 0);
    }

    @Test
    @DisplayName("add sin doctor lanza IllegalArgumentException")
    void addWithoutDoctorFails() {
        Patient patient = newPatient("33333333C");
        patient.setDoctor(null);

        assertThrows(IllegalArgumentException.class, () -> patientDao.add(patient));
    }

    @Test
    @DisplayName("add con un doctor que no existe lanza DataAccessException")
    void addWithUnknownDoctorFails() {
        Doctor unknown = new Doctor();
        unknown.setId(999);
        Patient patient = newPatient("33333333C");
        patient.setDoctor(unknown);

        assertThrows(DataAccessException.class, () -> patientDao.add(patient));
    }

    @Test
    @DisplayName("getPatient devuelve los mismos datos que se guardaron")
    void getPatientReturnsSavedData() {
        Patient patient = newPatient("33333333C");
        patientDao.add(patient);

        Patient found = patientDao.getPatient(patient.getId());

        assertNotNull(found);
        assertEquals("Carlos", found.getName());
        assertEquals("Gómez", found.getLastname());
        assertEquals("33333333C", found.getDni());
        assertEquals(45, found.getAge());
        assertEquals("600000001", found.getPhone());
        assertEquals("Flu", found.getDisease());
    }

    @Test
    @DisplayName("getPatient devuelve null si el id no existe")
    void getPatientReturnsNullWhenMissing() {
        assertNull(patientDao.getPatient(999));
    }

    @Test
    @DisplayName("getPatients devuelve todos los pacientes")
    void getPatientsReturnsAll() {
        patientDao.add(newPatient("33333333C"));
        patientDao.add(newPatient("44444444D"));

        assertEquals(2, patientDao.getPatients().size());
    }

    @Test
    @DisplayName("update cambia los datos del paciente")
    void updateChangesData() {
        Patient patient = newPatient("33333333C");
        patientDao.add(patient);

        patient.setDisease("Covid");
        patient.setAge(46);
        assertTrue(patientDao.update(patient));

        Patient updated = patientDao.getPatient(patient.getId());
        assertEquals("Covid", updated.getDisease());
        assertEquals(46, updated.getAge());
    }

    @Test
    @DisplayName("update devuelve false si el paciente no existe")
    void updateReturnsFalseWhenMissing() {
        Patient patient = newPatient("33333333C");
        patient.setId(999);

        assertFalse(patientDao.update(patient));
    }

    @Test
    @DisplayName("delete borra el paciente")
    void deleteRemovesPatient() {
        Patient patient = newPatient("33333333C");
        patientDao.add(patient);

        assertTrue(patientDao.delete(patient.getId()));
        assertNull(patientDao.getPatient(patient.getId()));
    }

    @Test
    @DisplayName("delete devuelve false si el paciente no existe")
    void deleteReturnsFalseWhenMissing() {
        assertFalse(patientDao.delete(999));
    }
}
