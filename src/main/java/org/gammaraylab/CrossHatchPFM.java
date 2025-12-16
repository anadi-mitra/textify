package org.gammaraylab;


import processing.core.PApplet;
import processing.core.PGraphics;

import static org.gammaraylab.Main.*;

public class CrossHatchPFM /*implements PFM*/ {/*

    @Override
    public void setup(PApplet pApplet) {
        // nothing to load (no glyphs)
    }

    @Override
    public void generateArt(PApplet pApplet) {
        lines.clear();  //clean the array from previous line segments
        Point start, end;
        Line line;
        pApplet.stroke(0);
        pApplet.noFill();

        for (int y = 0; y < img.height; y += CELL_SIZE) {
            for (int x = 0; x < img.width; x += CELL_SIZE) {
                float avg = avgBrightness(x, y, CELL_SIZE, pApplet);

                // map brightness to number of hatch layers (0–4)
                int layers = (int) pApplet.map(avg, 0, CUTOFF, 4, 0);
                layers = pApplet.constrain(layers, 0, 4);

                float x0 = x;
                float y0 = y;
                float x1 = x + CELL_SIZE;
                float y1 = y + CELL_SIZE;

                // draw different hatch directions based on layers
                if (layers >= 1) { // vertical
                    for (float xx = x0; xx <= x1; xx += CELL_SIZE / 4f) {
                        if (pApplet.random(1) > 0.1f) { // small chance to skip (breaks)
                            pApplet.line(xx, y0, xx, y1);
                            start= new Point(xx, y0);
                            end= new Point(xx, y1);
                            line= new Line(start,end);
                            lines.add(line);
                        }

                    }
                }
                if (layers >= 2) { // horizontal
                    for (float yy = y0; yy <= y1; yy += CELL_SIZE / 4f) {
                        if (pApplet.random(1) > 0.1f) {
                            pApplet.line(x0, yy, x1, yy);
                            start= new Point(x0, yy);
                            end= new Point(x1, yy);
                            line= new Line(start,end);
                            lines.add(line);
                        }
                    }
                }
                if (layers >= 3) { // diagonal ↘
                    for (float d = -CELL_SIZE; d <= CELL_SIZE; d += CELL_SIZE / 4f) {
                        if (pApplet.random(1) > 0.1f) {
                            pApplet.line(x0, y0 + d, x1, y1 + d);
                            start= new Point(x0, y0+d);
                            end= new Point(x1, y1+d);
                            line= new Line(start,end);
                            lines.add(line);
                        }
                    }
                }
                if (layers >= 4) { // anti-diagonal ↙
                    for (float d = 0; d <= 2 * CELL_SIZE; d += CELL_SIZE / 4f) {
                        if (pApplet.random(1) > 0.1f) {
                            pApplet.line(x0, y1 - d, x1, y0 - d);
                            start= new Point(x0, y1-d);
                            end= new Point(x1, y0-d);
                            line= new Line(start,end);
                            lines.add(line);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void exportSVG(PApplet pApplet) {
        export("output1");
    }

    @Override
    public void drawArt(PGraphics pg) {
        pg.stroke(0);
        pg.noFill();
        for (Line l : lines) {
            pg.line(l.start.x, l.start.y, l.end.x, l.end.y);
        }
    }

    private float avgBrightness(int x, int y, int size, PApplet pApplet) {
        float total = 0;
        int count = 0;
        for (int dy = 0; dy < size && (y + dy) < img.height; dy++) {
            for (int dx = 0; dx < size && (x + dx) < img.width; dx++) {
                total += pApplet.brightness(img.pixels[(y + dy) * img.width + (x + dx)]);
                count++;
            }
        }
        return count == 0 ? 255 : total / count;
    }
*/}
