package drawableObjects.Objects;

import Renderer.Renderer;

import java.awt.*;
//enum - LINE
public class Line implements Drawable{
    private Color color = Color.red;

    private Point a,b;

    public Line(Point a, Point b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public void draw(Renderer renderer) {
        renderer.useDDA(a.getX(),a.getY(),b.getX(),b.getY(),color);
    }
}
