package il.co.idanmoshe.digitalwallet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import Utils.idanUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class ShowExpense extends Activity {
	
	private boolean isPlay;
	private MediaPlayer mediaPlayer;

	private TextView tvDescription, tvPrice, tvPaymentOption, tvCategory, tvLocation, tvDate, tvTime, tvIsAudioNote;
	private ImageButton btnPlayRecordedNote, btnShareToTwitter, btnDeleteExpense, btnEdit;
	private Button btnExportToPDF;
	private ImageView ivItemPhoto;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_item_activity);

		isPlay = true; // Reset so we can play the audio

		tvDescription = (TextView)findViewById(R.id.textView2);
		tvPrice = (TextView)findViewById(R.id.textView4);
		tvPaymentOption = (TextView)findViewById(R.id.textView6);
		tvCategory=(TextView)findViewById(R.id.textView8);
		tvLocation=(TextView)findViewById(R.id.textView10);
		tvDate=(TextView)findViewById(R.id.textView12);
		tvTime=(TextView)findViewById(R.id.textView14);
		ivItemPhoto=(ImageView)findViewById(R.id.imageView1);
		btnDeleteExpense=(ImageButton)findViewById(R.id.imageButtonDeleteExpense);
		btnExportToPDF=(Button)findViewById(R.id.button3);
		btnPlayRecordedNote=(ImageButton)findViewById(R.id.imageButton1);
		tvIsAudioNote=(TextView)findViewById(R.id.textView15);
		btnShareToTwitter = (ImageButton) findViewById(R.id.imageButtonTwitter);
		btnEdit = (ImageButton) findViewById(R.id.imageButtonEditExpense);

		Bundle bundle=getIntent().getExtras();
		final Expense item=(Expense)bundle.getSerializable("itemName");

		Bundle positionExpenseBundle = getIntent().getExtras();
		final long expensePosition = positionExpenseBundle.getLong("positionExpense");
