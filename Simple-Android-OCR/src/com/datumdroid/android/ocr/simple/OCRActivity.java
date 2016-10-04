package com.datumdroid.android.ocr.simple;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.datumdroid.android.ocr.simple.util.ImageTreatmentUtil;
import com.datumdroid.android.ocr.simple.util.LettersUtil;
import com.datumdroid.android.ocr.simple.util.SaveImageUtil;
import com.datumdroid.android.ocr.simple.util.TesseractParametersUtil;
import com.googlecode.tesseract.android.TessBaseAPI;
import com.googlecode.tesseract.android.TessBaseAPI.PageSegMode;

import enumeration.RotationEnum;
import enumeration.ValueEnum;
import enumeration.VariablesEnum;

public class OCRActivity extends Activity {
	public static final String NOME_PACOTE = "com.datumdroid.android.ocr.simple";
	public static final String CAMINHO_DADOS = Environment
			.getExternalStorageDirectory().toString() + "/SimpleAndroidOCR/";
	public static final String linguagem = "cocacola";
	private static final String TAG = "SimpleAndroidOCR.java";
	public static final String lang = "eng";
	public static final String lang2 = "cocacola";
	protected Button _botao;
	protected EditText _campo;
	protected String _caminho;
	protected boolean _captura;
	TextToSpeech tts;
	Mat image;
	private RadioGroup radioGroup;
	private RadioButton radioButton;
	protected static final String FOTO_TIRADA = "photo_taken";

	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(int status) {
			switch (status) {
			case LoaderCallbackInterface.SUCCESS: {
				Log.i("OpenCV", "OpenCV loaded successfully");
				image = new Mat();
			}
				break;
			default: {
				super.onManagerConnected(status);
			}
				break;
			}
		}
	};
	
	
	private void createDirectory(String[] paths) {
		for (String path : paths) {
			File dir = new File(path);
			if (!dir.exists()) {
				if (!dir.mkdirs()) {
					Log.v(TAG, "ERROR: Creation of directory " + path + " on sdcard failed");
					return;
				} else {
					Log.v(TAG, "Created directory " + path + " on sdcard");
				}
			}
		}
	}
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		 tts=new TextToSpeech(OCRActivity.this, new TextToSpeech.OnInitListener() {

	            @Override
	            public void onInit(int status) {
	                // TODO Auto-generated method stub
	                if(status == TextToSpeech.SUCCESS){
	                    int result=tts.setLanguage(Locale.US);
	                    if(result==TextToSpeech.LANG_MISSING_DATA ||
	                            result==TextToSpeech.LANG_NOT_SUPPORTED){
	                        Log.e("error", "This Language is not supported");
	                    }
	                   
	                }
	                else
	                    Log.e("error", "Initilization Failed!");
	            }
	        });
		

		String[] paths = new String[] { CAMINHO_DADOS,
				CAMINHO_DADOS + "tessdata/" };
		
		createDirectory(paths);
		

		
		//fonte com treinamento do formato da letra coca cola
		if (!(new File(CAMINHO_DADOS + "tessdata/" + lang2 + ".traineddata")).exists()) {
            try {

                AssetManager assetManager = getAssets();
                InputStream in = assetManager.open("tessdata/cocacola.traineddata");
                //GZIPInputStream gin = new GZIPInputStream(in);
                OutputStream out = new FileOutputStream(CAMINHO_DADOS
                        + "tessdata/cocacola.traineddata");

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                //while ((lenf = gin.read(buff)) > 0) {
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                //gin.close();
                out.close();

                Log.v(TAG, "Copied " + lang2 + " traineddata");
            } catch (IOException e) {
                Log.e(TAG, "Was unable to copy " + lang2 + " traineddata " + e.toString());
            }
        }
		
		
		
		
		
        // You can get them at:
        // http://code.google.com/p/tesseract-ocr/downloads/list
        // This area needs work and optimization
		 if (!(new File(CAMINHO_DADOS + "tessdata/eng.user-words")).exists()) {
            try {

                AssetManager assetManager = getAssets();
                InputStream in = assetManager.open("tessdata/eng.user-words");
                //GZIPInputStream gin = new GZIPInputStream(in);
                OutputStream out = new FileOutputStream(CAMINHO_DADOS
                        + "tessdata/eng.user-words");

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                //while ((lenf = gin.read(buff)) > 0) {
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                //gin.close();
                out.close();

                Log.v(TAG, "Copied " + lang + " user-words");
            } catch (IOException e) {
                Log.e(TAG, "Was unable to copy " + lang + " user-words " + e.toString());
            }
        }
		
		
        if (!(new File(CAMINHO_DADOS + "tessdata/" + lang + ".traineddata")).exists()) {
            try {

                AssetManager assetManager = getAssets();
                InputStream in = assetManager.open("tessdata/eng.traineddata");
                //GZIPInputStream gin = new GZIPInputStream(in);
                OutputStream out = new FileOutputStream(CAMINHO_DADOS
                        + "tessdata/eng.traineddata");

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                //while ((lenf = gin.read(buff)) > 0) {
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                //gin.close();
                out.close();

                Log.v(TAG, "Copied " + lang + " traineddata");
            } catch (IOException e) {
                Log.e(TAG, "Was unable to copy " + lang + " traineddata " + e.toString());
            }
        }


        if (!(new File(CAMINHO_DADOS + "tessdata/eng.cube.bigrams")).exists()) {
            try {

                AssetManager assetManager = getAssets();
                InputStream in = assetManager.open("tessdata/eng.cube.bigrams");
                //GZIPInputStream gin = new GZIPInputStream(in);
                OutputStream out = new FileOutputStream(CAMINHO_DADOS
                        + "tessdata/eng.cube.bigrams");

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                //while ((lenf = gin.read(buff)) > 0) {
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                //gin.close();
                out.close();

                Log.v(TAG, "Copied eng.cube.bigrams");
            } catch (IOException e) {
                Log.e(TAG, "Was unable to copy eng.cube.bigrams " + e.toString());
            }
        }


        if (!(new File(CAMINHO_DADOS + "tessdata/eng.cube.fold")).exists()) {
            try {

                AssetManager assetManager = getAssets();
                InputStream in = assetManager.open("tessdata/eng.cube.fold");
                //GZIPInputStream gin = new GZIPInputStream(in);
                OutputStream out = new FileOutputStream(CAMINHO_DADOS
                        + "tessdata/eng.cube.fold");

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                //while ((lenf = gin.read(buff)) > 0) {
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                //gin.close();
                out.close();

                Log.v(TAG, "Copied eng.cube.fold");
            } catch (IOException e) {
                Log.e(TAG, "Was unable to copy eng.cube.fold " + e.toString());
            }
        }


        if (!(new File(CAMINHO_DADOS + "tessdata/eng.cube.lm")).exists()) {
            try {

                AssetManager assetManager = getAssets();
                InputStream in = assetManager.open("tessdata/eng.cube.lm");
                //GZIPInputStream gin = new GZIPInputStream(in);
                OutputStream out = new FileOutputStream(CAMINHO_DADOS
                        + "tessdata/eng.cube.lm");

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                //while ((lenf = gin.read(buff)) > 0) {
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                //gin.close();
                out.close();

                Log.v(TAG, "Copied eng.cube.lm");
            } catch (IOException e) {
                Log.e(TAG, "Was unable to copy eng.cube.lm " + e.toString());
            }
        }

        if (!(new File(CAMINHO_DADOS + "tessdata/eng.cube.nn")).exists()) {
            try {

                AssetManager assetManager = getAssets();
                InputStream in = assetManager.open("tessdata/eng.cube.nn");
                //GZIPInputStream gin = new GZIPInputStream(in);
                OutputStream out = new FileOutputStream(CAMINHO_DADOS
                        + "tessdata/eng.cube.nn");

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                //while ((lenf = gin.read(buff)) > 0) {
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                //gin.close();
                out.close();

                Log.v(TAG, "Copied eng.cube.nn");
            } catch (IOException e) {
                Log.e(TAG, "Was unable to copy eng.cube.nn " + e.toString());
            }
        }

        if (!(new File(CAMINHO_DADOS + "tessdata/eng.cube.params")).exists()) {
            try {

                AssetManager assetManager = getAssets();
                InputStream in = assetManager.open("tessdata/eng.cube.params");
                //GZIPInputStream gin = new GZIPInputStream(in);
                OutputStream out = new FileOutputStream(CAMINHO_DADOS
                        + "tessdata/eng.cube.params");

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                //while ((lenf = gin.read(buff)) > 0) {
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                //gin.close();
                out.close();

                Log.v(TAG, "Copied eng.cube.params");
            } catch (IOException e) {
                Log.e(TAG, "Was unable to copy eng.cube.params " + e.toString());
            }
        }

        if (!(new File(CAMINHO_DADOS + "tessdata/eng.cube.size")).exists()) {
            try {

                AssetManager assetManager = getAssets();
                InputStream in = assetManager.open("tessdata/eng.cube.size");
                //GZIPInputStream gin = new GZIPInputStream(in);
                OutputStream out = new FileOutputStream(CAMINHO_DADOS
                        + "tessdata/eng.cube.size");

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                //while ((lenf = gin.read(buff)) > 0) {
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                //gin.close();
                out.close();

                Log.v(TAG, "Copied eng.cube.size");
            } catch (IOException e) {
                Log.e(TAG, "Was unable to copy eng.cube.size " + e.toString());
            }
        }


        if (!(new File(CAMINHO_DADOS + "tessdata/eng.cube.word-freq")).exists()) {
            try {

                AssetManager assetManager = getAssets();
                InputStream in = assetManager.open("tessdata/eng.cube.word-freq");
                //GZIPInputStream gin = new GZIPInputStream(in);
                OutputStream out = new FileOutputStream(CAMINHO_DADOS
                        + "tessdata/eng.cube.word-freq");

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                //while ((lenf = gin.read(buff)) > 0) {
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                //gin.close();
                out.close();

                Log.v(TAG, "Copied eng.cube.word-freq");
            } catch (IOException e) {
                Log.e(TAG, "Was unable to copy eng.cube.word-freq " + e.toString());
            }
        }

        if (!(new File(CAMINHO_DADOS + "tessdata/eng.traineddata")).exists()) {
            try {

                AssetManager assetManager = getAssets();
                InputStream in = assetManager.open("tessdata/eng.traineddata");
                //GZIPInputStream gin = new GZIPInputStream(in);
                OutputStream out = new FileOutputStream(CAMINHO_DADOS
                        + "tessdata/eng.traineddata");

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                //while ((lenf = gin.read(buff)) > 0) {
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                //gin.close();
                out.close();

                Log.v(TAG, "Copied eng.traineddata");
            } catch (IOException e) {
                Log.e(TAG, "Was unable to copy eng.traineddata " + e.toString());
            }
        


			
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		_campo = (EditText) findViewById(R.id.field);
		_botao = (Button) findViewById(R.id.button);
		_botao.setOnClickListener(new ButtonClickHandler());
		_caminho = CAMINHO_DADOS + "/ocr.jpg";
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
	}

	public void onResume() {
		super.onResume();
		if (!OpenCVLoader.initDebug()) {
			Log.d("OpenCV",
					"Internal OpenCV library not found. Using OpenCV Manager for initialization");
			OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this,
					mLoaderCallback);
		} else {
			Log.d("OpenCV", "OpenCV library found inside package. Using it!");
			mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
		}
	}

	public class ButtonClickHandler implements View.OnClickListener {
		
		public void onClick(View view) {
			Log.v(TAG, "Iniciando camera");

			// get selected radio button from radioGroup
			int selectedId = radioGroup.getCheckedRadioButtonId();

			// find the radiobutton by returned id
			radioButton = (RadioButton) findViewById(selectedId);
			startCameraActivity();

		}
	}

	protected void startCameraActivity() {
		File file = new File(_caminho);
		Uri outputFileUri = Uri.fromFile(file);
		final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG, "resultCode: " + resultCode);
		if (resultCode == -1) {
			onPhotoTaken();
		} else {
			Log.v(TAG, "cancelado pelo usuário");
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(OCRActivity.FOTO_TIRADA, _captura);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.i(TAG, "onRestoreInstanceState()");
		if (savedInstanceState.getBoolean(OCRActivity.FOTO_TIRADA)) {
			onPhotoTaken();
		}
	}

	protected void onPhotoTaken() {
		_captura = true;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;
		Bitmap bitmap = BitmapFactory.decodeFile(_caminho, options);
		try {
			ExifInterface exif = new ExifInterface(_caminho);
			int exifOrientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			Log.v(TAG, "Orient: " + exifOrientation);
			int rotacao = RotationEnum.ZERO.value;
			rotacao = orientacao(exifOrientation, rotacao);
			Log.v(TAG, "Rotation: " + rotacao);
			bitmap = rotacao(bitmap, rotacao);
			bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
		} catch (IOException e) {
			Log.e(TAG, "Couldn't correct orientation: " + e.toString());
		}
		Log.v(TAG, "Before baseApi");
		String recognizedText = ocr(bitmap);
		
		
		Log.v(TAG, "OCRED TEXT: " + recognizedText);
		if (linguagem.equalsIgnoreCase("cocacola")) {
			recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9]+", " ");
			
			Locale ptBr = new Locale("pt", "BR");
			tts.setLanguage(ptBr);
			tts.speak(recognizedText, TextToSpeech.QUEUE_FLUSH, null);
		}
		recognizedText = recognizedText.trim();
		if (recognizedText.length() != 0) {
			_campo.setText(_campo.getText().toString().length() == 0 ? recognizedText
					: _campo.getText() + " " + recognizedText);
			_campo.setSelection(_campo.getText().toString().length());
		}
	}
	
	private boolean checkWriteExternalPermission()
	{

	    String permission = "android.permission.WRITE_EXTERNAL_STORAGE";
	    int res = this.checkCallingOrSelfPermission(permission);
	    return (res == PackageManager.PERMISSION_GRANTED);            
	}

	private String ocr(Bitmap bitmap) {
		checkWriteExternalPermission();
		
		
		
		image = Imgcodecs.imread(_caminho);
		SaveImageUtil.save(bitmap, this);
//		bitmap = ImageTreatmentUtil.resize(bitmap, 1200, 2400);
//		bitmap.setDensity(300);
		//bitmap.setDensity(500);
		//bitmap = ImageTreatmentUtil.resize(bitmap, 1517, 2700);
		 bitmap = ImageTreatmentUtil.imageConfig(bitmap,image, this, radioButton.getText().toString());
		SaveImageUtil.save(bitmap, this);
		TessBaseAPI baseApi = TesseractParametersUtil.create(CAMINHO_DADOS,
				linguagem, bitmap,this);
		

//		baseApi = TesseractParametersUtil.setParameters(
//				VariablesEnum.POR_LOAD_UNAMBIG_DAWG.value, ValueEnum.UM.value,
//				baseApi);
//		baseApi = TesseractParametersUtil.setParameters(
//				VariablesEnum.POR_LOAD_FREQ_DAWG.value, ValueEnum.UM.value,
//				baseApi);
//		baseApi = TesseractParametersUtil.setParameters(
//				VariablesEnum.POR_LOAD_WORD_DAWG.value, ValueEnum.UM.value,
//				baseApi);
//		baseApi = TesseractParametersUtil.setParameters(
//				VariablesEnum.POR_USER_WORD_SUFFIX.value,
//				ValueEnum.POR_WORD_USER.value, baseApi);
		String recognizedText= LettersUtil.unaccented(baseApi.getUTF8Text());
		baseApi.end();
		return recognizedText;
		 //bitmap = ImageTreatmentUtil.resize(bitmap, 800, 600);

	}

	private Bitmap rotacao(Bitmap bitmap, int rotacao) {
		if (rotacao != 0) {
			int w = bitmap.getWidth();
			int h = bitmap.getHeight();
			Matrix mtx = new Matrix();
			mtx.preRotate(rotacao);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
		}
		return bitmap;
	}

	private int orientacao(int exifOrientation, int rotate) {
		switch (exifOrientation) {
		case ExifInterface.ORIENTATION_ROTATE_90:
			rotate = RotationEnum.NOVENTA.value;
			break;
		case ExifInterface.ORIENTATION_ROTATE_180:
			rotate = RotationEnum.CENTOEOITENTA.value;
			break;
		case ExifInterface.ORIENTATION_ROTATE_270:
			rotate = RotationEnum.DUZENTOSESETENTA.value;
			break;
		}
		return rotate;
	}

}
