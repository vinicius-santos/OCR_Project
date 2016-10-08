package com.datumdroid.android.ocr.simple.util;

import android.graphics.Bitmap;

import com.datumdroid.android.ocr.simple.OCRActivity;
import com.googlecode.tesseract.android.TessBaseAPI;
import com.googlecode.tesseract.android.TessBaseAPI.PageSegMode;

public class TesseractParametersUtil {

	public static TessBaseAPI create(String DATA_PATH, String lang,
			Bitmap bitmap, OCRActivity ocrActivity) {
		TessBaseAPI baseApi = new TessBaseAPI();
		baseApi.setDebug(true);
		baseApi.init(DATA_PATH, "cocacola+eng+tang_aveia+por");
		baseApi.setVariable("user_words_suffix","eng.user-words");
		baseApi.setPageSegMode(PageSegMode.PSM_SPARSE_TEXT_OSD);
		//bitmap = ImageTreatmentUtil.resize(bitmap, 3840, 2160);bom
		//bitmap.setDensity(585);
		//bitmap = ImageTreatmentUtil.resize(bitmap, 1517, 2700);
		//bitmap = ImageTreatmentUtil.resize(bitmap, 4096, 2160);
		bitmap = ImageTreatmentUtil.resize(bitmap, 2550, 3300);
		//bitmap.setDensity(500);
		baseApi.setImage(bitmap);
		SaveImageUtil.save(bitmap, ocrActivity);
		return baseApi;
	}

	public static TessBaseAPI setParameters(String key, String value,
			TessBaseAPI baseApi) {
		baseApi.setVariable(key, value);
		return baseApi;
	}
}
