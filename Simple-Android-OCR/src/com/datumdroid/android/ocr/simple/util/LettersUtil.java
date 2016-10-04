package com.datumdroid.android.ocr.simple.util;

import java.text.Normalizer;
import java.util.regex.Pattern;


public class LettersUtil {
	  public static String unaccented(String str) {
	        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD); 
	        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
	        return pattern.matcher(nfdNormalizedString).replaceAll("");
	    }
	  }
	
	

