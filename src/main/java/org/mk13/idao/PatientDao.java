package org.mk13.idao;

import org.mk13.model.Patient;

import java.util.List;

public interface PatientDao {
    public boolean add(Patient pat);
    public boolean delete(int id);
    public Patient getPatient(int id);
    public List<Patient> getPatients();
    public boolean update(Patient pat);
}
