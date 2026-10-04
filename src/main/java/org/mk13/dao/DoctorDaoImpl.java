package org.mk13.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.mk13.idao.DoctorDao;
import org.mk13.model.Doctor;
import org.mk13.util.DatabaseConnection;

public class DoctorDaoImpl implements DoctorDao {
    static Connection con = DatabaseConnection.getConnection();

    @Override
    public Doctor getDoctor(int id) {
        String query = "select * from doctor where id= ?";
        PreparedStatement ps = null;
        boolean check = false;
        Doctor doctor = new Doctor();
        try {
            ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                check = true;
                doctor.setId(rs.getInt("id"));
                doctor.setDni(rs.getString("dni"));
                doctor.setLastname(rs.getString("lastname"));
                doctor.setName(rs.getString("name"));
                doctor.setSalary(rs.getDouble("salary"));
                doctor.setSpeciality(rs.getString("speciality"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (check) {
            return doctor;
        } else
            return null;
    }

    @Override
    public boolean add(Doctor doc) {
        String query = "INSERT INTO doctor (name, lastname, dni, salary, speciality) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, doc.getName());
            ps.setString(2, doc.getLastname());
            ps.setString(3, doc.getDni());
            ps.setDouble(4, doc.getSalary());
            ps.setString(5, doc.getSpeciality());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    doc.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        String query = "DELETE FROM doctor WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Doctor> getDoctors() {
        List<Doctor> doctor = new ArrayList<>();
        String query = "SELECT * FROM doctor";
        try (PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Doctor d = new Doctor();
                d.setId(rs.getInt("id"));
                d.setName(rs.getString("name"));
                d.setLastname(rs.getString("lastname"));
                d.setDni(rs.getString("dni"));
                d.setSalary(rs.getDouble("salary"));
                d.setSpeciality(rs.getString("speciality"));
                doctor.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctor;
    }

    @Override
    public boolean update(Doctor doc) {
        if (ifExistDoctor(doc.getId())){
            String query
                    = "UPDATE `doctor` SET `name`=?,`lastname`=?,`dni`=?,`salary`=?,`speciality`=? WHERE id=?";
            PreparedStatement ps = null;
            int rs = 0;
            try {
                ps = con.prepareStatement(query);
                ps.setString(1,doc.getName());
                ps.setString(2,doc.getLastname());
                ps.setString(3,doc.getDni());
                ps.setDouble(4,doc.getSalary());
                ps.setString(5,doc.getSpeciality());
                ps.setInt(6, doc.getId());
                rs = ps.executeUpdate(); //returns number of updated registries
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (rs > 0) {
                return true;
            }
        }
        return false;
    }
    private boolean ifExistDoctor(int id) {
        String query = "select * from doctor where id= ?";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Doctor getDoctorByPatientId(int patient_id) {
        String sql = "SELECT d.* FROM doctor d INNER JOIN patient p ON d.id = p.doctor_id  WHERE p.id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, patient_id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Doctor d = new Doctor();
                d.setId(rs.getInt("id"));
                d.setName(rs.getString("name"));
                d.setLastname(rs.getString("lastname"));
                d.setDni(rs.getString("dni"));
                d.setSalary(rs.getDouble("salary"));
                d.setSpeciality(rs.getString("speciality"));
                return d;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
