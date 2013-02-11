package il.co.idanmoshe.digitalwallet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class ItemsDataBase {

	public static final String TAG="MyWallet"; 
	public static final String DB_NAME="database_items.db";
	public static final int DB_VERSION=1;

	//-------------------- Table Expenses -------------------- //

	public static final String TABLE="itemsDataTable";
	public static final String COLUMN_ID=BaseColumns._ID; 
	public static final String COLUMN_DESCRIPTION="description";
	public static final String COLUMN_PRICE="price";
	public static final String COLUMN_PHOTO_URL="photo_url";
	public static final String COLUMN_PAYMENT_OPTION="payment_option";
	public static final String COLUMN_ITEM_CATEGORY="item_category";
	public static final String COLUMN_LOCATION="location";
	public static final String COLUMN_DATE="date";
	public static final String COLUMN_TIME="time";
	public static final String COLUMN_VOICE_RECORD_URL="voice_record_url";

	public static Integer expenseTotal = 0;
	public static String[] expenseCategories = { 
		"Fashion", "Home & Decor", "Outdoors", "Electronics",
		"Sporting Goods", "Motors", "Art & Collectibles", "Different" };

	public static final String CREATE_EXPENSE_TABLE=String.format(
			"create table %s (%s integer primary key autoincrement not null, %s text, %s int, %s text," +
					" %s text, %s text, %s text, %s text, %s text, %s text)",
					TABLE, COLUMN_ID, COLUMN_DESCRIPTION, COLUMN_PRICE, COLUMN_PHOTO_URL,
					COLUMN_PAYMENT_OPTION, COLUMN_ITEM_CATEGORY, COLUMN_LOCATION,
					COLUMN_DATE, COLUMN_TIME, COLUMN_VOICE_RECORD_URL);

	//-------------------- Table Expenses -------------------- //


	//-------------------- Table Income -------------------- //

	public static final String TABLE_INCOME="incomeDataTable";
	public static final String C_INCOME_ID=BaseColumns._ID;
	public static final String C_INCOME_DATE="date";
	public static final String C_INCOME_TIME="time";
	public static final String C_INCOME_LOCATION="location";
	public static final String C_INCOME_INCOME_OPTION="income_option";
	public static final String C_INCOME_DESCRIPTION="description";
	public static final String C_INCOME_MONEY_AMOUNT="money_amount";
	public static final String C_INCOME_PAYEE_NAME="payee_name";
	public static final String C_INCOME_VOICE_RECORD_URL="voice_record";
	public static final String C_INCOME_PHOTO_URL="photo_url";

	public static Integer incomeTotal = 0;
	public static String[] incomeCategories = { 
		"Salary", "Money Transfer", "Tax Refund", "Pension", 
		"Personal Savings", "Part-Time Work", "Social Security", "Different"};

	public static final String CREATE_INCOME_TABLE=String.format(
			"create table %s (%s integer primary key autoincrement not null, %s text, %s int, %s text," +
					" %s text, %s text, %s text, %s text, %s text, %s text)",
					TABLE_INCOME, C_INCOME_ID, C_INCOME_DESCRIPTION, C_INCOME_MONEY_AMOUNT, C_INCOME_INCOME_OPTION,
					C_INCOME_PAYEE_NAME, C_INCOME_VOICE_RECORD_URL, C_INCOME_DATE, C_INCOME_TIME, C_INCOME_LOCATION, C_INCOME_PHOTO_URL);

	//-------------------- Table Income -------------------- //


	//-------------------- Table User Name & Password -------------------- //

	//	public static final String TABLE_USER_LOGIN = "loginDataTable";
	//	public static final String C_USER_NAME = "user_name";
	//	public static final String C_PASSWORD = "password";
	//	public static final String C_LOGIN_TABLE_ID = BaseColumns._ID;
	//	
	//	public static final String CREATE_USER_LOGIN_TABLE = String.format(
	//			"create table %s (%s integer primary key autoincrement not null, %s text, %s text)",
	//			TABLE_INCOME, C_LOGIN_TABLE_ID, C_USER_NAME, C_PASSWORD);

	//-------------------- Table User Name & Password -------------------- //


	Context context;
	static DbHelper dbHelper; // I changed to static --> 31 December
	static SQLiteDatabase db; // I changed to static --> 31 December

	public ItemsDataBase(Context context){ // Initializing
		this.context=context;
		dbHelper=new DbHelper();
	}

	public void insert(Expense item){ // Insert Item
		db=dbHelper.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put(COLUMN_DESCRIPTION, item.strDescription);
		values.put(COLUMN_PRICE, item.strPrice);
		values.put(COLUMN_PHOTO_URL, item.photoURL);
		values.put(COLUMN_PAYMENT_OPTION, item.paymentOption);
		values.put(COLUMN_ITEM_CATEGORY, item.productCategory);
		values.put(COLUMN_LOCATION, item.isLocation);
		values.put(COLUMN_DATE, item.isDate);
		values.put(COLUMN_TIME, item.isTime);
		values.put(COLUMN_VOICE_RECORD_URL, item.isAudioNote);

		db.insertWithOnConflict(TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
	}

	public void insertIncome(Income income){ // Insert Income
		db=dbHelper.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put(C_INCOME_DESCRIPTION, income.income_description);
		values.put(C_INCOME_MONEY_AMOUNT, income.money_amount);
		values.put(C_INCOME_INCOME_OPTION, income.income_option);
		values.put(C_INCOME_PAYEE_NAME, income.payee_name);
		values.put(C_INCOME_VOICE_RECORD_URL, income.income_voice_record);
		values.put(C_INCOME_DATE, income.income_date);
		values.put(C_INCOME_TIME, income.income_time);
		values.put(C_INCOME_LOCATION, income.income_location);
		values.put(C_INCOME_PHOTO_URL, income.photo_url);

		db.insertWithOnConflict(TABLE_INCOME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
	}

	//	public void insertUserAndPass(String userName, String base64Key){ // Insert UserName & Encoded Key
	//		db=dbHelper.getWritableDatabase();
	//		ContentValues values = new ContentValues();
	//		values.put(C_USER_NAME, userName);
	//		values.put(C_PASSWORD, base64Key);
	//		
	//		db.insertWithOnConflict(TABLE_USER_LOGIN, null, values, SQLiteDatabase.CONFLICT_IGNORE);
	//	}

	public Integer getSumByCayegory (String strCategory){ // Get sum by expense category
		String command = "SELECT SUM(price) FROM itemsDataTable WHERE item_category LIKE '" + strCategory + "'";
		Cursor cursor = db.rawQuery(command, null);
		int amount;
		if(cursor.moveToFirst())
			amount = cursor.getInt(0);
		else
			amount = -1;
		cursor.close();
		return amount;
	}

	public Integer getTotalExpenseSum(){

		Integer total = 0;
		for (int i = 0; i < expenseCategories.length; i++) {
			expenseTotal = getSumByCayegory(expenseCategories[i]);
			total += expenseTotal;
		}
		Log.d(TAG, "Total Expenses = " + total);
		return total;
	}

	public Integer getTotalIncomeSum(){

		Integer total = 0;
		for (int i = 0; i < incomeCategories.length; i++) {
			incomeTotal = getSumByIncomeCayegory(incomeCategories[i]);
			total += incomeTotal;
		}
		Log.d(TAG, "Total Incomes = " + total);
		return total;
	}

	public Integer getSumByIncomeCayegory (String strIncomeOption){ // Get sum by income category
		String command = "SELECT SUM(money_amount) FROM incomeDataTable WHERE income_option LIKE '" + strIncomeOption + "'";
		Cursor cursor = db.rawQuery(command, null);
		int amount;
		if(cursor.moveToFirst())
			amount = cursor.getInt(0);
		else
			amount = -1;
		cursor.close();
		return amount;
	}

	public static Cursor query(){		
		db=dbHelper.getReadableDatabase();
		Cursor cursor=db.query(TABLE, null, null, null, null, null, null); // SELECT * FROM itemsDataTable
		return cursor;
	}

	public Cursor queryIncome(){
		db=dbHelper.getReadableDatabase();
		Cursor cursor=db.query(TABLE_INCOME, null, null, null, null, null, null); // SELECT * FROM incomeDataTables
		return cursor;
	}

	//	public Cursor queryLogin(){
	//		db = dbHelper.getReadableDatabase();
	//		Cursor cursor = db.query(TABLE_USER_LOGIN, null, null, null, null, null, null); // SELECT * FROM loginDataTable
	//		return cursor;
	//	}

	public void open() throws SQLException{ // Open the database connection
		db=dbHelper.getWritableDatabase();
	}

	public void close(){ // Close the database connection
		dbHelper.close();
	}

	public void deleteAll(){
		db.delete(TABLE, null, null);
		db.delete(TABLE_INCOME, null, null);
	}

	public void deleteExpense(long idToDelete){ // Delete Expense
		Log.d(TAG, "Expense deleted with id: " + idToDelete);
		//		db.delete(TABLE, COLUMN_ID + " = " + idToDelete, null);
		db.delete(TABLE, COLUMN_ID + " =?", new String[] { String.valueOf( idToDelete ) } );
	}

	public void deleteIncome (long idToDelete){ // Delete Income
		Log.d(TAG, "Income deleted with id: "+idToDelete);
		db.delete(TABLE_INCOME, C_INCOME_ID + " = " + idToDelete, null);
	}

	public void updateItem(int id, String columnName, String newValue){ // Update item
		db=dbHelper.getWritableDatabase();
		ContentValues cv=new ContentValues();
		String strFilter = COLUMN_ID + "=" + id;
		cv.put(columnName, newValue);
		db.updateWithOnConflict(TABLE, cv, strFilter, null, SQLiteDatabase.CONFLICT_IGNORE);
	}

	public void updateIncome(int id, String columnName, String newValue){ // Update Income
		db=dbHelper.getWritableDatabase();
		ContentValues cv=new ContentValues();
		String strFilter = C_INCOME_ID + "=" + id;
		cv.put(columnName, newValue);
		db.updateWithOnConflict(TABLE_INCOME, cv, strFilter, null, SQLiteDatabase.CONFLICT_IGNORE);
	}
	
	class DbHelper extends SQLiteOpenHelper{

		public DbHelper() {
			super(context, DB_NAME, null, DB_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			db.execSQL(CREATE_EXPENSE_TABLE);
			db.execSQL(CREATE_INCOME_TABLE);
			//			db.execSQL(CREATE_USER_LOGIN_TABLE);

			Log.d(TAG, "onCreate with SQL: " + CREATE_EXPENSE_TABLE);
			Log.d(TAG, "onCreate with SQL: " + CREATE_INCOME_TABLE);
			//			Log.d(TAG, "onCreate with SQL: " + CREATE_USER_LOGIN_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("drop if exists "+TABLE);
			db.execSQL("drop if exists "+TABLE_INCOME);
			//			db.execSQL("drop if exists "+TABLE_USER_LOGIN);
			onCreate(db);
		}

	}

}
