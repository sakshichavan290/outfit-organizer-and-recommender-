
package com.example.outfitorganizer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.IOException;

public class BitmapUtils {

    public static Bitmap decodeFile(File imageFile) throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false; // Read the full image
        return BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
    }
}
