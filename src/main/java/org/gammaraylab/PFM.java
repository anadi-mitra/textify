package org.gammaraylab;

import processing.core.PApplet;
import processing.core.PGraphics;

public interface PFM {
    void setup(PApplet pApplet);
    void drawArt(PGraphics pGraphics);
    void exportSVG(PApplet pApplet);
    void generateArt(PApplet pApplet);
}
