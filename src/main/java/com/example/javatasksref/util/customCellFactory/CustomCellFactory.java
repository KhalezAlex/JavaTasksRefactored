package com.example.javatasksref.util.customCellFactory;

import com.example.javatasksref.entity.Patient;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.time.LocalDate;
import java.util.Objects;

import static com.example.javatasksref.util.PatientAdapter.getDateStringFromSQLFormat;

public class CustomCellFactory implements Callback<TableColumn<Patient, Object>, TableCell<Patient, Object>> {
    @Override
    public TableCell<Patient, Object> call(TableColumn<Patient, Object> col) {
        return new TableCell<>() {
            @Override
            public void updateItem(Object field, boolean empty) {
                super.updateItem(field, empty);
                this.setStyle("-fx-alignment: center");
                if (field != null) {
                    setCell(this, field);
                }
            }
        };
    }

    private void setCell(TableCell<Patient, Object> cell, Object field) {
        String str = field.toString();
        if (!str.equals("1") && !str.equals("2") && !str.equals("3")) {
            cell.setText(field.toString());
        }
        if (field instanceof LocalDate) {
            cell.setText(getDateStringFromSQLFormat((LocalDate) field));
        }
        colorRowsBySex(cell, field);
        customizeFioCell(cell, field);
        customizeFinSourceCell(cell, field);
    }

    private void customizeFioCell(TableCell<Patient, Object> cell, Object field) {
        String[] tmp = field.toString().split(" ");
        // костыль. выяснить, можно ли по ячейке достучаться до названия столбца
        if (tmp.length == 3 && !tmp[1].equals("-") &&
                cell.getTableRow() != null && cell.getTableRow().getItem() != null) {
            cell.setTooltip(new Tooltip(cell.getTableRow().getItem().getFio()));
            cell.setStyle("-fx-font-weight: 700; -fx-alignment: center");
        }
    }

    private void customizeFinSourceCell(TableCell<Patient, Object> cell, Object field) {
        if (field.toString().equals("1")) {
            setFinSourceCellGraphics(cell, "oms.png", "ОМС");
        }
        if (field.toString().equals("2")) {
            setFinSourceCellGraphics(cell, "dms.png", "ДМС");
        }
        if (field.toString().equals("3")) {
            setFinSourceCellGraphics(cell, "cash.png", "Наличный расчет");
        }
    }

    private void setFinSourceCellGraphics(TableCell<Patient, Object> cell, String img, String tooltip) {
        HBox graphicContainer = new HBox();
        graphicContainer.setAlignment(Pos.CENTER);
        ImageView iv = new ImageView(new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/com/example/javatasksref/img/".concat(img)))));
        iv.setFitHeight(25);
        iv.setPreserveRatio(true);
        graphicContainer.getChildren().add(iv);
        cell.setTooltip(new Tooltip(tooltip));
        cell.graphicProperty().bind(Bindings.when(cell.emptyProperty()).then((Node) null).otherwise(graphicContainer));
    }


    private void colorRowsBySex(TableCell<Patient, Object> cell, Object field) {
        if (cell.getTableRow() != null) {
            if (field.toString().equals("МУЖ")) {
                cell.getTableRow().setStyle("-fx-background-color: #7c71ff");
            }
            if (field.toString().equals("ЖЕН")) {
                cell.getTableRow().setStyle("-fx-background-color: #ee8fc1");
            }
        }
    }
}
