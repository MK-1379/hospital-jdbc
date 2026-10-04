package org.mk13.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.mk13.exception.DataAccessException;
import org.mk13.idao.PatientDao;
import org.mk13.model.Patient;
import org.mk13.util.DatabaseConnection;

public class PatientDaoImpl implements PatientDao {

    @Override
    public boolean add(Patient pat) {
        String query = "INSERT INTO patient(name, lastname, dni, age, phone, disease, doctor_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, pat.getName());
            ps.setString(2, pat.getLastname());
            ps.setString(3, pat.getDni());
            ps.setInt(4, pat.getAge());
            ps.setString(5, pat.getPhone());
            ps.setString(6, pat.getDisease());
            ps.setInt(7, pat.getDoctor().getId());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        pat.setId(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new DataAccessException("No se pudo añadir el paciente", e);
        }
    }

    @Override
    public boolean delete(int id) {
        String query = "DELETE FROM patient WHERE id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("No se pudo borrar el paciente con id " + id, e);
        }
    }

    @Override
    public Patient getPatient(int id) {
        String query = "SELECT * FROM patient WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapPatient(rs) : null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("No se pudo obtener el paciente con id " + id, e);
        }
    }

    @Override
    public List<Patient> getPatients() {
        List<Patient> patients = new ArrayList<>();
        String query = "SELECT * FROM patient";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                patients.add(mapPatient(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("No se pudo obtener la lista de pacientes", e);
        }
        return patients;
    }

    @Override
    public boolean update(Patient pat) {
        String query = "UPDATE patient SET name=?, lastname=?, dni=?, phone=?, age=?, disease=? WHERE id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, pat.getName());
            ps.setString(2, pat.getLastname());
            ps.setString(3, pat.getDni());
            ps.setString(4, pat.getPhone());
            ps.setInt(5, pat.getAge());
            ps.setString(6, pat.getDisease());
            ps.setInt(7, pat.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("No se pudo actualizar el paciente con id " + pat.getId(), e);
        }
    }

    private Patient mapPatient(ResultSet rs) throws SQLException {
        Patient patient = new Patient();
        patient.setId(rs.getInt("id"));
        patient.setName(rs.getString("name"));
        patient.setLastname(rs.getString("lastname"));
        patient.setDni(rs.getString("dni"));
        patient.setAge(rs.getInt("age"));
        patient.setPhone(rs.getString("phone"));
        patient.setDisease(rs.getString("disease"));
        return patient;
    }
}