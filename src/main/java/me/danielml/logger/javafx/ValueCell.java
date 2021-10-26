package me.danielml.logger.javafx;

import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class ValueCell extends ListCell<String> {

    private HBox box;
    private Label name;
    private CheckBox checkBox;
    private Pane space;
    private String category,value;
    private GUIController controller;

    public ValueCell(GUIController controller, String category) {
        this.controller = controller;
        this.category = category;
        box = new HBox();
        box.setAlignment(Pos.CENTER);
        space = new Pane();
        HBox.setHgrow(space, Priority.ALWAYS);
        name = new Label();
        name.setText("empty");

        checkBox = new CheckBox();
        checkBox.setOnAction(event -> {
            if(checkBox.isSelected())
                controller.addChartData(category,value);
            else
                controller.removeChartData(category,value);
        });
        box.getChildren().addAll(name,space,checkBox);
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        setText(null);
        if(empty) {
            setGraphic(null);
        } else {
            setGraphic(box);
            name.setText(item);
            this.value = item;
            checkBox.setSelected(controller.isSelected(category,value));
            if(checkBox.isSelected())
                controller.addChartData(category,value);
        }
    }
}
