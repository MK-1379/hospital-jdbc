package org.mk13.repositories;

import org.mk13.idao.DoctorDao;
import org.mk13.idao.PatientDao;
import org.mk13.model.Patient;

public class PatientRepositoryImpl implements PatientRepository {

    private final PatientDao patientDao;
    private final DoctorDao doctorDao;

    public PatientRepositoryImpl(PatientDao patientDao, DoctorDao doctorDao) {
        this.patientDao = patientDao;
        this.doctorDao = doctorDao;
    }

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