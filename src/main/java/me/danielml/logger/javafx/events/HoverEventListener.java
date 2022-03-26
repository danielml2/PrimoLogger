package me.danielml.logger.javafx.events;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.chart.Axis;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
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
        if(controller.getSelectedEntries() == null || controller.getSelectedEntries().isEmpty()) return;
        Point2D mousePos = new Point2D(event.getSceneX(), event.getSceneY());
        Axis<Number> xAxis = controller.getMainChart().getXAxis();
        double mouseX = xAxis.getValueForDisplay(xAxis.sceneToLocal(mousePos).getX()).doubleValue();

        StringBuilder finalText = new StringBuilder();
        finalText.append("Time: " + mouseX + "s \n");
        for (String s : controller.getSelectedEntries()) {


            Map<Double, Number> data = controller.getLoadedDataByRecording(s);
            if(data == null) continue;

            if(data.getOrDefault(mouseX,-Double.MAX_VALUE).doubleValue() != -Double.MAX_VALUE)
            {
                finalText.append(s + ": ").append(data.get(mouseX)).append(" \n");
                continue;
            }

            Point2D nearestPoint = new Point2D(0, 0);
            Point2D secondNearest = new Point2D(0, 0);

            for (Map.Entry<Double, Number> entry : data.entrySet()) {
                double distance = Math.abs(entry.getKey()-mouseX);
                if (distance < Math.abs(nearestPoint.getX()-mouseX)) {
                    secondNearest = nearestPoint;
                    nearestPoint = new Point2D(entry.getKey(), Double.parseDouble(String.valueOf(entry.getValue())));
                } else if (distance < Math.abs(secondNearest.getX()-mouseX)) {
                    secondNearest = new Point2D(entry.getKey(), Double.parseDouble(String.valueOf(entry.getValue())));
                    break;
                }
            }

            if(controller.getBooleanEntries().contains(s)) {
                Point2D absoluteNearestPoint = (Math.abs(nearestPoint.getX()-mouseX)) < (Math.abs(secondNearest.getX()-mouseX)) ? nearestPoint : secondNearest;

                boolean value = absoluteNearestPoint.getY() == controller.getBooleanTrueNumeric();

                finalText.append(s + ": ").append(value).append(" \n");
                continue;
            }

            double interpY = MathUtil.linearInterpolation(nearestPoint,secondNearest,mouseX);

            finalText.append(s + ": ").append(interpY).append(" \n");
        }
        hoverText.setText(finalText.toString());
    }
}
