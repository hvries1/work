package com.example.hadev.myfrontend.surface;

/**
 * Created by Hadev on 16-2-2017.
 */

public class Triangle2 extends VertexObject {

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static float triangleCoords[] = {   // in counterclockwise order:
            1.0f,  0.622008459f, 2.0f,  // top
            0.5f, -0.311004243f, 2.0f, // bottom left
            1.5f, -0.311004243f, 2.0f   // bottom right
    };

    // Set color with red, green, blue and alpha (opacity) values
    float color[] = { 0.63671875f, 0.16953125f, 0.22265625f, 1.0f };

    public Triangle2() {
        super(triangleCoords);
    }

    public void draw(float[] mvpMatrix) {
        super.setColor(color);
        super.draw(mvpMatrix);
    }
}