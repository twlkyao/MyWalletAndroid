package il.co.idanmoshe.digitalwallet;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ImageButton;

public class EditExpense extends Activity {
	
	Expense expense;
	ImageButton btnLocation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.edit_expense);
		Bundle bundle=getIntent().getExtras();
		expense = (Expense)bundle.getSerializable("expenseName");
		
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_expense, menu);
		return true;
	}

}
