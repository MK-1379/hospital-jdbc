package org.mk13.repositories;

import org.mk13.dao.DoctorDaoImpl;
import org.mk13.dao.PatientDaoImpl;
import org.mk13.model.Doctor;
import org.mk13.model.Patient;

public class PatientRepositoryImpl implements PatientRepository {

    private PatientDaoImpl patientDao = new PatientDaoImpl();
    private DoctorDaoImpl doctorDao = new DoctorDaoImpl();

    @Override
    public Patient getPatient(int id) {
        Patient patient = patientDao.getPatient(id);
        if (patient == null) {
            return null;
        }
        patient.setDoctor(doctorDao.getDoctorByPatientId(patient.getId()));
        return patient;
    }

    @Override
    public void add(Patient patient) {
        patientDao.add(patient);
    }

    @Override
    public void update(Patient patient) {
        patientDao.update(patient);
    }

    @Override
    public void remove(Patient patient) {
        patientDao.delete(patient.getId());
    }
}