module com.example.javatasksref {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jcabi.xml;


    opens com.example.javatasksref to javafx.fxml;
    exports com.example.javatasksref;

    opens com.example.javatasksref.ui to javafx.fxml;
    opens com.example.javatasksref.entity to javafx.base;
    opens com.example.javatasksref.util to javafx.base;
}
