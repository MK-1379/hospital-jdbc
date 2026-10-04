package org.mk13.idao;

import org.mk13.model.Doctor;
import java.util.List;

public interface DoctorDao {
    public boolean add(Doctor doc);
    public boolean delete(int id);
    public Doctor getDoctor(int id);
    public List<Doctor> getDoctors();
    public boolean update(Doctor doc);
    public Doctor getDoctorByPatientId(int patient_id);
}
