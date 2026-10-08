package org.mk13;

import java.math.BigDecimal;
import java.util.List;
import javax.sql.DataSource;

import org.mk13.dao.DoctorDaoImpl;
import org.mk13.dao.PatientDaoImpl;
import org.mk13.model.Doctor;
import org.mk13.model.Patient;
import org.mk13.repositories.PatientRepository;
import org.mk13.repositories.PatientRepositoryImpl;
import org.mk13.util.DatabaseConnection;

public class App {

    public static void main(String[] args) {

        // Se crean las dependencias aquí y se pasan por el constructor.
        DataSource dataSource = DatabaseConnection.createDataSource();
        DoctorDaoImpl doctorDao = new DoctorDaoImpl(dataSource);
        PatientDaoImpl patientDao = new PatientDaoImpl(dataSource);
        PatientRepository repo = new PatientRepositoryImpl(patientDao, doctorDao);

        // Insertar doctor
        Doctor doc = new Doctor();
        doc.setName("Juan");
        doc.setLastname("Pérez");
        doc.setDni("12345678A");
        doc.setSalary(new BigDecimal("3500.00"));
        doc.setSpeciality("Cardiology");

        doctorDao.add(doc);
        System.out.println("Doctor añadido: " + doc);


        // Obtener doctor por ID
        Doctor doctor = doctorDao.getDoctor(doc.getId());
        System.out.println("Doctor obtenido:");
        System.out.println(doctor);


        // Actualizar doctor
        doctor.setSalary(new BigDecimal("4000.00"));
        doctorDao.update(doctor);

        System.out.println("Doctor actualizado:");
        System.out.println(doctorDao.getDoctor(doctor.getId()));


        // Listar doctores
        List<Doctor> doctors = doctorDao.getDoctors();

        System.out.println("Lista de doctores:");
        for (Doctor d : doctors) {
            System.out.println(d);
        }


        // Insertar paciente
        Patient p = new Patient();
        p.setName("Carlos");
        p.setLastname("Gomez");
        p.setDni("87654321B");
        p.setAge(45);
        p.setPhone("600123456");
        p.setDisease("Flu");

        // Para asignar doctor
        p.setDoctor(doc);

        patientDao.add(p);

        System.out.println("Paciente añadido:");
        System.out.println(p);


        // Obtener paciente por id
        Patient patient = patientDao.getPatient(p.getId());

        System.out.println("Paciente obtenido:");
        System.out.println(patient);


        // Actualizar paciente
        patient.setDisease("Covid");

        patientDao.update(patient);

        System.out.println("Paciente actualizado:");
        System.out.println(patientDao.getPatient(patient.getId()));


        // Listar pacientes
        List<Patient> patients = patientDao.getPatients();

        System.out.println("Lista de pacientes:");
        for (Patient pat : patients) {
            System.out.println(pat);
        }


        // Obtener paciente y doctor
        Patient patientWithDoctor = repo.getPatient(p.getId());

        System.out.println("Paciente con su doctor:");
        System.out.println(patientWithDoctor);
        System.out.println("Doctor asignado:");
        System.out.println(patientWithDoctor.getDoctor());


        // Borrar paciente
        patientDao.delete(p.getId());
        System.out.println("Paciente eliminado");


        // Borrar doctor
        doctorDao.delete(doc.getId());
        System.out.println("Doctor eliminado");
    }

}

