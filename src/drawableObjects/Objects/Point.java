package drawableObjects.Objects;

import Renderer.Renderer;

import java.awt.*;

public class Point implements Drawable{
    private int x,y;
    private Color color = Color.blue;

    public Point(int x, int y, Color color){
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void draw(Renderer renderer) {
        renderer.drawPixel(x,y,color);

    }
}
