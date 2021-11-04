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

    /**
     * Represents each JavaFX cell in the ListView for the value list.
     * @param controller - Main GUI's Controller
     * @param category - The category the value's from
     */
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
                controller.addDataToChart(category,value);
            else
                controller.removeDataFromChart(category,value);
        });
        box.getChildren().addAll(name,space,checkBox);
    }

    /**
     * Called when the ListView cell gets updated & when first added
     * @param item - The string value of the list
     * @param empty - If it's an empty cell or not
     */
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
                controller.addDataToChart(category,value);
        }
    }
}
