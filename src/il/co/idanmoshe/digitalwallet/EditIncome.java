package il.co.idanmoshe.digitalwallet;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class EditIncome extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_income);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_income, menu);
		return true;
	}

}
