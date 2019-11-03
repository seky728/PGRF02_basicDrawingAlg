package filler;

import Renderer.Renderer;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface Filler {


    void Fill();
    void setBorderColor(Color borderColor);
    void setImg(BufferedImage img);

}
