package com.datumdroid.android.ocr.simple.util;


import java.util.ArrayList;
import java.util.List;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.IpPrefix;

import com.datumdroid.android.ocr.simple.OCRActivity;


/**
 * Classe util utilizada para tratar a imagem.
 * @author vinicius
 *
 */
public class ImageTreatmentUtil {
	
	public static Bitmap setDensityImage(Bitmap bitmap, int density){
		bitmap.setDensity(density);
		return bitmap;
	}
	
	public static Bitmap createBitMapImage(String _path){
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;
		Bitmap bitmap = BitmapFactory.decodeFile(_path, options);
		return bitmap;
	}
	
	
	public static Bitmap rotateImage(Bitmap bitmap, int rotate){
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix mtx = new Matrix();
		mtx.preRotate(rotate);
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
		return bitmap;
	}
	
	public static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
	    int width = image.getWidth();
	    int height = image.getHeight();
	    float ratioBitmap = (float) width / (float) height;
	    float ratioMax = (float) maxWidth / (float) maxHeight;

	    int finalWidth = maxWidth;
	    int finalHeight = maxHeight;
	    if (ratioMax > 1) {
	        finalWidth = (int) ((float)maxHeight * ratioBitmap);
	    } else {
	        finalHeight = (int) ((float)maxWidth / ratioBitmap);
	    }
	    image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
	    return image;
	}
	
	
	
	public static Bitmap toGrayScale(Bitmap bitmap)
	{        
	    int width, height;
	    height = bitmap.getHeight();
	    width = bitmap.getWidth();    
	    Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
	    Canvas c = new Canvas(bmpGrayscale);
	    Paint paint = new Paint();
	    ColorMatrix cm = new ColorMatrix();
	    cm.setSaturation(0);
	    ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
	    paint.setColorFilter(f);
	    c.drawBitmap(bitmap, 0, 0, paint);
	    return bmpGrayscale;
	}
	
	public static Bitmap toBlackAndWhite(Bitmap bitmap) {
	    int width = bitmap.getWidth();
	    int height = bitmap.getHeight();
	    Bitmap bmOut = Bitmap.createBitmap(width, height, bitmap.getConfig());
	    int A, R, G, B;
	    int pixel;
	    for (int x = 0; x < width; ++x) {
	        for (int y = 0; y < height; ++y) {
	            pixel = bitmap.getPixel(x, y);
	            A = Color.alpha(pixel);
	            R = Color.red(pixel);
	            G = Color.green(pixel);
	            B = Color.blue(pixel);
	            int gray = (int) (0.2989 * R + 0.5870 * G + 0.1140 * B);
	            if (gray > 128) 
	                gray = 255;
	            else
	                gray = 0;
	            bmOut.setPixel(x, y, Color.argb(A, gray, gray, gray));
	        }
	    }
	    return bmOut;
	}
	
	private static Bitmap configBlackWhite(Bitmap bitmap, Mat image, OCRActivity ocrActivity){
		Utils.bitmapToMat(bitmap, image);
		Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY); // tom de cinza
		Utils.matToBitmap(image, bitmap, true);
		SaveImageUtil.save(bitmap, ocrActivity);

		
		Imgproc.GaussianBlur(image, image, new Size(7, 7), 0);// gaussian para ajudar no background
		Utils.matToBitmap(image, bitmap, true);
		SaveImageUtil.save(bitmap, ocrActivity);

		Imgproc.adaptiveThreshold(image, image, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 5, 4);// preto e branco
	
		Utils.matToBitmap(image, bitmap, true);
		SaveImageUtil.save(bitmap, ocrActivity);

		
		Imgproc.dilate(image, image, Imgproc.getStructuringElement(Imgproc.MORPH_DILATE, new Size(2,2))); //aumentar um pouco a letra 
		Utils.matToBitmap(image, bitmap, true);
		SaveImageUtil.save(bitmap, ocrActivity);
//		
		
		
		
//		Imgproc.adaptiveThreshold(image, image, 127, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 11, 11); 
//		Imgproc.adaptiveThreshold(image, image, 127, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 11, 8); 
		//Imgproc.adaptiveThreshold(image, image, 127, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 11, 4); 
		//Imgproc.threshold(image, image, 50, 255, Imgproc.THRESH_BINARY_INV);
		//Imgproc.dilate(image, image, Imgproc.getStructuringElement(Imgproc.MORPH_DILATE, new Size(9,9)));        
		//Imgproc.morphologyEx(image, image, Imgproc.MORPH_TOPHAT, image);
		//duas dessa imagem fica brancs se tiver fundo brnaco
		//{
			//Imgproc.threshold(image, image, 50, 255, Imgproc.THRESH_BINARY_INV);
		//}
		//Imgproc.GaussianBlur(image, image, new Size(7, 7), 0);
		//Imgproc.threshold(image, image, 50, 255, Imgproc.THRESH_BINARY_INV);
		//Imgproc.drawContours(image, contours, contourIdx, color);
		//Utils.matToBitmap(image, bitmap, true);
		//SaveImageUtil.save(bitmap, ocrActivity);
//		Mat blurred = new Mat();

//		

		//Imgproc.adaptiveThreshold(image, image, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 5, 4);
		
