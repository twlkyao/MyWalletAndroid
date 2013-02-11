package il.co.idanmoshe.digitalwallet;

import java.util.ArrayList;

import Utils.idanUtils;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class HistoryIncome extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history_income_layout);
		
		idanUtils idanUtils=new idanUtils();
		ListView lvIncomeHistory=(ListView)findViewById(R.id.listView1);
		final ArrayList<Income>incomeList=new ArrayList<Income>();
		Cursor sortCursor=MainActivity.itemsDataBase.queryIncome();
		
		while (sortCursor.moveToNext()) {
			incomeList.add(new Income(
					sortCursor.getString(sortCursor.getColumnIndex(ItemsDataBase.C_INCOME_DESCRIPTION)),
					sortCursor.getInt(sortCursor.getColumnIndex(ItemsDataBase.C_INCOME_MONEY_AMOUNT)),
					sortCursor.getString(sortCursor.getColumnIndex(ItemsDataBase.C_INCOME_INCOME_OPTION)),
					sortCursor.getString(sortCursor.getColumnIndex(ItemsDataBase.C_INCOME_PAYEE_NAME)),
					sortCursor.getString(sortCursor.getColumnIndex(ItemsDataBase.C_INCOME_VOICE_RECORD_URL)),
					sortCursor.getString(sortCursor.getColumnIndex(ItemsDataBase.C_INCOME_DATE)),
					sortCursor.getString(sortCursor.getColumnIndex(ItemsDataBase.C_INCOME_TIME)),
					sortCursor.getString(sortCursor.getColumnIndex(ItemsDataBase.C_INCOME_LOCATION)),
					sortCursor.getString(sortCursor.getColumnIndex(ItemsDataBase.C_INCOME_PHOTO_URL))));
		}
		
		ArrayAdapter<Income>incomeAdapter=new ArrayAdapter<Income>(this, android.R.layout.simple_list_item_1, incomeList);
		lvIncomeHistory.setAdapter(incomeAdapter);
		lvIncomeHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				Income income=incomeList.get(position);
				Intent intent=new Intent(HistoryIncome.this, ShowIncome.class);
				intent.putExtra("incomeName", income);
				startActivity(intent);
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.history_income_layout, menu);
		return true;
	}

}
