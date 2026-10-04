package org.mk13.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.mk13.exception.DataAccessException;
import org.mk13.idao.DoctorDao;
import org.mk13.model.Doctor;
import org.mk13.util.DatabaseConnection;

public class DoctorDaoImpl implements DoctorDao {

    @Override
    public Doctor getDoctor(int id) {
        String query = "SELECT * FROM doctor WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapDoctor(rs) : null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("No se pudo obtener el doctor con id " + id, e);
        }
    }

    @Override
    public boolean add(Doctor doc) {
        String query = "INSERT INTO doctor (name, lastname, dni, salary, speciality) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, doc.getName());
            ps.setString(2, doc.getLastname());
            ps.setString(3, doc.getDni());
            ps.setDouble(4, doc.getSalary());
            ps.setString(5, doc.getSpeciality());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        doc.setId(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new DataAccessException("No se pudo añadir el doctor", e);
        }
    }

    @Override
    public boolean delete(int id) {
        String query = "DELETE FROM doctor WHERE id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("No se pudo borrar el doctor con id " + id, e);
        }
    }

    @Override
    public List<Doctor> getDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        String query = "SELECT * FROM doctor";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                doctors.add(mapDoctor(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("No se pudo obtener la lista de doctores", e);
        }
        return doctors;
    }

    @Override
    public boolean update(Doctor doc) {
        String query = "UPDATE doctor SET name=?, lastname=?, dni=?, salary=?, speciality=? WHERE id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, doc.getName());
            ps.setString(2, doc.getLastname());
            ps.setString(3, doc.getDni());
            ps.setDouble(4, doc.getSalary());
            ps.setString(5, doc.getSpeciality());
            ps.setInt(6, doc.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("No se pudo actualizar el doctor con id " + doc.getId(), e);
        }
    }

    @Override
    public Doctor getDoctorByPatientId(int patientId) {
        String query = "SELECT d.* FROM doctor d INNER JOIN patient p ON d.id = p.doctor_id WHERE p.id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapDoctor(rs) : null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("No se pudo obtener el doctor del paciente con id " + patientId, e);
        }
    }

    private Doctor mapDoctor(ResultSet rs) throws SQLException {
        Doctor doctor = new Doctor();
        doctor.setId(rs.getInt("id"));
        doctor.setName(rs.getString("name"));
        doctor.setLastname(rs.getString("lastname"));
        doctor.setDni(rs.getString("dni"));
        doctor.setSalary(rs.getDouble("salary"));
        doctor.setSpeciality(rs.getString("speciality"));
        return doctor;
    }
}