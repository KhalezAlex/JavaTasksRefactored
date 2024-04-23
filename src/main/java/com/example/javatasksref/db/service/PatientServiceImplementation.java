package com.example.javatasksref.db.service;

import com.example.javatasksref.db.repository.PatientRepository;
import com.example.javatasksref.entity.Patient;

import java.util.List;

import static com.example.javatasksref.util.QueryWizard.query;

public class PatientServiceImplementation implements PatientService{
    private final PatientRepository repo;

    public PatientServiceImplementation() {
        repo = new PatientRepository();
    }

    @Override
    public List<Patient> all() {
        return repo.all();
    }

    @Override
    public List<Patient> byQuery(String queryParams) {
        return repo.byQuery(query(queryParams));
    }
}
