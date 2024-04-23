package com.example.javatasksref.db.service;

import com.example.javatasksref.entity.Patient;

import java.util.List;

public interface PatientService {
    List<Patient> all();
    List<Patient> byQuery(String queryParams);
}
