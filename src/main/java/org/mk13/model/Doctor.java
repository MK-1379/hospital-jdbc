package org.mk13.model;

public class Doctor {
    private int id;
    private String name;
    private String lastname;
    private String dni;
    private double salary;
    private String speciality;

    public Doctor(String dni, int id, String lastname, String name, double salary, String speciality) {
        this.dni = dni;
        this.id = id;
        this.lastname = lastname;
        this.name = name;
        this.salary = salary;
        this.speciality = speciality;
    }

    public Doctor(){
        id = 1;
        name = "Pedro";
        lastname = "Perez";
        dni = "12345678G";
        salary = 3000.50;
        speciality = "Pediatry";
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "dni='" + dni + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", salary=" + salary +
                ", speciality='" + speciality + '\'' +
                '}';
    }
}
