package filler;

import drawableObjects.Objects.IrregularPolygon;
import drawableObjects.Objects.Point;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ScanLineFill implements Filler {

    private IrregularPolygon polygon;
    private int minY;
    private int maxY;
    List<Line> lines = new ArrayList<>();



    public void setBorders(IrregularPolygon polygon){
        this.polygon = polygon;

        for (int i = 0; i <= polygon.getSize()-1; i++){
            if (i != polygon.getSize()) {
                lines.add(new Line(polygon.getPoints().get(i),polygon.getPoints().get(i+1) ));
            } else {
                lines.add(new Line(polygon.getPoints().get(i),polygon.getPoints().get(0) ));
            }
        }

    }

    private void findMaxAndMinY(){
        minY = polygon.getPoints().get(0).getY();
        maxY = polygon.getPoints().get(0).getY();

        for (Point point : polygon.getPoints()){


                if (minY > point.getY()){
                    minY = point.getY();
                }

                if (maxY < point.getY()){
                    maxY = point.getY();
                }
        }
    }



    @Override
    public void Fill() {
        findMaxAndMinY();

        for (int y = minY; y <= maxY; y++){
            List<Integer> intersection = new ArrayList<>();
            for (Line line : lines) {
                if (line.isIntersection(y)){
                    intersection.add(line.getIntersection(y));
                }
                //TODO uspořádat intersection podle velikosti X souřadnice

                for (int i = 0; i < intersection.size(); i += 2){
                    int x1 = intersection.get(i);
                    int x2 = intersection.get(i+1);
                    for (int x = x1; x <= x2; x++){
                       // drawPixel(x,y);
                    }
                }
            }
        }

    }



    @Override
    public void setBorderColor(Color borderColor) {

    }

    @Override
    public void setImg(BufferedImage img) {

    }


    private class Line{
        private Point a,b;

        public Line(Point a, Point b) {
            this.a = a;
            this.b = b;
        }

        private boolean isIntersection(int y){
            return a.getY() < y && a.getY() < b.getY(); //záležý na orientaci -> co když je to rovné?
        }

        private int getIntersection(int y){
            return 0;

        }

/*
        private void cutLine(){}
        private boolean isHorizontal(){}
        private void setHorizontal(){}
*/



    }

}

