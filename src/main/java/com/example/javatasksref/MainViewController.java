package com.example.javatasksref;

import com.example.javatasksref.db.DbService;
import com.example.javatasksref.db.DbServiceImplementation;
import com.example.javatasksref.entity.Patient;
import com.example.javatasksref.ui.PaneTable;
import com.example.javatasksref.ui.TableViewPatient;
import com.example.javatasksref.util.QueryWizard;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.sql.SQLException;

public class MainViewController {
    @FXML
    public TextArea textAreaQuery;
    @FXML
    public Button buttonSearch;
    @FXML
    public Button buttonCancel;
    @FXML
    public PaneTable paneTable;

    private String dbUrl;

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public void buttonSearchClickHandler() {
        if (textAreaQuery.getText().equals("")) {
            return;
        }
        ObservableList<Patient> selection = getSelection(textAreaQuery.getText());
        updateTable(selection);
    }

    private ObservableList<Patient> getSelection(String queryStr) {
        QueryWizard qw = new QueryWizard(queryStr);
        DbService dbService = new DbServiceImplementation(dbUrl);
        ObservableList<Patient> temp = null;
        try {
            temp = FXCollections.observableArrayList(dbService.byQuery(qw.getQuery()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        dbService.closeConnection();
        return temp;
    }

    private void updateTable(ObservableList<Patient> ol) {
        TableView<Patient> table = (TableViewPatient) paneTable.getChildren().get(0);
        while (table.getItems().size() > 0) {
            table.getItems().remove(0);
        }
        table.getItems().addAll(ol);
        table.refresh();
    }

    public void cancelButtonClickHandler() {
        DbService dbService = new DbServiceImplementation(dbUrl);
        try {
            updateTable(FXCollections.observableArrayList(dbService.all()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        dbService.closeConnection();
        textAreaQuery.setText("");
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
        String temp = textAreaQuery.getText();
        temp = temp.replace("\n", " ").replace("\t", " ");
        textAreaQuery.setText(temp);
        buttonSearchClickHandler();
    }
}