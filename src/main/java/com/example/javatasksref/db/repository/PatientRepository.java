package com.example.javatasksref.db.repository;

import com.example.javatasksref.entity.Patient;

import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PatientRepository {
    private final XML config;

    public PatientRepository() {
        try {
            String configPath = "src/main/resources/db/config/dbConfig.xml";
            this.config = new XMLDocument(new File(configPath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Patient> all() {
        String query = "SELECT * FROM java_tasks_patient;";
        return byQuery(query);
    }

    public List<Patient> byQuery(String query) {
        try (Connection connection = setConnection();
             Statement statement = connection.createStatement();
             ResultSet queryResult = statement.executeQuery(query)) {
            return patientsListFromResultSet(queryResult);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Connection setConnection() {
        try {
            return DriverManager.getConnection(
                    System.getProperty("jdbcUrl"),
                    config.xpath("//app/db/username/text()").get(0),
                    config.xpath("//app/db/password/text()").get(0)
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Patient> patientsListFromResultSet(ResultSet queryResult) {
        List<Patient> patients = new ArrayList<>();
        while (true) {
            try {
                if (!queryResult.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            patients.add(patientFromResultSet(queryResult));
        }
        return patients;
    }

    private Patient patientFromResultSet(ResultSet resultSet) {
        try {
            return new Patient(
                    resultSet.getString("fio"),
                    getDate(resultSet.getString("birth_date")),
                    Integer.parseInt(resultSet.getString("sex")),
                    Integer.parseInt(resultSet.getString("num")),
                    resultSet.getString("smo"),
                    resultSet.getString("snils"),
                    resultSet.getString("policy"),
                    Integer.parseInt(resultSet.getString("fin_source")
                    ));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private LocalDate getDate(String date) {
        String[] birthDate = date.split("-");
        return LocalDate.of(Integer.parseInt(birthDate[0]),
                Integer.parseInt(birthDate[1]), Integer.parseInt(birthDate[2]));
    }
}