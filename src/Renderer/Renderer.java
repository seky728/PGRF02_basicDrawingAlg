package Renderer;

import ui.MainFrame;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Renderer {
    private BufferedImage img;

    public int getWidth(){
        return img.getWidth();
    }

    public int getPixelColor(int x, int y){
        return img.getRGB(x,y);
    }

    public int getHeight(){
        return img.getHeight();
    }

    public Renderer(BufferedImage img){
        this.img = img;
    }

    public void drawPixel(int x, int y, Color color){
        if (x < 0 || x >= img.getWidth()) return;
        if (y < 0 || y >= img.getHeight()) return;
        img.setRGB(x, y, color.getRGB());
    }

    public void useDDA(int x1, int y1, int x2, int y2, Color color) {
        float x, y, k, g, h;

        int dx = x2 - x1;
        int dy = y2 - y1;

        k = dy / (float) dx;
        boolean axis = Math.abs(dx) > Math.abs(dy); // axis true = x, axis false = y

        if (axis) {
            g = 1;
            h = k;
            if (x1 > x2) {
                int temp = x1;
                x1 = x2;
                x2 = temp;
                temp = y1;
                y1 = y2;
                y2 = temp;
            }
        } else {
            g = 1 / k;
            h = 1;

            if (y1 > y2) {
                int temp = x1;
                x1 = x2;
                x2 = temp;
                temp = y1;
                y1 = y2;
                y2 = temp;
            }
        }
        x = x1;
        y = y1;

        int max = Math.max(Math.abs(dx), Math.abs(dy));
        int count = 0;
        boolean isDraw = true;
        for (int i = 1; i < max + 1; i++) {

            drawPixel(Math.round(x), Math.round(y), color);


//            if (count < 10)
//                count++;
//            else {
//                count = 0;
//                isDraw = !isDraw;
//            }

            x += g;
            y += h;
        }
    }
}
