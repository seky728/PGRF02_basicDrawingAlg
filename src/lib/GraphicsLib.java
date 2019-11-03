package lib;

import drawableObjects.Objects.Point;

public class GraphicsLib {
    public static double distance(Point a, Point b) {
        double x1 = a.getX();
        double y1 = a.getY();
        double x2 = b.getX();
        double y2 = b.getY();

        return Math.sqrt(Math.pow((y2 - y1), 2) + Math.pow((x2 - x1), 2)); // pyth. věta
    }
}
