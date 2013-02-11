package Utils;

import il.co.idanmoshe.digitalwallet.ItemsDataBase;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

public class idanUtils extends Activity{

	Integer isHighPrice, isHighPriceID;
	Integer isHighIncome, isHighIncomeID;
	
	public static void writeData(String data,String strFilePath)
	{

		PrintWriter csvWriter;
		try
		{

			File file = new File(strFilePath);
			if(!file.exists()){
				file = new File(strFilePath);
			}
			csvWriter = new  PrintWriter(new FileWriter(file,true));


			csvWriter.print(data+","+"hello");
			csvWriter.print("\r\n");
			csvWriter.print("world");


			csvWriter.close();


		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public String recordAudio(String fileName, Activity activity) { // Record Audio
		final MediaRecorder recorder = new MediaRecorder();

		String voiceRecordPath = "null";

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

		final ProgressDialog mProgressDialog = new ProgressDialog(activity);
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

		return voiceRecordPath;
	}

	public void createMyWalletFolderOnlyOneTime(){

		String fullDirPath = Environment.getExternalStorageDirectory().getAbsolutePath();
		fullDirPath += "/My Wallet";
		File myWalletDir=new File(fullDirPath);
		myWalletDir.mkdirs();
	}

	public static String getRealPathFromURI(Uri contentUri, Activity activity) { //For Android 2.3.X SDK (Lowest versions)
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor =activity.managedQuery(contentUri, proj, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}


	public String getDateAndTimeStamp(){
		String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
		return timeStamp;
	}

	public String getDateStamp(){
		String dateStamp = new SimpleDateFormat("ddMMyyyy").format(new Date());
		return dateStamp;
	}

	public String getTimeStamp(){
		String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
		return timeStamp;
	}

	public String getFixedTimeStamp(){
		String hh=new SimpleDateFormat("HH").format(new Date());
		String mm=new SimpleDateFormat("mm").format(new Date());
		String ss=new SimpleDateFormat("ss").format(new Date());
		String timeStamp=hh+":"+mm+":"+ss;
		return timeStamp;
	}

	public String getFixedDateStamp(){
		String dd=new SimpleDateFormat("dd").format(new Date());
		String mm=new SimpleDateFormat("MM").format(new Date());
		String yyyy=new SimpleDateFormat("yyyy").format(new Date());
		String dateStamp=dd+"/"+mm+"/"+yyyy;
		return dateStamp;
	}

	public void setHighPrice(Integer strHightPrice){
		this.isHighPrice=strHightPrice;
	}

	public Integer getHighPrice(Integer strHighPrice){
		return this.isHighPrice;
	}

	public void setHighPriceID(Integer strID){
		this.isHighPriceID=strID;
	}

	public Integer getHighPriceID(Integer strID){
		return this.isHighPriceID;
	}

	public Integer getIsHighIncome() {
		return isHighIncome;
	}

	public void setIsHighIncome(Integer isHighIncome) {
		this.isHighIncome = isHighIncome;
	}

	public Integer getIsHighIncomeID() {
		return isHighIncomeID;
	}

	public void setIsHighIncomeID(Integer isHighIncomeID) {
		this.isHighIncomeID = isHighIncomeID;
	}

}

