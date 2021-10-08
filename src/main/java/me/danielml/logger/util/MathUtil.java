package me.danielml.logger.util;

public class MathUtil {
    public static double axisDistance(double p1, double p2) {
        return Math.max(p1,p2) - Math.min(p1,p2);
    }

    public static double linearInterpolation(double x1, double y1,double x2, double y2, double xBetween) {

        double minX = Math.min(x1,x2), maxX = Math.max(x1,x2);
        double minY = Math.min(y1,y2), maxY = Math.max(y1,y2);


        return minY + ((maxY - minY) * (xBetween-minX))/(maxX - minX);
    }
}
