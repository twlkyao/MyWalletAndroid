package il.co.idanmoshe.digitalwallet;

import java.io.File;

import Utils.idanUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class AddExpense extends Activity {

	public static final int  TAKE_A_PHOTO = 100;
	public static final int LOAD_AN_IMAGE = 200;
	public static String realPathName;
	public static idanUtils idanUtils=new idanUtils();
	public static String voiceRecordPath = "null";
	String TAG = ItemsDataBase.TAG;

	public static String isLocation;
	String isDate;
	String isTime;
	String strDescription;
	String isAudioNote, productCategory, paymentOption;
	Integer strPrice;

	EditText etDescreption, etPrice;
	Spinner spnPaymentOption, spnProductCategory;
	Button btnTakePhoto, btnLoadImageFromGallery;
	ImageButton btnPhotoHandle, btnRecordAudioNote, btnOK;
	ImageButton tbLocation;

	LocationManager locMan;
	double latitude, longitude;
	public Uri outputFileUri;

	public static void initialize(){}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//		setContentView(R.layout.add_expense);

		isDate=idanUtils.getFixedDateStamp();
		isTime=idanUtils.getFixedTimeStamp();
		etDescreption=(EditText)findViewById(R.id.editText1);
		etPrice=(EditText)findViewById(R.id.editText2);
		spnPaymentOption=(Spinner)findViewById(R.id.spinner1);
		spnProductCategory=(Spinner)findViewById(R.id.spinner2);
		btnOK=(ImageButton)findViewById(R.id.imageButtonOkExpense);
		btnTakePhoto=(Button)findViewById(R.id.button1);
		btnRecordAudioNote=(ImageButton)findViewById(R.id.imageButtonAudioNote);
		btnPhotoHandle = (ImageButton) findViewById(R.id.imageButtonPhoto);
		btnLoadImageFromGallery=(Button)findViewById(R.id.button3);
		tbLocation = (ImageButton) findViewById(R.id.imageButton1);

		etPrice.setText("0");
		//		strPrice = 0;

		tbLocation.setOnClickListener(new View.OnClickListener() { // LOCATION

			@Override
			public void onClick(View v) {
				Intent getLocationIntent = new Intent(AddExpense.this, LocationFinder.class);
				startActivity(getLocationIntent);
			}
		});

		btnPhotoHandle.setOnClickListener(new View.OnClickListener() { // Take a Photo || Load an Image

			@Override
			public void onClick(View v) {

				String take_a_photo= getString(R.string.take_a_photo);
				String loan_an_image = getString(R.string.load_an_image);

				final CharSequence[] captureOption = {take_a_photo, loan_an_image};
				AlertDialog.Builder pickBuilder = new AlertDialog.Builder(AddExpense.this); // Context Menu (LOAD OR CAPTURE)
				pickBuilder.setTitle(R.string.please_select);
				pickBuilder.setItems(captureOption, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int choose) {
						if (choose == 0) {		// TAKE A PHOTO			

							String ImageDirPath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/My Wallet";
							String ImageFileName=idanUtils.getDateAndTimeStamp()+".jpg";
							File file = new File(ImageDirPath, ImageFileName);
							realPathName=ImageDirPath+"/"+ImageFileName;
							Log.i(ItemsDataBase.TAG, "Image saved. realPathName="+realPathName);
							outputFileUri = Uri.fromFile(file);
							Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
							startActivityForResult(intent, TAKE_A_PHOTO);
						} else {		// LOAD AN IMAGE

							Intent intentLoadPhoto=new Intent();
							intentLoadPhoto.setType("image/*");
							intentLoadPhoto.setAction(Intent.ACTION_GET_CONTENT);
							startActivityForResult(Intent.createChooser(intentLoadPhoto,"Select Picture"), LOAD_AN_IMAGE);
						}
					}
				});
				AlertDialog alert = pickBuilder.create();
				alert.show();
			}
		});

		btnOK.setOnClickListener(new View.OnClickListener() { // OK Click

			@Override
			public void onClick(View v) {

				strDescription=etDescreption.getText().toString();
				paymentOption=spnPaymentOption.getSelectedItem().toString();
				productCategory=spnProductCategory.getSelectedItem().toString();
				strPrice=Integer.parseInt(etPrice.getText().toString());

				Log.d(TAG, "strPrice = " + strPrice);
				if (strPrice == 0) {
					strPrice = 0;
				}
				if (strDescription.isEmpty()) {
					strDescription = "Default description";
				}

				//				if (tbLocation.isChecked()) {
				//					isLocation = latitude + "#" + longitude;
				//				}
				//				else {
				//					isLocation="None";
				//				}

				isAudioNote = voiceRecordPath;

				Expense expense=new Expense(
						strDescription, strPrice, paymentOption, productCategory, realPathName,
						isLocation, isDate, isTime, isAudioNote);


				MainActivity.itemsDataBase.insert(expense); //Put information to SQLite database
				MainActivity.itemsDataBase.close(); //Closing connection to SQLite database

				AlertDialog.Builder builder=new AlertDialog.Builder(AddExpense.this); //Notify user all is OK
				AlertDialog completeOK=builder.setTitle("Information").setIcon(R.drawable.success).setMessage("Item successfuly added")
						.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								finish();
							}
						}).create();
				completeOK.show();
			}
		});

		btnRecordAudioNote.setOnClickListener(new View.OnClickListener() { // RECORD AUDIO

			@Override
			public void onClick(View v) {
				recordAudio(idanUtils.getDateAndTimeStamp());
				Toast.makeText(AddExpense.this, "Audio note saved to " + voiceRecordPath, Toast.LENGTH_LONG).show();
			}
		});
	}

	@SuppressWarnings("deprecation")
	public void recordAudio(String fileName) { // Record audio
		final MediaRecorder recorder = new MediaRecorder();

		String voiceRecordURL = Environment.getExternalStorageDirectory().getAbsolutePath();
		voiceRecordURL += "/My Wallet/";
		voiceRecordURL += fileName;
		voiceRecordURL += ".3gp";

		ContentValues values = new ContentValues(3);
		values.put(MediaStore.MediaColumns.TITLE, fileName);
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		recorder.setOutputFile(voiceRecordURL);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

		Log.i(ItemsDataBase.TAG, "File saved to " + voiceRecordURL);
		voiceRecordPath = voiceRecordURL;

		try {
			recorder.prepare();
		} catch (Exception e){
			Log.e(ItemsDataBase.TAG, e.getMessage());
		}

		final ProgressDialog mProgressDialog = new ProgressDialog(AddExpense.this);
		mProgressDialog.setTitle("Recording...");
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setButton("Stop recording", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				mProgressDialog.dismiss();
				recorder.stop();
				recorder.release();
			}
		});

		mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener(){
			public void onCancel(DialogInterface p1) {
				recorder.stop();
				recorder.release();
			}
		});
		recorder.start();
		mProgressDialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) { // Handling the photo

		ImageView imageView=(ImageView)findViewById(R.id.imageView1);
		if (requestCode == TAKE_A_PHOTO) {
			// Check if the result includes a thumbnail Bitmap
			if (data != null) {
				if (data.hasExtra("data")) {
					Bitmap thumbnail=data.getParcelableExtra("data");
					//					bitmap2 = data.getParcelableExtra("data");
					imageView.setImageBitmap(thumbnail);
				}
			} else {
				// If there is no thumbnail image data, the image
				// will have been stored in the target output URI.
				// Resize the full image to fit in out image view.
				int width = imageView.getWidth();
				int height = imageView.getHeight();
				BitmapFactory.Options factoryOptions = new BitmapFactory.Options();
				factoryOptions.inJustDecodeBounds = true; BitmapFactory.decodeFile(outputFileUri.getPath(),
						factoryOptions);
				int imageWidth = factoryOptions.outWidth; int imageHeight = factoryOptions.outHeight;
				// Determine how much to scale down the image
				int scaleFactor = Math.min(imageWidth/width, imageHeight/height);
				// Decode the image file into a Bitmap sized to fill the View
				factoryOptions.inJustDecodeBounds = false; factoryOptions.inSampleSize = scaleFactor; factoryOptions.inPurgeable = true;
				Bitmap bitmap = BitmapFactory.decodeFile(outputFileUri.getPath(),
						factoryOptions);
				imageView.setImageBitmap(bitmap);
				Toast.makeText(this, "Image saved to "+realPathName, Toast.LENGTH_LONG).show();
			}
		}
		else if (requestCode == LOAD_AN_IMAGE) {
			Uri selectedImageUri=data.getData();
			realPathName=idanUtils.getRealPathFromURI(selectedImageUri, AddExpense.this);
			Toast.makeText(this, "Image loaded from "+realPathName, Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onPause() {
		MainActivity.itemsDataBase.close();
		super.onPause();
	}

	@Override
	protected void onResume() {
		MainActivity.itemsDataBase.open();
		super.onResume();
	}
}