package com.example.hadev.myfrontend.surface;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by Hadev on 16-2-2017.
 */

public class MySurfaceView extends GLSurfaceView {

    private final MyRenderer renderer;

    public MySurfaceView(Context context) {
        super(context);

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        renderer = new MyRenderer();

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer);
    }
}
