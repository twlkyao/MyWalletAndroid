package il.co.idanmoshe.digitalwallet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import Utils.idanUtils;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowIncome extends Activity {
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_income_layout);

		TextView tvDescription=(TextView)findViewById(R.id.textView2);
		TextView tvMoneyAmount=(TextView)findViewById(R.id.textView4);
		TextView tvIncomeType=(TextView)findViewById(R.id.textView6);
		TextView tvPayeeName=(TextView)findViewById(R.id.textView8);
		TextView tvLocation=(TextView)findViewById(R.id.textView10);
		TextView tvDate=(TextView)findViewById(R.id.textView12);
		TextView tvTime=(TextView)findViewById(R.id.textView14);
		ImageView ivIncomePhoto=(ImageView)findViewById(R.id.imageView1);
		Button btnDeleteItem=(Button)findViewById(R.id.button1);
		Button btnExportToPDF=(Button)findViewById(R.id.button3);
		ImageButton btnPlayRecordedNote=(ImageButton)findViewById(R.id.imageButton1);
		TextView tvIsAudioNote=(TextView)findViewById(R.id.textView15);

		Bundle bundle=getIntent().getExtras();
		final Income income=(Income)bundle.getSerializable("incomeName");

		if (income.income_voice_record.equals("null") || income.income_voice_record.isEmpty()) {
			tvIsAudioNote.setText("No recorded audio note.");
		}
		else {
			tvIsAudioNote.setText("Recorded audio note saved to " + income.income_voice_record);
		}

		tvDescription.setText(income.income_description);
		tvMoneyAmount.setText(income.money_amount+"");
		tvIncomeType.setText(income.income_option);
		tvPayeeName.setText(income.payee_name);
		tvLocation.setText(income.income_location);
		tvDate.setText(income.income_date);
		tvTime.setText(income.income_time);

		final String PHOTO_URL=income.photo_url+"";
		Log.i(ItemsDataBase.TAG, PHOTO_URL+"");

		if (PHOTO_URL.isEmpty() || PHOTO_URL.equals("null")) {
			Log.d(ItemsDataBase.TAG, "There is no photo. Default photo will be presented.");
			int defaultImageID=R.drawable.default_item_photo;
			ivIncomePhoto.setImageResource(defaultImageID);
		}
		else {

			File file=new File(PHOTO_URL);
			Log.i(ItemsDataBase.TAG, file.toString());
			if (file.exists()) {
				Log.d(ItemsDataBase.TAG, "File photo url exists");
				Bitmap bmp=BitmapFactory.decodeFile(file.getAbsolutePath());
				Log.i(ItemsDataBase.TAG, "File absolute path="+file.getAbsolutePath());
				ivIncomePhoto.setImageBitmap(bmp);
			}
			else {
				Log.d(ItemsDataBase.TAG, "File photo url does not exists");
			}
		}

		btnExportToPDF.setOnClickListener(new View.OnClickListener() { // Export To PDF

			@Override
			public void onClick(View v) {

				idanUtils idanUtils=new idanUtils();
				final String filename="/"+"INCOME_"+idanUtils.getDateAndTimeStamp()+".pdf";
				String deviceStorage=Environment.getExternalStorageDirectory().getAbsolutePath().toString();
				Log.d(ItemsDataBase.TAG, deviceStorage+filename);

				File myDir = new File(deviceStorage + "/My Wallet");
				myDir.mkdirs();
				File file=new File(myDir, filename);

				com.itextpdf.text.Document document=new com.itextpdf.text.Document();

				try {
					PdfWriter.getInstance(document, new FileOutputStream(file));
					document.open();
					document.add(new Paragraph("Income description: "+income.income_description));
					document.add(new Paragraph("Income Type: "+income.income_option));
					document.add(new Paragraph("Payee Name: "+income.payee_name));
					document.add(new Paragraph("Money Amount: "+income.money_amount));
					document.add(new Paragraph("Added at: "+income.income_location));
					document.add(new Paragraph("Dated: "+income.income_date));
					document.add(new Paragraph("Time: "+income.income_time));

					if (PHOTO_URL.isEmpty() || PHOTO_URL.equals("null") || PHOTO_URL.equals("")) {
						document.add(new Paragraph("No image available"));
						Log.i(ItemsDataBase.TAG, "No image available");
					} else {
						Image image=Image.getInstance(income.photo_url);
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

						AlertDialog.Builder builder=new AlertDialog.Builder(ShowIncome.this); //Notify user all is OK
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

		btnPlayRecordedNote.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				MediaPlayer mediaPlayer=new MediaPlayer();
				try {
					mediaPlayer.setDataSource(income.income_voice_record);
					mediaPlayer.prepare();
					mediaPlayer.start();
				} catch (IllegalArgumentException e) {
					Log.e(ItemsDataBase.TAG, e.getMessage());
				} catch (IllegalStateException e) {
					Log.e(ItemsDataBase.TAG, e.getMessage());
				} catch (IOException e) {
					Log.e(ItemsDataBase.TAG, e.getMessage());
				}
				// TODO - On Key Press Back - STOP PLAY THE AUDIO NOTE
				//				mediaPlayer.stop();
				//				mediaPlayer.release();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_income_layout, menu);
		return true;
	}

}
