package me.danielml.logger.util;

import javafx.geometry.Point2D;

public class MathUtil {

    /**
     * Linear interpolation between two 2D points. Finds the Y value between the two points given an X value between the two points
     * {@see https://bit.ly/2ZJtQlt}
     * @param x1 Point 1's X
     * @param y1 Point 1's Y
     * @param x2 Point 2's X
     * @param y2 Point 2's Y
     * @param xBetween X value between the two points
     * @return The Y value for the X value between the 2 points, basically the missing Y coordinate
     */
    public static double linearInterpolation(double x1, double y1,double x2, double y2, double xBetween) {

        double minX = Math.min(x1,x2), maxX = Math.max(x1,x2);
        double minY = Math.min(y1,y2), maxY = Math.max(y1,y2);


        return minY + ((maxY - minY) * (xBetween-minX))/(maxX - minX);
    }

    public static double linearInterpolation(Point2D p1, Point2D p2, double xBetween) {
        return linearInterpolation(p1.getX(),p1.getY(),p2.getX(),p2.getY(),xBetween);
    }
}
