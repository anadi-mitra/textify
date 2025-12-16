package org.gammaraylab;

import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.ArrayList;

import static org.gammaraylab.Main.*;

public class DynamicCrossHatch implements PFM {

    private float[][] gradientMag;
    private float[][] gradientDir;

    @Override
    public void setup(PApplet pApplet) {
        // Precompute gradient magnitude and direction using Sobel
        gradientMag = new float[img.width][img.height];
        gradientDir = new float[img.width][img.height];

        img.loadPixels();
        for (int y = 1; y < img.height - 1; y++) {
            for (int x = 1; x < img.width - 1; x++) {

                // Sobel kernels
                float gx =
                        -1 * pApplet.brightness(img.pixels[(y - 1) * img.width + (x - 1)]) +
                                1 * pApplet.brightness(img.pixels[(y - 1) * img.width + (x + 1)]) +
                                -2 * pApplet.brightness(img.pixels[(y) * img.width + (x - 1)]) +
                                2 * pApplet.brightness(img.pixels[(y) * img.width + (x + 1)]) +
                                -1 * pApplet.brightness(img.pixels[(y + 1) * img.width + (x - 1)]) +
                                1 * pApplet.brightness(img.pixels[(y + 1) * img.width + (x + 1)]);

                float gy =
                        -1 * pApplet.brightness(img.pixels[(y - 1) * img.width + (x - 1)]) +
                                -2 * pApplet.brightness(img.pixels[(y - 1) * img.width + (x)]) +
                                -1 * pApplet.brightness(img.pixels[(y - 1) * img.width + (x + 1)]) +
                                1 * pApplet.brightness(img.pixels[(y + 1) * img.width + (x - 1)]) +
                                2 * pApplet.brightness(img.pixels[(y + 1) * img.width + (x)]) +
                                1 * pApplet.brightness(img.pixels[(y + 1) * img.width + (x + 1)]);

                gradientMag[x][y] = PApplet.sqrt(gx * gx + gy * gy);
                gradientDir[x][y] = PApplet.atan2(gy, gx); // radians
            }
        }
        generateArt(pApplet);   //generate the line array after initialization
    }

    @Override
    public void drawArt(PGraphics artBuffer) {
        for (Line l : new ArrayList<>(lines)) {
            artBuffer.line(l.start.x, l.start.y, l.end.x, l.end.y);
        }
    }

    @Override
    public void exportSVG(PApplet pApplet) {
        export("DynamicCrossHatch");
    }

    @Override
    public void generateArt(PApplet pApplet) {
        lines.clear();  //clean the array from previous line segments
        Point start, end;
        Line line;

        for (int y = 0; y < img.height; y += CELL_SIZE) {
            for (int x = 0; x < img.width; x += CELL_SIZE) {

                float avg = avgBrightness(x, y, CELL_SIZE, pApplet);
                if (avg > CUTOFF) continue; // skip light areas

                // Get gradient direction around the cell center
                float angle = getAngle(x, y);

                // Map brightness to spacing (darker = tighter lines)
                float spacing = PApplet.map(avg, 0, 255, 2, CELL_SIZE / 2f);

                // Draw hatch lines in this cell
                float cx = x + CELL_SIZE / 2f;
                float cy = y + CELL_SIZE / 2f;
//                pApplet.pushMatrix();
//                pApplet.translate(cx, cy);
//                pApplet.rotate(angle);

                for (float ly = -CELL_SIZE / 2f; ly < CELL_SIZE / 2f; ly += spacing) {
                    // local endpoints (before transform)
                    float x1 = -CELL_SIZE / 2f;
                    float y1 = ly;
                    float x2 =  CELL_SIZE / 2f;
                    float y2 = ly;
                    // rotate around origin (0,0)
                    float cosA = PApplet.cos(angle);
                    float sinA = PApplet.sin(angle);
                    float rx1 = x1 * cosA - y1 * sinA;
                    float ry1 = x1 * sinA + y1 * cosA;
                    float rx2 = x2 * cosA - y2 * sinA;
                    float ry2 = x2 * sinA + y2 * cosA;

                    // translate into absolute position
                    rx1 += cx;
                    ry1 += cy;
                    rx2 += cx;
                    ry2 += cy;

//                    pApplet.line(-CELL_SIZE / 2f, ly, CELL_SIZE / 2f, ly);
                    start = new Point(rx1, ry1);
                    end   = new Point(rx2, ry2);
                    line= new Line(start,end);
                    lines.add(line);
                }
//                pApplet.popMatrix();
            }
        }
        pApplet.redraw();
    }

    private float getAngle(int x, int y) {
        int cx = PApplet.constrain(x + CELL_SIZE / 2, 0, img.width - 1);
        int cy = PApplet.constrain(y + CELL_SIZE / 2, 0, img.height - 1);
        float dir = gradientDir[cx][cy];
        float mag = gradientMag[cx][cy];

        // Map direction to one of 4 hatch angles
        float angle;// = quantizeDirection(dir);
        if (mag < 20)
            angle = 0; // default horizontal
        else
            angle = quantizeDirection(dir);
        return angle;
    }

    private float avgBrightness(int x, int y, int size, PApplet pApplet) {
        float total = 0; int count = 0;
        for (int dy = 0; dy < size && (y + dy) < img.height; dy++) {
            for (int dx = 0; dx < size && (x + dx) < img.width; dx++) {
                total += pApplet.brightness(img.pixels[(y + dy) * img.width + (x + dx)]);
                count++;
            }
        }
        return total / count;
    }

    private float quantizeDirection(float angle) {
        // Normalize to [0, PI)
        angle = (angle + PApplet.PI) % PApplet.PI;
        if (angle < PApplet.PI / 8 || angle >= 7 * PApplet.PI / 8) {
            return PApplet.HALF_PI; // horizontal
        } else if (angle < 3 * PApplet.PI / 8) {
            return 3 * PApplet.PI / 4; // diagonal
        } else if (angle < 5 * PApplet.PI / 8) {
            return 0; // vertical
        } else {
            return PApplet.PI / 4; // anti-diagonal
        }
    }
}
