package com.example.fbuserregister;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public abstract class Utils {
    public static final String imgDir = "/images";


    public static Uri writeImage(Context context, Bitmap bmp, String imageFileName)
    {
        Uri imageUri = null;

        if (bmp == null)
        {
            Log.e("Utils.writeImage", "Save Failed - no bitmap " + imageFileName);
            return null;
        }
        OutputStream fOutputStream = null;
        File file = new File(context.getExternalFilesDir(null),
                imageFileName);
        URI javaURI  = file.toURI();
        imageUri = android.net.Uri.parse(javaURI.toString());
        // source: https://stackoverflow.com/questions/12649530/convert-java-net-uri-to-android-net-uri


        try {
            fOutputStream = new FileOutputStream(file);

            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fOutputStream);

            fOutputStream.flush();
            fOutputStream.close();

            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(context, "Save Failed", Toast.LENGTH_SHORT).show();
            Log.e("Utils.writeImage", "Save Failed: " + imageFileName);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Save Failed", Toast.LENGTH_SHORT).show();
            Log.e("Utils.writeImage", "Save Failed: " + imageFileName);
            return null;
        }
        return  imageUri;
    }

    public static void download2ImageView(String downloadUrl, ImageView imgView, Context context)
    {
        if (downloadUrl == null || context == null || imgView == null) {
            // Handle null parameters
            return;
        }
        Uri imageUri = android.net.Uri.parse(downloadUrl);

        if (imageUri != null)
            Picasso.get()
                    .load(imageUri)
                    .placeholder(R.mipmap.ic_launcher)
                    .fit()
                    .centerInside()
                    .into(imgView);
    }

}
