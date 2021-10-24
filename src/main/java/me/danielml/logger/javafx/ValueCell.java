package me.danielml.logger.javafx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import me.danielml.logger.Recording;

public class ValueCell extends ListCell<String> {

    private HBox box;
    private Label name;
    private Button button;
    private Pane space;
    private String value;
    private boolean on = false;

    public ValueCell(GUIController controller, String category) {
        box = new HBox();
        box.setAlignment(Pos.CENTER);
        HBox.setHgrow(space, Priority.ALWAYS);
        name.setText("empty");
        CheckBox ch = new CheckBox();

        button.setOnAction(event -> {
            if(!on)
                controller.addChartData(category,value);

        });
        button.setText("");
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
        }
    }
}
