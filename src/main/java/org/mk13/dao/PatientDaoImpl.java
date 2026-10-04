package org.mk13.dao;

import org.mk13.idao.PatientDao;
import org.mk13.model.Patient;
import org.mk13.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDaoImpl implements PatientDao {
    static Connection con = DatabaseConnection.getConnection();

    @Override
    public boolean add(Patient pat) {
        String query = "INSERT INTO patient(name, lastname, dni, age, phone, disease, doctor_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, pat.getName());
            ps.setString(2, pat.getLastname());
            ps.setString(3, pat.getDni());
            ps.setInt(4, pat.getAge());
            ps.setString(5, pat.getPhone());
            ps.setString(6, pat.getDisease());
            ps.setInt(7, pat.getDoctor().getId());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    pat.setId(rs.getInt(1)); // ← guardar ID generado
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
        String query = "DELETE FROM patient WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Patient getPatient(int id) {
        String query = "select * from patient where id= ?";
        PreparedStatement ps = null;
        boolean check = false;
        Patient patient = new Patient();
        try {
            ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                check = true;
                patient.setId(rs.getInt("id"));
                patient.setDni(rs.getString("dni"));
                patient.setLastname(rs.getString("lastname"));
                patient.setName(rs.getString("name"));
                patient.setAge(rs.getInt("age"));
                patient.setPhone(rs.getString("phone"));
                patient.setDisease(rs.getString("disease"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (check) {
            return patient;
        } else
            return null;
    }

    @Override
    public List<Patient> getPatients() {
        List<Patient> patient = new ArrayList<>();
        String query = "SELECT * FROM patient";
        try (PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Patient p = new Patient();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setLastname(rs.getString("lastname"));
                p.setDni(rs.getString("dni"));
                p.setAge(rs.getInt("age"));
                p.setPhone(rs.getString("phone"));
                p.setDisease(rs.getString("disease"));
                patient.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patient;
    }

    @Override
    public boolean update(Patient pat) {
        if (ifExistPatient(pat.getId())){
            String query
                    = "UPDATE `patient` SET `name`=?,`lastname`=?,`dni`=?,`phone`=?,`age`=?, `disease`=? WHERE id=?";
            PreparedStatement ps = null;
            int rs = 0;
            try {
                ps = con.prepareStatement(query);
                ps.setString(1,pat.getName());
                ps.setString(2,pat.getLastname());
                ps.setString(3,pat.getDni());
                ps.setString(4,pat.getPhone());
                ps.setInt(5,pat.getAge());
                ps.setString(6,pat.getDisease());
                ps.setInt(7, pat.getId());
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

    private boolean ifExistPatient(int id) {
        String query = "select * from patient where id= ?";
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
}
