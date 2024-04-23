package com.example.javatasksref.ui;

import javafx.scene.layout.Pane;

public class PaneTable extends Pane {
    public PaneTable() throws ClassNotFoundException {
        super(new TableViewPatient());
    }
}
