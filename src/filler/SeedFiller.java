package filler;


import drawableObjects.Objects.Point;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class SeedFiller implements Filler{
    private Point seed;//possition of seed
    Color borderColor;
    Color newColor;

    private BufferedImage img;

    private List<Point> coloredPoints;



    public SeedFiller(BufferedImage img, List<Point> coloredPoints) {
        this.img = img;
        this.coloredPoints = coloredPoints;
        setSeed(new Point(img.getWidth()/2,img.getHeight()/2));
    }




    private void draw(int x, int y, Color color) {
        if (x < 0 || x >= img.getWidth()) return;
        if (y < 0 || y >= img.getHeight()) return;

        coloredPoints.add(new Point(x, y, color));
    }

    public void setSeed(Point seed) {
        this.seed = seed;
    }

    private int yMaxSize = 10;
    private boolean canDraw = false;

    public boolean isCanDraw() {
        return canDraw;
    }

    public void seedFill(int x, int y, Color borderColor, Color newColor){
        if (x < img.getWidth() && y < img.getHeight() && x > 0 && y > 0 && seed.getY()+yMaxSize >= y && seed.getY()-yMaxSize <= y) {
            if (borderColor.getRGB() != img.getRGB(x, y) && img.getRGB(x, y) != newColor.getRGB()) {

                        draw(x,y,newColor);
                        seedFill(x - 1, y, borderColor, newColor);
                        seedFill(x, y + 1, borderColor, newColor);
                       // seedFill(x + 1, y, borderColor, newColor);
                       // seedFill(x, y - 1, borderColor, newColor);


            }
        }
    }



    @Override
    public void Fill() {
        canDraw = false;
        seedFill(seed.getX(),seed.getY(), borderColor, newColor);
        canDraw = true;
    }

    @Override
    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    @Override
    public void setImg(BufferedImage img) {
        this.img = img;
    }

    public void setNewColor(Color newColor) {
        this.newColor = newColor;
    }
}
