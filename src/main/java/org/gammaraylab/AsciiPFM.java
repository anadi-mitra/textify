package org.gammaraylab;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PShape;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import static org.gammaraylab.Main.*;

public class AsciiPFM /*implements PFM*/ {/*
    ArrayList<Glyph> glyphs = new ArrayList<>();

    @Override
    public void setup(PApplet pApplet) {
        File glyphFolder = new File(pApplet.sketchPath("/home/anadi/scripts/glyph/surat_ulu"));
        for (File f : Objects.requireNonNull(glyphFolder.listFiles())) {
            if (f.getName().endsWith(".svg")) {
                PShape s = pApplet.loadShape(f.getAbsolutePath());
                s.disableStyle();  // ignore fill/stroke from SVG file
                float d = calcDensity(s, 10, pApplet); //change the cell size
                glyphs.add(new Glyph(s, d));
            }
        }
    }

    private Glyph pickGlyph(float brightnessVal) {
        float target = map(brightnessVal, 0, CUTOFF, 1, 0); // invert so dark ↔ dense
        Glyph best = glyphs.get(0);
        float minDiff = abs(best.density - target);
        for (Glyph g : glyphs) {
            float d = abs(g.density - target);
            if (d < minDiff) { best = g; minDiff = d; }
        }
        return best;
    }

    @Override
    public void drawArt(PGraphics pGraphics) {
        for (int y = 0; y < img.height; y += CELL_SIZE) {
            for (int x = 0; x < img.width; x += CELL_SIZE) {
                // average brightness
                float avg = avgBrightness(x, y, CELL_SIZE, pApplet);
                Glyph g = pickGlyph(avg);

                pApplet.pushMatrix();
                pApplet.translate(x + (float) CELL_SIZE /2, y + (float) CELL_SIZE /2);
                pApplet.shapeMode(CENTER);
                pApplet.noFill();
//                strokeWeight(1);
                pApplet.shape(g.shape, 0, 0, CELL_SIZE, CELL_SIZE);
                pApplet.popMatrix();
            }
        }
    }

    @Override
    public void exportSVG(PApplet pApplet) {
        pApplet.beginRecord(SVG, "/home/anadi/scripts/svg/output.svg");
        for (int y = 0; y < img.height; y += CELL_SIZE) {
            for (int x = 0; x < img.width; x += CELL_SIZE) {
                // average brightness
                float avg = avgBrightness(x, y, CELL_SIZE, pApplet);
                Glyph g = pickGlyph(avg);

                pApplet.pushMatrix();
                pApplet.translate(x + (float) CELL_SIZE /2, y + (float) CELL_SIZE /2);
                pApplet.shapeMode(CENTER);
                pApplet.noFill();
//                strokeWeight(1);
                pApplet.shape(g.shape, 0, 0, CELL_SIZE, CELL_SIZE);
                pApplet.popMatrix();
            }
        }
        pApplet.endRecord(); // export to output.svg
    }

    @Override
    public void generateArt(PApplet pApplet) {

    }

    private float calcDensity(PShape s, int size, PApplet pApplet) {
        PGraphics pg = pApplet.createGraphics(size, size);
        pg.beginDraw();
        pg.background(255);
        pg.shapeMode(CENTER);
        pg.shape(s, (float) size /2, (float) size /2, size, size);
        pg.endDraw();

        pg.loadPixels();
        int darkCount=0;
        for (int c:pg.pixels)
            if (pApplet.brightness(c)<128)
                darkCount++;
        return (float)darkCount/pg.pixels.length;
    }

    private float avgBrightness(int x, int y, int size, PApplet pApplet) {
        float total = 0; int count = 0;
        for (int dy=0; dy<size && (y+dy)<img.height; dy++) {
            for (int dx=0; dx<size && (x+dx)<img.width; dx++) {
                total += pApplet.brightness(img.pixels[(y+dy)*img.width+(x+dx)]);
                count++;
            }
        }
        return total/count;
    }
*/}
