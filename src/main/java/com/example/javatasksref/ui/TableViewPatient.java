package com.example.javatasksref.ui;

import com.example.javatasksref.db.service.PatientService;
import com.example.javatasksref.db.service.PatientServiceImplementation;
import com.example.javatasksref.util.customCellFactory.CustomCellFactory;
import com.example.javatasksref.entity.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.lang.reflect.Field;
import java.util.*;

public class TableViewPatient extends TableView<Patient> {
    ObservableList<Patient> patients;

    public TableViewPatient() throws ClassNotFoundException {
        super();
        this.setMinSize(800, 700);
        this.patients = getOl();
        this.getColumns().addAll(getColumns(patients));
        this.setItems(getOl());
        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private List<TableColumn<Patient, Object>> getColumns(ObservableList<Patient> patients)
            throws ClassNotFoundException {
        Map<String, String> columnNames = columnNames();
        return Arrays.stream(Class.forName(patients
                                .get(0)
                                .getClass()
                                .getName())
                        .getDeclaredFields())
                .map(Field::getName)
                .filter(fieldName -> !fieldName.equals("fio"))
                .map(fn -> getColumnFromField(fn, columnNames.get(fn)))
                .toList();
    }

    private TableColumn<Patient, Object> getColumnFromField(String prop, String colName) {
        TableColumn<Patient, Object> column = new TableColumn<>(colName);
        column.setCellValueFactory(new PropertyValueFactory<>(prop));
        column.setCellFactory(new CustomCellFactory());
        return column;
    }

    private Map<String, String> columnNames() {
        Map<String, String> columnNames = new HashMap<>();
        columnNames.put("num", "Номер");
        columnNames.put("snils", "СНИЛС");
        columnNames.put("sex", "Пол");
        columnNames.put("fioAbbr", "ФИО");
        columnNames.put("birthDate", "Дата рожд.");
        columnNames.put("age", "Возраст");
        columnNames.put("policy", "Полис");
        columnNames.put("finSource", "Ист.Фин.");
        return columnNames;
    }

    private ObservableList<Patient> getOl() {
        PatientService patientService = new PatientServiceImplementation();
        return FXCollections
                .observableArrayList(patientService.all());
    }
}