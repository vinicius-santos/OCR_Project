package com.datumdroid.android.ocr.simple.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;


/**
 * Classe utilizada para salvar as imagens capturadas no dispositivo móvel
 * @author vinicius
 *
 */
public class SaveImageUtil {
	
	public static void save(Bitmap finalBitmap, Context context) {
	    String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
	    File myDir = new File(root + "/saved_images");
	    myDir.mkdirs();
	    Random generator = new Random();
	    int n = 10000;
	    n = generator.nextInt(n);
	    String fname = "Image-" + n + ".png";
	    File file = new File(myDir, fname);
	    if (file.exists())
	        file.delete();
	    try {
	        FileOutputStream out = new FileOutputStream(file);
	        finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
	        out.flush();
	        out.close();
	    }
	    catch (Exception e) {
	        e.printStackTrace();
	    }
	    MediaScannerConnection.scanFile(context, new String[] { file.toString() }, null, 
	    		new MediaScannerConnection.OnScanCompletedListener() {
	                public void onScanCompleted(String path, Uri uri) {
	                    Log.i("ExternalStorage", "Scanned " + path + ":");
	                    Log.i("ExternalStorage", "-> uri=" + uri);
	                }
	    });
	}
}
