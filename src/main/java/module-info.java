module com.example.javatasksref {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jcabi.xml;

    exports com.example.javatasksref;
    exports com.example.javatasksref.controller;

    opens com.example.javatasksref to javafx.fxml;
    opens com.example.javatasksref.ui to javafx.fxml;
    opens com.example.javatasksref.entity to javafx.base;
    opens com.example.javatasksref.util to javafx.base;
    opens com.example.javatasksref.util.customCellFactory to javafx.base;
    opens com.example.javatasksref.controller to javafx.fxml;
}
