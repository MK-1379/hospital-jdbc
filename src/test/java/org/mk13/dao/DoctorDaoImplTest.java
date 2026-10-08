package org.mk13.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mk13.TestDatabase;
import org.mk13.exception.DataAccessException;
import org.mk13.model.Doctor;
import org.mk13.model.Patient;

class DoctorDaoImplTest {

    private DoctorDaoImpl doctorDao;
    private PatientDaoImpl patientDao;

    @BeforeEach
    void setUp() {
        DataSource dataSource = TestDatabase.create();
        doctorDao = new DoctorDaoImpl(dataSource);
        patientDao = new PatientDaoImpl(dataSource);
    }

    private Doctor newDoctor(String dni) {
        Doctor doctor = new Doctor();
        doctor.setName("Laura");
        doctor.setLastname("Martín");
        doctor.setDni(dni);
        doctor.setSalary(new BigDecimal("3200.50"));
        doctor.setSpeciality("Cardiology");
        return doctor;
    }

    @Test
    @DisplayName("add guarda el doctor y le asigna el id generado")
    void addAssignsGeneratedId() {
        Doctor doctor = newDoctor("11111111A");

        assertTrue(doctorDao.add(doctor));
        assertTrue(doctor.getId() > 0);
    }

    @Test
    @DisplayName("getDoctor devuelve los mismos datos que se guardaron")
    void getDoctorReturnsSavedData() {
        Doctor doctor = newDoctor("11111111A");
        doctorDao.add(doctor);

        Doctor found = doctorDao.getDoctor(doctor.getId());

        assertNotNull(found);
        assertEquals("Laura", found.getName());
        assertEquals("Martín", found.getLastname());
        assertEquals("11111111A", found.getDni());
        assertEquals("Cardiology", found.getSpeciality());
        // compareTo y no equals: con BigDecimal, 3200.5 y 3200.50 son "distintos" para equals.
        assertEquals(0, new BigDecimal("3200.50").compareTo(found.getSalary()));
    }

    @Test
    @DisplayName("getDoctor devuelve null si el id no existe")
    void getDoctorReturnsNullWhenMissing() {
        assertNull(doctorDao.getDoctor(999));
    }

    @Test
    @DisplayName("getDoctors devuelve todos los doctores")
    void getDoctorsReturnsAll() {
        doctorDao.add(newDoctor("11111111A"));
        doctorDao.add(newDoctor("22222222B"));

        List<Doctor> doctors = doctorDao.getDoctors();

        assertEquals(2, doctors.size());
    }

    @Test
    @DisplayName("getDoctors devuelve una lista vacía si no hay doctores")
    void getDoctorsReturnsEmptyList() {
        assertTrue(doctorDao.getDoctors().isEmpty());
    }

    @Test
    @DisplayName("update cambia los datos del doctor")
    void updateChangesData() {
        Doctor doctor = newDoctor("11111111A");
        doctorDao.add(doctor);

        doctor.setSalary(new BigDecimal("4000.00"));
        doctor.setSpeciality("Neurology");
        assertTrue(doctorDao.update(doctor));

        Doctor updated = doctorDao.getDoctor(doctor.getId());
        assertEquals(0, new BigDecimal("4000.00").compareTo(updated.getSalary()));
        assertEquals("Neurology", updated.getSpeciality());
    }

    @Test
    @DisplayName("update devuelve false si el doctor no existe")
    void updateReturnsFalseWhenMissing() {
        Doctor doctor = newDoctor("11111111A");
        doctor.setId(999);

        assertFalse(doctorDao.update(doctor));
    }

    @Test
    @DisplayName("delete borra el doctor")
    void deleteRemovesDoctor() {
        Doctor doctor = newDoctor("11111111A");
        doctorDao.add(doctor);

        assertTrue(doctorDao.delete(doctor.getId()));
        assertNull(doctorDao.getDoctor(doctor.getId()));
    }

    @Test
    @DisplayName("delete devuelve false si el doctor no existe")
    void deleteReturnsFalseWhenMissing() {
        assertFalse(doctorDao.delete(999));
    }

    @Test
    @DisplayName("no se puede borrar un doctor que tiene pacientes (clave foránea)")
    void deleteFailsWhenDoctorHasPatients() {
        Doctor doctor = newDoctor("11111111A");
        doctorDao.add(doctor);
        Patient patient = new Patient(45, "600000001", "Carlos", "Gómez", 0, "33333333C", "Flu");
        patient.setDoctor(doctor);
        patientDao.add(patient);

        assertThrows(DataAccessException.class, () -> doctorDao.delete(doctor.getId()));
    }

    @Test
    @DisplayName("getDoctorByPatientId devuelve el doctor asignado al paciente")
    void getDoctorByPatientIdReturnsAssignedDoctor() {
        Doctor doctor = newDoctor("11111111A");
        doctorDao.add(doctor);
        Patient patient = new Patient(45, "600000001", "Carlos", "Gómez", 0, "33333333C", "Flu");
        patient.setDoctor(doctor);
        patientDao.add(patient);

        Doctor found = doctorDao.getDoctorByPatientId(patient.getId());

        assertNotNull(found);
        assertEquals(doctor.getId(), found.getId());
    }

    @Test
    @DisplayName("getDoctorByPatientId devuelve null si el paciente no existe")
    void getDoctorByPatientIdReturnsNullWhenMissing() {
        assertNull(doctorDao.getDoctorByPatientId(999));
    }
}
