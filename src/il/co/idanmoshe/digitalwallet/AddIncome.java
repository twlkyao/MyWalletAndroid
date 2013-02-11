package il.co.idanmoshe.digitalwallet;

import java.io.File;
import java.io.Serializable;

import Utils.idanUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class AddIncome extends Activity {

	public static final int  TAKE_A_PHOTO = 100;
	public static final int LOAD_AN_IMAGE = 200;


	public String isAudioRecordPath;
	public String incomePhotoURL;
	public Uri outputFileUri;
	
	ImageButton btnAddIncome;
	ToggleButton chkLocation;
	EditText txtMoneyAmount, txtPayeeName, txtDescription;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_income);

		ImageButton btnTakePhoto=(ImageButton)findViewById(R.id.imageButton1);
		ImageButton btnRecordAudioNote=(ImageButton)findViewById(R.id.imageButtonRecordAudio);
		btnAddIncome=(ImageButton)findViewById(R.id.imageButtonOkIncome);
		final Spinner spnIncomeType=(Spinner)findViewById(R.id.spinner1);
		txtMoneyAmount=(EditText)findViewById(R.id.editText1);
		txtPayeeName=(EditText)findViewById(R.id.editText2);
		txtDescription=(EditText)findViewById(R.id.editTextIncomeDescription);
		chkLocation = (ToggleButton) findViewById(R.id.toggleButton1);
		TextView tvDate=(TextView)findViewById(R.id.textView3);
		TextView tvTime=(TextView)findViewById(R.id.textView5);

		final idanUtils idanUtils=new idanUtils();

		tvDate.setText(idanUtils.getFixedDateStamp());
		tvTime.setText(idanUtils.getFixedTimeStamp());

		btnAddIncome.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) { // OK Click
				
				String incomeType=spnIncomeType.getSelectedItem().toString(); // Get data
				String payeeName=txtPayeeName.getText().toString();
				int moneyAmount=Integer.parseInt(txtMoneyAmount.getText().toString());
				String description=txtDescription.getText().toString();
				String isLocation;
				if (chkLocation.isChecked()) { isLocation="default location - soon"; }
				else { isLocation="None"; }
				String isDate=idanUtils.getFixedDateStamp();
				String isTime=idanUtils.getFixedTimeStamp();

				Income income=new Income(description, moneyAmount, incomeType, payeeName,
						isAudioRecordPath, isDate, isTime, isLocation, incomePhotoURL); // Put data to database

				MainActivity.itemsDataBase.insertIncome(income);
				MainActivity.itemsDataBase.close();
				
				AlertDialog.Builder builder=new AlertDialog.Builder(AddIncome.this); //Notify user all is OK
				AlertDialog completeOK=builder.setTitle("Information").setMessage("Income successfuly added")
						.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								Log.d(ItemsDataBase.TAG, "Income added successfuly to database");
								Intent intent=new Intent(AddIncome.this, MainActivity.class);
								startActivity(intent);
								finish();
							}
						}).create();
				completeOK.show();
			}
		});

		btnRecordAudioNote.setOnClickListener(new View.OnClickListener() { // Record Audio Note

			@Override
			public void onClick(View v) {
				String fileName=idanUtils.getDateAndTimeStamp();
				isAudioRecordPath=idanUtils.recordAudio(fileName, AddIncome.this);
			}
		});

		btnTakePhoto.setOnClickListener(new View.OnClickListener() { // Take a Photo

			@Override
			public void onClick(View v) {
				
				final CharSequence[] captureOption = {"Take a Photo", "Load an Image"};
				AlertDialog.Builder pickBuilder = new AlertDialog.Builder(AddIncome.this); // Context Menu (LOAD OR CAPTURE)
				pickBuilder.setTitle(R.string.please_select);
				pickBuilder.setItems(captureOption, new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int choose) {
				        if (choose == 0) {		// TAKE A PHOTO
				        	Log.d(ItemsDataBase.TAG, "TAKE A PHOTO FROM CAMERA");
							
							String ImageDirPath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/My Wallet";
							String ImageFileName=idanUtils.getDateAndTimeStamp()+".jpg";
							File file = new File(ImageDirPath, ImageFileName);
							incomePhotoURL=ImageDirPath+"/"+ImageFileName;
							Log.i(ItemsDataBase.TAG, "Image saved. realPathName="+incomePhotoURL);
							outputFileUri = Uri.fromFile(file);
							Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
							startActivityForResult(intent, TAKE_A_PHOTO);

						} else {		// LOAD AN IMAGE
							Log.d(ItemsDataBase.TAG, "LOAD AN PHOTO FROM GALLERY");
							
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_income_layout, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) { // Handling the photo

		ImageView imageView=(ImageView)findViewById(R.id.imageView1);
		if (requestCode == TAKE_A_PHOTO) {
			// Check if the result includes a thumbnail Bitmap
			if (data != null) {
				if (data.hasExtra("data")) {
					Bitmap thumbnail=data.getParcelableExtra("data");
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
				Toast.makeText(this, "Image saved to "+ incomePhotoURL, Toast.LENGTH_LONG).show();
				changeImageButtonToImage(incomePhotoURL); // TEST
			}
		}
		else if (requestCode == LOAD_AN_IMAGE) {
			Uri selectedImageUri=data.getData();
			incomePhotoURL=idanUtils.getRealPathFromURI(selectedImageUri, AddIncome.this);
			Toast.makeText(this, "Image loaded from "+incomePhotoURL, Toast.LENGTH_LONG).show();
			Log.i(ItemsDataBase.TAG, "Image loaded from "+incomePhotoURL);
		}
	}
	public void changeImageButtonToImage(String isPhotoURL){
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				ImageButton btnTakePhoto=(ImageButton)findViewById(R.id.imageButton1);
				
			}
		});
	}
}
class Income implements Serializable{
	String income_date;
	String income_time;
	String income_location;
	String income_option;
	String income_description;
	int    money_amount;
	String payee_name;
	String income_voice_record;
	String photo_url;

	public Income(String income_description, int moneyAmount,
			String income_option, String payee_name, String income_voice_record,
			String income_date, String income_time, String income_location, String photo_url) {
		super();
		this.income_description = income_description;
		this.money_amount = moneyAmount;
		this.income_date = income_date;
		this.income_time = income_time;
		this.income_location = income_location;
		this.income_option = income_option;
		this.payee_name = payee_name;
		this.income_voice_record = income_voice_record;
		this.photo_url = photo_url;
	}

	@Override
	public String toString() {
		return "Income: " + income_description + ", Amount: " + money_amount + " $";
	}

}