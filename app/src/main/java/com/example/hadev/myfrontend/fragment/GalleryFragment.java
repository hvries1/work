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

        ArrayList<Bitmap> bitmapList = new ArrayList<Bitmap>();
        for(int i = 0; i < 6; i++) {
            bitmapList.add(BitmapFactory.decodeResource(null, R.mipmap.placeholder));
        }
        imageGrid.setAdapter(new ImageAdapter(this.getActivity(), bitmapList));
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
