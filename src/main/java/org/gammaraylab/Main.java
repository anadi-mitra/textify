package org.gammaraylab;

import processing.core.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Main extends PApplet {

    private PGraphics artBuffer;
    private float offsetX = 0f;
    private float offsetY = 0f;
    private float prevMouseX;
    private float prevMouseY;
    private boolean dragging = false;
    private float zoomScale= 1.0f;
    static PImage img;
    static int CUTOFF= 250;
    static int CELL_SIZE = 10;       // initial cell size

    static final int MAX_CELL = 100;
    static final int MIN_CELL = 1;
    static final ArrayList<Line> lines= new ArrayList<>();
    private static final ArrayList<Class<? extends PFM>> pfmList = new ArrayList<>();
    private static final String DEFAULT_EXPORT_LOCATION="/home/anadi/scripts/svg/";

    private PFM currentPFM;
    private short pfmIndex= 0;

    public static void main(String[] args) {
        pfmList.add(DynamicCrossHatch.class);
//        pfmList.add(CrossHatchPFM.class);
//        pfmList.add(AsciiPFM.class);
        PApplet.main(Main.class.getName());
    }

    public void settings() {
        String samplePath = sketchPath(
                "/home/anadi/Downloads/152985.jpg"
                /*"/home/anadi/Programs/pico/sketch/rawImages/wednesday.jpg"*/
        ); // change if needed
        File f0 = new File(samplePath);

        if (f0.exists() && (samplePath.endsWith(".jpg") || samplePath.endsWith(".png"))) {
            img = loadImage(samplePath);
        } else {
            println("File not found or invalid extension: " + samplePath);
            exit();
        }
        size(img.width, img.height);
    }

    public void setup() {
        try {
            currentPFM= pfmList.get(pfmIndex++).newInstance();
        } catch (InstantiationException e) {
            System.err.println("Cannot initialize current PFM object");
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        artBuffer= createGraphics(img.width, img.height);
        currentPFM.setup(this);
        noLoop();
    }

    @Override
    public void draw() {
        artBuffer.beginDraw();
        artBuffer.background(255);
        artBuffer.noFill();
        artBuffer.stroke(0);
        artBuffer.strokeWeight(1);
        artBuffer.translate(width/2f, height/2f);
        artBuffer.scale(zoomScale);
        artBuffer.translate(-width/2f+ offsetX, -height/2f+ offsetY);
        artBuffer.textAlign(CENTER, CENTER);
        currentPFM.drawArt(artBuffer); //draws the art on the canvas
        artBuffer.endDraw();
        image(artBuffer,0,0);
    }

    @Override
    public void keyPressed() {
        if (key == 'n') {
            try {
                currentPFM= pfmList.get((++pfmIndex)%pfmList.size()).newInstance();
                println("Current PFM: "+currentPFM.getClass().getName());
            } catch (InstantiationException e) {
                System.err.println("Cannot initialize current PFM object");
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            currentPFM.setup(this);
        }else if (key == 'k') { //reduces the cell size/ distance
            CELL_SIZE = max(MIN_CELL, CELL_SIZE - 1);
            println("Cell size: " + CELL_SIZE);
            currentPFM.generateArt(this);
        } else if (key == 'l') { //increases the cell size/ distance
            CELL_SIZE = min(MAX_CELL, CELL_SIZE + 1);
            println("Cell size: " + CELL_SIZE);
            currentPFM.generateArt(this);
        } else if (key == 'i') { //reduces the cell size/ distance
            currentPFM.exportSVG(this);
            println("Export complete:");
            println("/home/anadi/scripts/svg/output.svg");

        } else if (key == 'c') {
            if(CUTOFF>100) {
                CUTOFF -= 5;
                currentPFM.generateArt(this);
            }
            else
                System.err.println("minimum cutoff reached");
        } else if (key == 'v') {
            if(CUTOFF<255) {
                CUTOFF += 5;
                currentPFM.generateArt(this);
            }
            else
                System.err.println("Maximum cutoff reached");
        } else if (key == '[') {
            zoomScale*=0.9f;
            redraw();
        } else if (key == ']') {
            zoomScale*=1.1f;
            redraw();
        }
    }

    @Override
    public void mousePressed() {
        dragging = true;
        prevMouseX = mouseX;
        prevMouseY = mouseY;
    }

    @Override
    public void mouseDragged() {
        if (dragging) {
            float dx = mouseX - prevMouseX;
            float dy = mouseY - prevMouseY;

            offsetX += dx / zoomScale; // adjust for zoom
            offsetY += dy / zoomScale;

            prevMouseX = mouseX;
            prevMouseY = mouseY;

            redraw();
        }
    }

    @Override
    public void mouseReleased() {
        dragging = false;
    }

    static void export(String fileName) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
                .append("<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"")
                .append(img.width)
                .append("px\" height=\"")
                .append(img.height)
                .append("px\" version=\"1.1\">\n")
//                .append("<rect width=\"100%\" height=\"100%\" fill=\"white\" />\n")
                .append("<g stroke=\"#312B2B\"  stroke-opacity=\"0.7\" stroke-width=\"0.7\">\n");
        for (Line l : lines) {
            sb.append("<line x1=\"")
                    .append(l.start.x)
                    .append("\" y1=\"")
                    .append(l.start.y)
                    .append("\" x2=\"")
                    .append(l.end.x)
                    .append("\" y2=\"")
                    .append(l.end.y)
                    .append("\"/>\n");
        }
        sb.append("</g>\n")
                .append("</svg>");
        try {
            java.nio.file.Files.write(java.nio.file.Paths.get(DEFAULT_EXPORT_LOCATION+fileName+"_"+getTimestamp()+".svg"), sb.toString().getBytes());
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static String getTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy_HHmmss");
        return sdf.format(new Date());
    }
}