//		int erosion_size = 5;
//        int dilation_size = 5;
//		//Mat image2 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new  Size(2*erosion_size + 1, 2*erosion_size+1));
//		Imgproc.erode(image, image, image);
//		Utils.matToBitmap(image, bitmap, true);
//		SaveImageUtil.save(bitmap, ocrActivity);
		
		
		
		
		//erosin dilatation
//		nt erosion_size = 1;
//        //int dilation_size = 4;
//        
//        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new  Size(4*erosion_size + 1, 4*erosion_size+1));
//        Imgproc.erode(image, image, element);
//		
//		
//		//Imgproc.dilate(image, image, Imgproc.getStructuringElement(Imgproc.MORPH_DILATE, new Size(9,2)));        
//		//Imgproc.morphologyEx(image, image, Imgproc.MORPH_TOPHAT, image);
//		//duas dessa imagem fica brancs se tiver fundo brnaco
//		//{
//			Imgproc.threshold(image, image, 50, 255, Imgproc.THRESH_BINARY_INV);
//		//}
//		Imgproc.GaussianBlur(image, image, new Size(7, 7), 0);
//		Utils.matToBitmap(image, bitmap, true);
//		
		
		//Mat element1 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new  Size(2*dilation_size + 1, 2*dilation_size+1));
        //Imgproc.dilate(source, destination, element1);
		
		
		
		return bitmap;
		
	}

	public static Bitmap  imageConfig(Bitmap bitmap, Mat image, OCRActivity ocrActivity, String configSelected){
		
		if(configSelected.equals("Imagem preto e branco")){
			return configBlackWhite(bitmap, image, ocrActivity);
		}else{
			return configColored(bitmap, image, ocrActivity);
		}
	}

	private static Bitmap configColored(Bitmap bitmap, Mat image,OCRActivity ocrActivity) {
		Utils.bitmapToMat(bitmap, image);
		Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);
		Utils.matToBitmap(image, bitmap, true);
		SaveImageUtil.save(bitmap, ocrActivity);
		
		Imgproc.resize(image, image, image.size(), 0.3, 0.3,
                Imgproc.INTER_LINEAR);
		
		Utils.matToBitmap(image, bitmap, true);
		SaveImageUtil.save(bitmap, ocrActivity);
		
//
//		
		Imgproc.GaussianBlur(image, image, new Size(7, 7), 0);// gaussian para ajudar no background
		Utils.matToBitmap(image, bitmap, true);
		SaveImageUtil.save(bitmap, ocrActivity);
//
//		Imgproc.adaptiveThreshold(image, image, 200, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 5, 4);// preto e branco
		Imgproc.adaptiveThreshold(image, image, 200, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 5, 4);// preto e branco
//		Imgproc.adaptiveThreshold(image, image, 200, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 5, 4);// preto e branco
//		Imgproc.adaptiveThreshold(image, image, 200, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 5, 4);// preto e branco
//		
//		Utils.matToBitmap(image, bitmap, true);
//		SaveImageUtil.save(bitmap, ocrActivity);
		
		   Mat src = new Mat(bitmap.getWidth(), bitmap.getHeight(), CvType.CV_8UC4);
		    Utils.bitmapToMat(bitmap, src);

		    // init new matrices
		    Mat dst = new Mat(bitmap.getWidth(), bitmap.getHeight(), CvType.CV_8UC4);
		    Mat tmp = new Mat(bitmap.getWidth(), bitmap.getHeight(), CvType.CV_8UC4);
		    Mat alpha = new Mat(bitmap.getWidth(), bitmap.getHeight(), CvType.CV_8UC4);

		    // convert image to grayscale
		    Imgproc.cvtColor(src, tmp, Imgproc.COLOR_BGR2GRAY);

		    // threshold the image to create alpha channel with complete transparency in black background region and zero transparency in foreground object region.
		    Imgproc.threshold(tmp, alpha, 100, 255, Imgproc.THRESH_BINARY);
		    Imgproc.threshold(alpha, tmp, 230, 255, Imgproc.THRESH_BINARY);
		    

		    // split the original image into three single channel.
		    List<Mat> rgb = new ArrayList<Mat>(3);
		    Core.split(src, rgb);

		    // Create the final result by merging three single channel and alpha(BGRA order)
		    List<Mat> rgba = new ArrayList<Mat>(4);
		    rgba.add(rgb.get(0));
		    rgba.add(rgb.get(1));
		    rgba.add(rgb.get(2));
		    rgba.add(tmp);
		    Core.merge(rgba, dst);
		    
		    Utils.matToBitmap(dst, bitmap, true);
			SaveImageUtil.save(bitmap, ocrActivity);
			
			
			Imgproc.threshold(dst, dst, 100, 255, Imgproc.THRESH_BINARY_INV);			
			Utils.matToBitmap(dst, bitmap, true);
			SaveImageUtil.save(bitmap, ocrActivity);
			
			Imgproc.GaussianBlur(dst, dst, new Size(7, 7), 0);// gaussian para ajudar no background
			Utils.matToBitmap(dst, bitmap, true);
			SaveImageUtil.save(bitmap, ocrActivity);
			
			
		
			
			
			
//			Imgproc.threshold(dst, dst, 240, 255, Imgproc.THRESH_BINARY_INV);			
//			Utils.matToBitmap(dst, bitmap, true);
//			SaveImageUtil.save(bitmap, ocrActivity);
		return bitmap;
	}

	
	
}

