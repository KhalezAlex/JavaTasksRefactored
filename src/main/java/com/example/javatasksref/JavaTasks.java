package com.example.javatasksref;

import com.example.javatasksref.ui.TableViewPatient;
import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class JavaTasks extends Application {
    private static final String CONFIG = "src/main/resources/db/config/dbConfig.xml";

    @Override
    public void start(Stage stage) throws IOException {
        XML config = new XMLDocument(new File(CONFIG));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                .getResource(config
                        .xpath("//app/resources/main-view/xml/text()")
                        .get(0)
                )
        );

        String dbUrl = getParameters().getRaw().get(1).replace(":", "/");
        Scene scene = new Scene(fxmlLoader.load());
        scene
                .getStylesheets()
                .add(Objects
                        .requireNonNull(getClass()
                                .getResource(config
                                        .xpath("//app/resources/main-view/css/text()")
                                        .get(0)))
                        .toExternalForm());
        stage.setTitle("JAVATASKS");
        stage.setScene(scene);

        MainViewController controller = fxmlLoader.getController();
        controller.setDbUrl(dbUrl);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}