package me.danielml.logger.javafx.events;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.stage.PopupWindow;
import me.danielml.logger.Main;
import me.danielml.logger.javafx.GUIController;
import me.danielml.logger.util.MathUtil;

import java.util.Map;

public class HoverEventListener implements EventHandler<MouseEvent> {

    private Label hoverText;
    private GUIController controller;

    /**
     * Constructs the listener for mouse hovering over the graph, to update the text under the chart accordingly
     * @param controller Main GUI's controller
     * @param hoverText Text to be updated
     */
    public HoverEventListener(GUIController controller, Label hoverText) {
        this.controller = controller;
        this.hoverText = hoverText;
    }

    @Override
    public void handle(MouseEvent event) {
        if(controller.getSelectedColumns() == null || controller.getSelectedColumns().isEmpty()) return;
        Point2D mousePos = new Point2D(event.getSceneX(), event.getSceneY());
        Axis<Number> xAxis = controller.getMainChart().getXAxis();
        double mouseX = xAxis.getValueForDisplay(xAxis.sceneToLocal(mousePos).getX()).doubleValue();

        StringBuilder finalText = new StringBuilder();
        finalText.append("Time: " + mouseX + "s \n");
        for (String s : controller.getSelectedColumns()) {

            Map<Double, Double> data = controller.getLoadedDataByRecording(s);
            if(data == null) return;

            Point2D nearestPoint = new Point2D(0, 0);
            Point2D secondNearest = new Point2D(0, 0);

            for (Map.Entry<Double, Double> entry : data.entrySet()) {
                double distance = Math.abs(entry.getKey()-mouseX);
                if (distance < Math.abs(nearestPoint.getX()-mouseX)) {
                    secondNearest = nearestPoint;
                    nearestPoint = new Point2D(entry.getKey(), entry.getValue());
                } else if (distance < Math.abs(secondNearest.getX()-mouseX)) {
                    secondNearest = new Point2D(entry.getKey(), entry.getValue());
                }
            }

            double interpY = MathUtil.linearInterpolation(nearestPoint,secondNearest,mouseX);

            finalText.append(s + ": ").append(interpY).append(" \n");
        }
        hoverText.setText(finalText.toString());
    }
}
