package com.example.hadev.myfrontend.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.data.ImageAdapter;
import com.example.hadev.myfrontend.R;

import java.net.URL;
import java.util.ArrayList;

public class GalleryFragment extends Fragment {

    GridView imageGrid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gallery, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        imageGrid = (GridView) this.getView().findViewById(R.id.gridview);

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                ArrayList<Bitmap> bitmapList = new ArrayList<Bitmap>();
                try {
                    for(int i = 0; i < 6; i++) {
                        bitmapList.add(urlImageToBitmap("https://placeholdit.imgix.net/~text?txtsize=14&txt=150%C3%97150&w=150&h=150"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                GalleryFragment.this.setImages(bitmapList);
            }
        });
        thread.start();
    }

    private void setImages(final ArrayList<Bitmap> bitmapList) {
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageGrid.setAdapter(new ImageAdapter(GalleryFragment.this.getActivity(), bitmapList));
            }
        });
    }

    private Bitmap urlImageToBitmap(String imageUrl) throws Exception {
        Bitmap result = null;
        URL url = new URL(imageUrl);
        if(url != null) {
            result = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        }
        return result;
    }


}
