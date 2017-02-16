package com.example.hadev.myfrontend.fragment;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hadev.myfrontend.surface.MySurfaceView;

public class ShapeFragment extends Fragment {

    private GLSurfaceView surfaceView;

   /**
    * Called when the activity is first created.
    */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        //return inflater.inflate(R.layout.activity_surface, parent, false);
        return new MySurfaceView(getActivity());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}

