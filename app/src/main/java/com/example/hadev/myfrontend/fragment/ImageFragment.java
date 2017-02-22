package com.example.hadev.myfrontend.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.hadev.myfrontend.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ImageFragment extends Fragment {

    private static final int PICK_PHOTO_FOR_AVATAR = 101;
    private static final int PICK_PHOTO_FOR_AVATAR_2 = 102;

    private ImageView imageView;

   /**
    * Called when the activity is first created.
    */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.activity_image, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        imageView = (ImageView) this.getView().findViewById(R.id.imageview);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
     }

    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
    }

    public void pickImage2() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("outputX", 256);
        intent.putExtra("outputY", 256);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR_2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PHOTO_FOR_AVATAR
                && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return; //Display an error
            }
            try {
                //InputStream inputStream = context.getContentResolver().openInputStream(data.getData());
                InputStream inputStream = getActivity().getContentResolver().openInputStream(data.getData());
                Bitmap image = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
                imageView.setImageBitmap(image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

         }
        else if (requestCode == PICK_PHOTO_FOR_AVATAR_2) {
            final Bundle extras = data.getExtras();
            if (extras != null) {
                //Get image
                Bitmap image = extras.getParcelable("data");
                imageView.setImageBitmap(image);
            }
        }
    }
}
