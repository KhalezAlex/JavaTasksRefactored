package com.example.javatasksref.controller;

import com.example.javatasksref.db.service.PatientService;
import com.example.javatasksref.db.service.PatientServiceImplementation;
import com.example.javatasksref.entity.Patient;
import com.example.javatasksref.ui.PaneTable;
import com.example.javatasksref.ui.TableViewPatient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class MainViewController {
    @FXML
    public TextArea textAreaQuery;
    @FXML
    public Button buttonSearch;
    @FXML
    public Button buttonCancel;
    @FXML
    public PaneTable paneTable;

    private final PatientService patientService;

    public MainViewController() {
        this.patientService = new PatientServiceImplementation();
    }

    public void searchButtonClickHandler() {
        if (!textAreaQuery.getText().equals("")) {
            ObservableList<Patient> selection = byQueryParametersPatientsList(textAreaQuery.getText());
            updateTable(selection);
//            textAreaQuery.setText("");
        }
    }

    public void cancelButtonClickHandler() {
        updateTable(FXCollections.observableArrayList(patientService.all()));
        textAreaQuery.setText("");
    }

    private ObservableList<Patient> byQueryParametersPatientsList(String queryParameters) {
        return FXCollections
                .observableArrayList(patientService.byQuery(queryParameters));
    }

    private void updateTable(ObservableList<Patient> patients) {
        TableView<Patient> table = (TableViewPatient) paneTable.getChildren().get(0);
        while (table.getItems().size() > 0) {
            table.getItems().remove(0);
        }
        table.getItems().addAll(patients);
        table.refresh();
    }

    public void textAreaQueryKeyPressedHandler(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            onTextAreaEnterPressedHandler();
        }
        if (keyEvent.getCode() == KeyCode.ESCAPE) {
            cancelButtonClickHandler();
        }
    }

    private void onTextAreaEnterPressedHandler() {
        String parameters = textAreaQuery
                .getText()
                .replace("\n", "")
                .replace("\t", "");
        textAreaQuery.setText(parameters);
        searchButtonClickHandler();
    }
}