package org.mk13.repositories;

import org.mk13.model.Patient;

public interface PatientRepository {
    Patient getPatient(int id);
    void add(Patient patient);
    void update(Patient patient);
    void remove(Patient patient);
}