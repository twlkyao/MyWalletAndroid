package il.co.idanmoshe.digitalwallet;

import java.io.File;
import java.io.IOException;

import Utils.FolderZipper;
import Utils.ZipUtility;
import Utils.idanUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BackupAndRestore extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.backup_and_restore_layout);
		final TextView tvTest=(TextView)findViewById(R.id.textView1);
		final FolderZipper fZipper=new FolderZipper();
		idanUtils iUtils=new idanUtils();
		final ZipUtility zUtility=new ZipUtility();

		Button btnBackup=(Button)findViewById(R.id.button1);
		Button btnRestore=(Button)findViewById(R.id.button2);

		btnBackup.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				String path;
				final String dirPath;
				final String name;
				name="BackUp.zip";
				path=Environment.getDataDirectory()+"/data/"+getApplicationContext().getPackageName();
				dirPath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/My Wallet";
				final File sourceDirectory=new File(path);
				final File destZip=new File(dirPath, name);
				AlertDialog.Builder builder=new AlertDialog.Builder(BackupAndRestore.this); //Notify user all is OK
				AlertDialog completeOK=builder.setTitle("Information").setIcon(android.R.drawable.ic_menu_info_details).setMessage("Folder zipped to: "+dirPath+"/"+name)
						.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {

								try {
									zUtility.zipDirectory(sourceDirectory, destZip);
									Log.i(ItemsDataBase.TAG, "Folder zipped to: "+dirPath+"/"+name);
									tvTest.setText("Folder zipped to: "+dirPath+"/"+name);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}).create();
				completeOK.show();
			}
		});

		btnRestore.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				final String testFileName="BackUp.zip";
				final String testPackagePath=Environment.getDataDirectory()+"/data/"+getApplicationContext().getPackageName();
				String dest=Environment.getExternalStorageDirectory().getAbsolutePath()+"/My Wallet";
				final File zip=new File(dest, testFileName);
				final File extractTo=new File(testPackagePath);

				AlertDialog.Builder builder=new AlertDialog.Builder(BackupAndRestore.this); //Notify user all is OK
				AlertDialog completeOK=builder.setTitle("Information").setIcon(android.R.drawable.ic_menu_info_details).setMessage("Folder zipped to: "+testPackagePath+"/"+testFileName)
						.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								try {
									zUtility.unzip(zip, extractTo);
									Log.i(ItemsDataBase.TAG, "Folder unzipped to: "+testPackagePath+"/"+testFileName);
									tvTest.setText("Folder unzipped to: "+testPackagePath+"/"+testFileName);

//									String fullPath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/My Wallet/"+"BackUp.zip";
//									Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND); 
//									emailIntent.setType("application/zip"); 	// ("application/csv") - CSV file
//									emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {"idanmos@gmail.com"}); 
//									emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My Wallet zip backup"); 
//									emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "This is an automatic email with an attached BackUp.zip file.\n\rPlease do not respond."); 
//									Log.v(ItemsDataBase.TAG, "fullPath=" + Uri.parse(fullPath));
//									emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(fullPath));
//									startActivity(Intent.createChooser(emailIntent, "Send Mail"));
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}).create();
				completeOK.show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.backup_and_restore_layout, menu);
		return true;
	}

}
