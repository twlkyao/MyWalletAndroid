package il.co.idanmoshe.digitalwallet;

import Utils.idanUtils;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity {

	public static ItemsDataBase itemsDataBase;
	private Intent intent;
	private idanUtils tempIdanUtils=new idanUtils();
	
	ImageButton btnAddItem, btnHistory, btnSettings;
	TextView tvMoneyStatus;
	Integer expenseCount, incomeCount, balance;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		/*-----------------------------------------------*/
		btnAddItem=(ImageButton)findViewById(R.id.imageButton1);
		btnHistory=(ImageButton)findViewById(R.id.imageButtonHistoryExpense);
		tvMoneyStatus=(TextView)findViewById(R.id.textView1);
		btnSettings=(ImageButton)findViewById(R.id.imageButton2);
		/*---------------Initialize----------------------*/
		tempIdanUtils.createMyWalletFolderOnlyOneTime();
		itemsDataBase=new ItemsDataBase(this);
		itemsDataBase.open();
		showBalance();
		GetTablesData collect1 = new GetTablesData();
		collect1.getAllExpensesDetails();
		GetIncomeTableData collect2 = new GetIncomeTableData();
		collect2.getAllIncomeDetails();
		getUniquePhoneID();
		/*--------------------Action Bar--------------------*/
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setTitle("Current balance: " + balance + " $");
		actionBar.setSubtitle("Total expenses: " + expenseCount + " $, Total income: " + incomeCount + " $");
		actionBar.show();
		
		/*
		Button btnTestButton = (Button) findViewById(R.id.button1); // TEST - TAB SWIPE
		btnTestButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent testIntent = new Intent(MainActivity.this, TabSwipe1.class);
				startActivity(testIntent);
			}
		});   */
		
		btnSettings.setOnClickListener(new View.OnClickListener() { // SETTINGS

			@Override
			public void onClick(View v) {
//				Intent settingsIntent = new Intent(MainActivity.this, ClassActivity.class);
//				startActivity(settingsIntent);
			}
		});

		btnAddItem.setOnClickListener(new View.OnClickListener() { // ADD EXPENSE

			@Override
			public void onClick(View v) {

				CharSequence addExpense = getResources().getString(R.string.add_expense);
				CharSequence addIncome = getResources().getString(R.string.add_income);

				final CharSequence[] captureOption = { addExpense, addIncome};

				MainActivity.this.getResources();
				AlertDialog.Builder pickBuilder = new AlertDialog.Builder(MainActivity.this); // Context Menu (LOAD OR CAPTURE)
				pickBuilder.setTitle(R.string.please_select);
				pickBuilder.setItems(captureOption, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int choose) {
						if (choose == 0) {		// ADD EXPENSE

							Log.d(ItemsDataBase.TAG, "ADD EXPENSE");
							intent=new Intent(MainActivity.this, AddExpense.class);
							startActivity(intent);
						} else {		// ADD INCOME

							Log.d(ItemsDataBase.TAG, "ADD INCOME");
							intent=new Intent(MainActivity.this, AddIncome.class);
							startActivity(intent);
						}
					}
				});
				AlertDialog alert = pickBuilder.create();
				alert.show();
			}
		});

		btnHistory.setOnClickListener(new View.OnClickListener() { // HISTORY

			@Override
			public void onClick(View v) {
				intent=new Intent(MainActivity.this, HistoryExpense.class);
				startActivity(intent);
			}
		});
	}
	
	public void btnExpenseStats (View view){ // Statistics - Expenses

		CharSequence statExpense = getResources().getString(R.string.statistics_expenses);
		CharSequence statIncome = getResources().getString(R.string.statistics_income);

		final CharSequence[] captureOption = { statExpense, statIncome };

		AlertDialog.Builder pickBuilder = new AlertDialog.Builder(MainActivity.this); // Context Menu (LOAD OR CAPTURE)
		pickBuilder.setTitle(R.string.please_select);
		pickBuilder.setItems(captureOption, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int choose) {
				if (choose == 0) {		// EXPENSE STATISTICS

					Log.d(ItemsDataBase.TAG, "EXPENSE STATISTICS");
					MyPieGraph pie = new MyPieGraph();
					Intent pieChartIntent = pie.getIntent(MainActivity.this);
					startActivity(pieChartIntent);
				} else {		// INCOME STATISTICS

					Log.d(ItemsDataBase.TAG, "INCOME STATISTICS");
					IncomePieGraph pie = new IncomePieGraph();
					Intent pieChartIntent = pie.getIntent(MainActivity.this);
					startActivity(pieChartIntent);
				}
			}
		});
		AlertDialog alert = pickBuilder.create();
		alert.show();

	}

	public Integer getBalance(){ // Get current balance

		int income = GetIncomeTableData.getTotalPrice();
		int expense = GetTablesData.getTotalPrice();
		int sum = income - expense;
		return sum;
	}

	public void getUniquePhoneID(){
		String android_id = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
		Log.v(ItemsDataBase.TAG, "Unique phone ID = "+android_id);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
	public void showBalance(){
		expenseCount = itemsDataBase.getTotalExpenseSum();
		incomeCount = itemsDataBase.getTotalIncomeSum();
		balance =(Integer) expenseCount - incomeCount;
	}

	@Override
	protected void onResume() {
		itemsDataBase.open();
		showBalance();
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater mi=getMenuInflater();
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.addExpense:
			Intent intent1=new Intent(MainActivity.this, AddExpense.class);
			startActivity(intent1);
			break;
		case R.id.addIncome:
			Intent intent2=new Intent(MainActivity.this, AddIncome.class);
			startActivity(intent2);
			break;
		case R.id.statistics:
			IncomePieGraph pie = new IncomePieGraph();
			Intent pieChartIntent = pie.getIntent(this);
			startActivity(pieChartIntent);
			break;
		case R.id.historyIncome:
			Intent intent4=new Intent(MainActivity.this, HistoryIncome.class);
			startActivity(intent4);
			break;
		case R.id.backupAndRestore:
			Intent intent5=new Intent(MainActivity.this, BackupAndRestore.class);
			startActivity(intent5);
			break;
		case R.id.menu_settings:
//			Intent intent6 = new Intent(MainActivity.this, ClassActivity.class);
//			startActivity(intent6);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
