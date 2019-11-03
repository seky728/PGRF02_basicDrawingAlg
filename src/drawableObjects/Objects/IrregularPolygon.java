package drawableObjects.Objects;

import Renderer.Renderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IrregularPolygon implements Drawable {
    private List<Point> points;
    private Color color = Color.red;

    public IrregularPolygon(List<Point> points) {
        this.points = points;
    }

    public IrregularPolygon(Color color) {
        points = new ArrayList<>();
        this.color = color;
    }

    public void addPoint(Point point){
        points.add(point);
    }

    public int getSize(){
        return points.size();
    }

    public Point getFirstPoint(){
        return points.get(0);
    }

    public Point getLastPoint(){
        return points.get(getSize()-1);
    }


    @Override
    public void draw(Renderer renderer) {
        int size = getSize();

        if (size == 0) return;

        int firstX = points.get(0).getX();
        int lastX = points.get(size - 1).getX();

        int firstY = points.get(0).getY();
        int lastY = points.get(size - 1).getY();

        if (getSize() == 1) renderer.drawPixel(firstX,firstY,color);

        if (getSize() > 1){
            int toX, toY;
            int prevX = firstX;
            int prevY = firstY;
            for (int i = 0; i < size; i++){
                toX = points.get(i).getX();
                toY = points.get(i).getY();

                renderer.useDDA(prevX,prevY,toX,toY, color);

                prevX = toX;
                prevY = toY;

            }

        }

        if (size > 2) renderer.useDDA(firstX,firstY,lastX,lastY,color);
    }


    public Color getColor(){
        return color;
    }

    public void setColor(Color color){this.color = color;}

    public List<Point> getPoints() {
        return Collections.unmodifiableList(points);
    }
}
