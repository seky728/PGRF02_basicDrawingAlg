package drawableObjects.Objects;

import Renderer.Renderer;
import lib.GraphicsLib;

import java.awt.*;

public class RegularPolygon implements Drawable {

    private Point a,b;
    private Color color = Color.GREEN;
    private int edge = 3;

    public RegularPolygon(Color color) {
        this.color = color;
    }

    public void addPoint(Point a, Point b) {
        this.a = a;
        this.b = b;
    }

    public void changeEdge(Point a1, Point b1, Point a2, Point b2) {
        double cathetusA = GraphicsLib.distance(a1, b1);
        double cathetusB = GraphicsLib.distance(a2, b2);

        double hypotenuse = (Math.sqrt(Math.pow(cathetusA, 2) + Math.pow(cathetusB, 2))) / 15;
        if (hypotenuse < 3) hypotenuse = 3;

        setEdge((int) Math.round(hypotenuse));
    }

    @Override
    public void draw(Renderer renderer) {
        if (a == null && b == null) return;

        double x0 = b.getX() - a.getX();
        double y0 = b.getY() - a.getY();

        //renderer.lineDDA(a.getX(), a.getY(), b.getX(), b.getY(), color);

        double circleRadius = 2 * Math.PI;
        double step = circleRadius / (double) edge;

        for (double i = 0; i < circleRadius; i += step) {
            double x = x0 * Math.cos(step) + y0 * Math.sin(step);
            double y = y0 * Math.cos(step) - x0 * Math.sin(step);
            renderer.useDDA((int) x0 + a.getX(), (int) y0 + a.getY(), (int) x + a.getX(), (int) y + a.getY(), color);
            x0 = x;
            y0 = y;
        }

    }

    public RegularPolygon(Point a, Point b, Color color, int edge) {
        this.a = a;
        this.b = b;
        this.color = color;
        this.edge = edge;
    }

    public int getEdge() {
        return edge;
    }

    public void setEdge(int edge) {
        this.edge = edge;
    }

    public Point getA(){return a;}
    public Point getB(){return b;}

    public Color getColor() {
        return color;
    }
}
