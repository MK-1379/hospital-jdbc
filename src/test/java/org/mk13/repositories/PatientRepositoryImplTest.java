package org.mk13.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mk13.TestDatabase;
import org.mk13.dao.DoctorDaoImpl;
import org.mk13.dao.PatientDaoImpl;
import org.mk13.model.Doctor;
import org.mk13.model.Patient;

class PatientRepositoryImplTest {

    private PatientRepository repository;
    private PatientDaoImpl patientDao;
    private Doctor doctor;

    @BeforeEach
    void setUp() {
        DataSource dataSource = TestDatabase.create();
        DoctorDaoImpl doctorDao = new DoctorDaoImpl(dataSource);
        patientDao = new PatientDaoImpl(dataSource);
        repository = new PatientRepositoryImpl(patientDao, doctorDao);

        doctor = new Doctor("11111111A", 0, "Martín", "Laura", new BigDecimal("3200.00"), "Cardiology");
        doctorDao.add(doctor);
    }

    private Patient newPatient() {
        Patient patient = new Patient(45, "600000001", "Carlos", "Gómez", 0, "33333333C", "Flu");
        patient.setDoctor(doctor);
        return patient;
    }

    @Test
    @DisplayName("getPatient devuelve el paciente con su doctor cargado")
    void getPatientLoadsDoctor() {
        Patient patient = newPatient();
        repository.add(patient);

        Patient found = repository.getPatient(patient.getId());

        assertNotNull(found);
        assertNotNull(found.getDoctor());
        assertEquals(doctor.getId(), found.getDoctor().getId());
        assertEquals("Laura", found.getDoctor().getName());
    }

    @Test
    @DisplayName("el DAO solo no carga el doctor: para eso está el repositorio")
    void daoDoesNotLoadDoctor() {
        Patient patient = newPatient();
        repository.add(patient);

        assertNull(patientDao.getPatient(patient.getId()).getDoctor());
    }

    @Test
    @DisplayName("getPatient devuelve null si el paciente no existe")
    void getPatientReturnsNullWhenMissing() {
        assertNull(repository.getPatient(999));
    }

    @Test
    @DisplayName("update guarda los cambios del paciente")
    void updateSavesChanges() {
        Patient patient = newPatient();
        repository.add(patient);

        patient.setDisease("Covid");
        repository.update(patient);

        assertEquals("Covid", repository.getPatient(patient.getId()).getDisease());
    }

    @Test
    @DisplayName("remove borra el paciente")
    void removeDeletesPatient() {
        Patient patient = newPatient();
        repository.add(patient);

        repository.remove(patient);

        assertNull(repository.getPatient(patient.getId()));
    }
}
