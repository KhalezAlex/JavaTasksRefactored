package com.example.javatasksref;

import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

public class JavaTasks extends Application {
    private static final String CONFIG = "src/main/resources/db/config/dbConfig.xml";
    private final XML config;

    public JavaTasks() throws FileNotFoundException {
        this.config = new XMLDocument(new File(CONFIG));
    }

    @Override
    public void start(Stage stage) throws IOException {
        setJdbcUrlProperty();
        stageInit(stage);
    }

    private void stageInit(Stage stage) throws IOException {
        Scene scene = sceneInit();

        stage.setTitle("Etalon");
        stage.setScene(scene);

        stage.show();
    }

    private Scene sceneInit() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                .getResource(config
                        .xpath("//app/resources/main-view/xml/text()")
                        .get(0)
                )
        );

        Scene scene = new Scene(fxmlLoader.load());
        scene
                .getStylesheets()
                .add(Objects
                        .requireNonNull(getClass()
                                .getResource(config
                                        .xpath("//app/resources/main-view/css/text()")
                                        .get(0)))
                        .toExternalForm());
        return scene;
    }

    private void setJdbcUrlProperty() {
        String prefix = "jdbc:postgresql://";
        String jdbcUrl = prefix
                .concat(getParameters()
                        .getRaw()
                        .get(1)
                        .replace(":etalon", "/etalon"));
        System.setProperty("jdbcUrl", jdbcUrl);
    }

    public static void main(String[] args) {
        launch(args);
    }
}