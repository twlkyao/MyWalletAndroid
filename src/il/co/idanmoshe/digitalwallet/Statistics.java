package il.co.idanmoshe.digitalwallet;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;


public class Statistics extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.statistics_layout);
		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.history_activity, menu);
		return true;
	}
}
