package il.co.idanmoshe.digitalwallet;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class HistoryExpense extends Activity {

	ArrayList<Integer>expenseID; //= new ArrayList<Integer>(); // All ID's
	Integer[] allExpID;
	Integer real_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history_activity);

		//		expenseID.clear();
		expenseID = new ArrayList<Integer>();
		allExpID = new Integer[500];
		
		ListView lvItemsHistory=(ListView)findViewById(R.id.listView1);
		final ArrayList<Expense>expensesList=new ArrayList<Expense>();
		Cursor sortCurser=MainActivity.itemsDataBase.query();


		int temp = 0;
		while (sortCurser.moveToNext()) {
			expensesList.add( new Expense(
					sortCurser.getString(sortCurser.getColumnIndex(ItemsDataBase.COLUMN_DESCRIPTION)),
					sortCurser.getInt(sortCurser.getColumnIndex(ItemsDataBase.COLUMN_PRICE)),
					sortCurser.getString(sortCurser.getColumnIndex(ItemsDataBase.COLUMN_PAYMENT_OPTION)),
					sortCurser.getString(sortCurser.getColumnIndex(ItemsDataBase.COLUMN_ITEM_CATEGORY)),
					sortCurser.getString(sortCurser.getColumnIndex(ItemsDataBase.COLUMN_PHOTO_URL)),
					sortCurser.getString(sortCurser.getColumnIndex(ItemsDataBase.COLUMN_LOCATION)),
					sortCurser.getString(sortCurser.getColumnIndex(ItemsDataBase.COLUMN_DATE)),
					sortCurser.getString(sortCurser.getColumnIndex(ItemsDataBase.COLUMN_TIME)),
					sortCurser.getString(sortCurser.getColumnIndex(ItemsDataBase.COLUMN_VOICE_RECORD_URL))));

//			expenseID.add(temp, sortCurser.getInt(sortCurser.getColumnIndex(ItemsDataBase.COLUMN_ID))); // All ID's
			allExpID[temp] = sortCurser.getInt(sortCurser.getColumnIndex(ItemsDataBase.COLUMN_ID)); // All ID's
			temp++;
		}
		printAllID();
		
		ArrayAdapter<Expense>itemsAdapter=new ArrayAdapter<Expense>(this, android.R.layout.simple_list_item_1, expensesList);
		lvItemsHistory.setAdapter(itemsAdapter);
		lvItemsHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				//				_id = expenseID.get(position);
				real_id = allExpID[position];
				Log.d(ItemsDataBase.TAG, "_id Position = " + real_id);
				Expense expense=expensesList.get(position + 1);
				Intent intent=new Intent(HistoryExpense.this, ShowExpense.class);
				intent.putExtra("itemName", expense);
				intent.putExtra("positionExpense", real_id);
				startActivity(intent);
			}
		});
	}

	void printAllID(){
//		for (int i = 0; i < expenseID.size(); i++) {
//			Log.i(ItemsDataBase.TAG, "_id = " + expenseID.get(i));
//		}
		for (int i = 0; i < allExpID.length; i++) {
			if (allExpID[i] == null) {
				return;
			}
			Log.d(ItemsDataBase.TAG, "_id = " + allExpID[i]);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//		expenseID.clear();
		expenseID = new ArrayList<Integer>();
		allExpID = new Integer[500];
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.history_activity, menu);
		return true;
	}

}