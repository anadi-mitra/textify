package org.gammaraylab;

import processing.core.PShape;

public class Glyph {
    PShape shape;
    float density;

    public Glyph(PShape shape, float density) {
        this.shape = shape;
        this.density = density;
    }
}
