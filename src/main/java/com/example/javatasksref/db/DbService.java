package com.example.javatasksref.db;

import com.example.javatasksref.entity.Patient;

import java.sql.SQLException;
import java.util.LinkedList;

public interface DbService {
    LinkedList<Patient> all() throws SQLException;
    LinkedList<Patient> byQuery(String query) throws SQLException;
    void closeConnection();
}
