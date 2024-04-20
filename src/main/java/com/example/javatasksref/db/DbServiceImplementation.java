package com.example.javatasksref.db;

import com.example.javatasksref.entity.Patient;

import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedList;

public class DbServiceImplementation implements DbService {
    private final Connection connection;

    public DbServiceImplementation() {
        final String configPath = "src/main/resources/db/config/dbConfig.xml";
        try {
            XML config = new XMLDocument(new File(configPath));
            connection = DriverManager.getConnection(
                    System.getProperty("jdbcUrl"),
                    config.xpath("//app/db/username/text()").get(0),
                    config.xpath("//app/db/password/text()").get(0)
            );
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public LinkedList<Patient> all() throws SQLException {
        String query = "SELECT * FROM java_tasks_patient";
        return byQuery(query);
    }

    @Override
    public LinkedList<Patient> byQuery(String query) throws SQLException {
        ResultSet queryResult = null;
        Statement statement = null;
        try {
            statement = this.connection.createStatement();
            queryResult = statement.executeQuery(query);
            return getPatientsListFromResultSet(queryResult);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            assert queryResult != null;
            queryResult.close();
            statement.close();
        }
    }

    private LinkedList<Patient> getPatientsListFromResultSet(ResultSet result) throws SQLException {
        LinkedList<Patient> patients = new LinkedList<>();
        while (result.next()) {
            patients.add(getPatientFromResultSet(result));
        }
        result.close();
        return patients;
    }

    private Patient getPatientFromResultSet(ResultSet resultSet) {
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

    public LocalDate getDate(String date) {
        String[] birthDate = date.split("-");
        return LocalDate.of(Integer.parseInt(birthDate[0]),
                Integer.parseInt(birthDate[1]), Integer.parseInt(birthDate[2]));
    }

    @Override
    public void closeConnection() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}