//		final long expensePosition = HistoryExpense.exp_ID;
		

		if (item.isAudioNote.equals("null") || item.isAudioNote.isEmpty()) {
			tvIsAudioNote.setText("No recorded audio note.");
		}
		else {
			tvIsAudioNote.setText("Recorded audio note saved to " + item.isAudioNote);
		}

		tvDescription.setText(item.strDescription);
		tvPrice.setText(item.strPrice+"");
		tvPaymentOption.setText(item.paymentOption);
		tvCategory.setText(item.productCategory);
		tvLocation.setText(item.isLocation);
		tvDate.setText(item.isDate);
		tvTime.setText(item.isTime);
		
		final String PHOTO_URL=item.photoURL+"";
		Log.i(ItemsDataBase.TAG, PHOTO_URL+"");

		if (PHOTO_URL.isEmpty() || PHOTO_URL.equals("null") || PHOTO_URL.equals(null)) {
			Log.d(ItemsDataBase.TAG, "There is no photo. Default photo will be presented.");
			int defaultImageID=R.drawable.default_item_photo;
			ivItemPhoto.setImageResource(defaultImageID);
		}
		else {

			File file=new File(PHOTO_URL);
			Log.i(ItemsDataBase.TAG, file.toString());
			if (file.exists()) {
				Log.d(ItemsDataBase.TAG, "File photo url exists");
				Bitmap bmp=BitmapFactory.decodeFile(file.getAbsolutePath());
				Log.i(ItemsDataBase.TAG, "File absolute path="+file.getAbsolutePath());
				ivItemPhoto.setImageBitmap(bmp);
			}
			else {
				Log.d(ItemsDataBase.TAG, "File photo url does not exists");
			}
		}

		btnDeleteExpense.setOnClickListener(new View.OnClickListener() { // Delete Expense
			
			@Override
			public void onClick(View v) {
				
				MainActivity.itemsDataBase.deleteExpense(expensePosition);
				runOnUiThread(new Runnable() {

					public void run() {

						AlertDialog.Builder builder=new AlertDialog.Builder(ShowExpense.this); //Notify user all is OK
						AlertDialog completeOK=builder.setTitle("Information").setMessage("Expense deleted with id: " + (expensePosition)).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								Log.d(ItemsDataBase.TAG, "Expense deleted with id: " + expensePosition);
								finish();
							}
						}).create();
						completeOK.show();
					}
				});
			}
		});

		btnExportToPDF.setOnClickListener(new View.OnClickListener() { // Export to pdf

			@Override
			public void onClick(View v) {

				idanUtils idanUtils=new idanUtils();
				final String filename="/"+"EXPENSE_"+idanUtils.getDateAndTimeStamp()+".pdf";
				String deviceStorage=Environment.getExternalStorageDirectory().getAbsolutePath().toString();
				Log.d(ItemsDataBase.TAG, deviceStorage+filename);

				File myDir = new File(deviceStorage + "/My Wallet");
				myDir.mkdirs();
				File file=new File(myDir, filename);

				Document document=new Document();

				try {
					PdfWriter.getInstance(document, new FileOutputStream(file));
					document.open();
					document.add(new Paragraph("Item description: "+item.strDescription));
					document.add(new Paragraph("Paid by: "+item.paymentOption));
					document.add(new Paragraph("Category: "+item.productCategory));
					document.add(new Paragraph("Price: "+item.strPrice));
					document.add(new Paragraph("Bught at: "+item.isLocation));
					document.add(new Paragraph("Dated: "+item.isDate));
					document.add(new Paragraph("Time: "+item.isTime));

					if (PHOTO_URL.isEmpty() || PHOTO_URL.equals("null")) {
						document.add(new Paragraph("No image available"));
						Log.i(ItemsDataBase.TAG, "No image available");
					} else {
						Image image=Image.getInstance(item.photoURL);
						image.scaleAbsolute(200f, 200f);
						document.add(image);
					}

				} catch (Exception e) {
					String error=(e.getMessage()==null)?"Error adding information to PDF document":e.getMessage();
					Log.e(ItemsDataBase.TAG, error);
				}
				document.close();

				runOnUiThread(new Runnable() {

					public void run() {

						AlertDialog.Builder builder=new AlertDialog.Builder(ShowExpense.this); //Notify user all is OK
						AlertDialog completeOK=builder.setTitle("Information").setMessage(filename+" saved to /My Wallet folder on your internal storage.").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								Log.d(ItemsDataBase.TAG, filename+" saved to /My Wallet folder on your internal storage.");
							}
						}).create();
						completeOK.show();
					}
				});
			}
		});

		btnPlayRecordedNote.setOnClickListener(new View.OnClickListener() { // Play recorded audio note

			@Override
			public void onClick(View v) {

				if (isPlay) { // Play
					mediaPlayer=new MediaPlayer();
					try {
						mediaPlayer.setDataSource(item.isAudioNote);
						mediaPlayer.prepare();
						mediaPlayer.start();
					} catch (IllegalArgumentException e) {
						Log.e(ItemsDataBase.TAG, e.getMessage());
					} catch (IllegalStateException e) {
						Log.e(ItemsDataBase.TAG, e.getMessage());
					} catch (IOException e) {
						Log.e(ItemsDataBase.TAG, e.getMessage());
					}
					isPlay = false;
				} else{ // Stop
					mediaPlayer.stop();
					mediaPlayer.release();
					isPlay = true;
				}
			}
		});

		btnShareToTwitter.setOnClickListener(new View.OnClickListener() { // Share to Twitter

			@Override
			public void onClick(View v) {

				String tweetUrl = "https://twitter.com/intent/tweet?text=";
				String text = "Hi, just twitting from My Wallet app! I just spent " + item.strPrice + " to buy " + item.strDescription;
				Uri uri = Uri.parse(tweetUrl + text);
				startActivity(new Intent(Intent.ACTION_VIEW, uri));
			}
		});
		
		btnEdit.setOnClickListener(new View.OnClickListener() { // Edit
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(ShowExpense.this, EditExpense.class);
				intent.putExtra("expenseName", item);
				startActivity(intent);
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_item_activity, menu);
		return true;
	}

}
