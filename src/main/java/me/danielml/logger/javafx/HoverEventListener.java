package me.danielml.logger.javafx;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.chart.Axis;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import me.danielml.logger.Main;
import me.danielml.logger.util.MathUtil;

import java.util.Map;

public class HoverEventListener implements EventHandler<MouseEvent> {

    private final Main main;
    private Label text;
    private Axis<Number> numberAxis;
    public HoverEventListener(Main main, Label text) {
        this.main = main;
        this.numberAxis = main.getChart().getXAxis();
        this.text = text;
    }

    @Override
    public void handle(MouseEvent event) {
        Point2D mousePos = new Point2D(event.getSceneX(), event.getSceneY());
        Axis<Number> xAxis = numberAxis;
        double mouseX = xAxis.getValueForDisplay(xAxis.sceneToLocal(mousePos).getX()).doubleValue();

        StringBuilder finalText = new StringBuilder();

        for (Map<Double, Double> data : main.getCurrentData()) {

            Point2D nearestPoint = new Point2D(0, 0);
            Point2D secondNearest = new Point2D(999, 999);
            for (Map.Entry<Double, Double> entry : data.entrySet()) {
                double distance = MathUtil.axisDistance(entry.getKey(), mouseX);
                if (distance < MathUtil.axisDistance(nearestPoint.getX(), mouseX)) {
                    secondNearest = nearestPoint;
                    nearestPoint = new Point2D(entry.getKey(), entry.getValue());
                } else if (distance < MathUtil.axisDistance(secondNearest.getX(), mouseX) && MathUtil.axisDistance(nearestPoint.getX(), mouseX) > distance) {
                    secondNearest = new Point2D(entry.getKey(), entry.getValue());
                }
            }

            double interpY = MathUtil.linearInterpolation(nearestPoint.getX(),nearestPoint.getY(),secondNearest.getX(),secondNearest.getY(),mouseX);

            finalText.append("(").append(mouseX).append(",").append(interpY).append(") \n");
        }
        text.setText(finalText.toString());

    }
}
