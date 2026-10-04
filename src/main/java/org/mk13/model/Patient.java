package org.mk13.model;

public class Patient {
    private int id;
    private String name, lastname;
    private String dni;
    private int age;
    private String phone;
    private String disease;

    private Doctor doctor;

    public Patient(int age, String phone, String name, String lastname, int id, String dni, String disease) {
        this.age = age;
        this.phone = phone;
        this.name = name;
        this.lastname = lastname;
        this.id = id;
        this.dni = dni;
        this.disease = disease;
    }

    public Patient(){
        age = 32;
        phone = "610442323";
        name = "Carlos";
        lastname = "Gonzalez";
        id = 1;
        dni = "18543256H";
        disease = "Headache";
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "age=" + age +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", dni='" + dni + '\'' +
                ", phone='" + phone + '\'' +
                ", disease='" + disease + '\'' +
                '}';
    }
}
