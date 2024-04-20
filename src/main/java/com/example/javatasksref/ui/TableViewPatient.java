package com.example.javatasksref.ui;

import com.example.javatasksref.db.DbService;
import com.example.javatasksref.db.DbServiceImplementation;
import com.example.javatasksref.util.CustomCellFactory;
import com.example.javatasksref.entity.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.*;

public class TableViewPatient extends TableView<Patient> {
    private String dbUrl = "jdbc:postgresql://localhost:5432/etalon";

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public TableViewPatient() throws ClassNotFoundException {
        super();
        this.setMinSize(800, 700);
        ObservableList<Patient> ol = getOl();
        this.getColumns().addAll(getColumns(ol));
        this.setItems(getOl());
        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public TableViewPatient(ObservableList<Patient> ol) throws ClassNotFoundException {
        super(ol);
        this.setMinSize(800, 700);
        this.getColumns().addAll(getColumns(ol));
        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private List<TableColumn<Patient, Object>> getColumns(ObservableList<Patient> ol)
            throws ClassNotFoundException {
        Map<String, String> columnNames = columnNames();
        return new ArrayList<>(Arrays.
                stream(Class.forName(ol.get(0).getClass().getName()).getDeclaredFields())
                .map(Field::getName)
                .filter(fn -> !fn.equals("fio"))
                .map(fn -> getColumnFromField(fn, columnNames.get(fn)))
                .toList());
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
        DbService dbService = new DbServiceImplementation(dbUrl);
        try {
            return FXCollections.observableArrayList(dbService.all());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}