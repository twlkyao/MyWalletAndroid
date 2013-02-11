package il.co.idanmoshe.digitalwallet;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.database.Cursor;
import android.util.Log;

public class GetTablesData extends Activity {

	// Get all EXPENSE TABLE data and save it

	HashMap<Integer, Expense>expenseHashMap=new HashMap<Integer, Expense>(); // TABLE
	HashMap<Integer, Expense>expenseByCategory=new HashMap<Integer, Expense>(); // Categories
	static ArrayList<Integer>priceList=new ArrayList<Integer>();

	ArrayList<Expense>categoryFashion = new ArrayList<Expense>(); // Total price - Fashion
	ArrayList<Expense>categoryHomeAndDecor = new ArrayList<Expense>(); // Total price - Home & Decor
	ArrayList<Expense>categoryOutdoors = new ArrayList<Expense>(); // Total price - Outdoors
	ArrayList<Expense>categoryElectronics = new ArrayList<Expense>(); // Total price - Electronics
	ArrayList<Expense>categorySportingGoods = new ArrayList<Expense>(); // Total price - SportingGoods
	ArrayList<Expense>categoryMotors = new ArrayList<Expense>(); // Total price - Motors
	ArrayList<Expense>categoryArtAndCollectibles = new ArrayList<Expense>(); // Total price - ArtAndCollectibles
	ArrayList<Expense>categoryDifferent = new ArrayList<Expense>(); // Total price - Different

	public static int totalFashionPrice;
	public static int totalHomeAndDecorPrice;
	public static int totalOutdoors;
	public static int totalElectronics;
	public static int totalSportingGoods;
	public static int totalMotors;
	public static int totalArtAndCollectibles;
	public static int totalDifferent;
	
	public void getExpenseDataTable(){

		Cursor expenseCurser=MainActivity.itemsDataBase.query();

		int index=1;

		while (expenseCurser.moveToNext()) {   /* Put all expense table to HashMap */
			Expense item=new Expense(
					expenseCurser.getString(expenseCurser.getColumnIndex(ItemsDataBase.COLUMN_DESCRIPTION)),
					expenseCurser.getShort(expenseCurser.getColumnIndex(ItemsDataBase.COLUMN_PRICE)), 
					expenseCurser.getString(expenseCurser.getColumnIndex(ItemsDataBase.COLUMN_PAYMENT_OPTION)), 
					expenseCurser.getString(expenseCurser.getColumnIndex(ItemsDataBase.COLUMN_ITEM_CATEGORY)), 
					expenseCurser.getString(expenseCurser.getColumnIndex(ItemsDataBase.COLUMN_PHOTO_URL)), 
					expenseCurser.getString(expenseCurser.getColumnIndex(ItemsDataBase.COLUMN_LOCATION)), 
					expenseCurser.getString(expenseCurser.getColumnIndex(ItemsDataBase.COLUMN_DATE)), 
					expenseCurser.getString(expenseCurser.getColumnIndex(ItemsDataBase.COLUMN_TIME)),
					expenseCurser.getString(expenseCurser.getColumnIndex(ItemsDataBase.COLUMN_VOICE_RECORD_URL)));
			expenseHashMap.put(index, item);

			priceList.add((int) expenseCurser.getShort(expenseCurser.getColumnIndex(ItemsDataBase.COLUMN_PRICE)));
			index++;
		}

		getTotalFashionPrice();
		getTotalArtAndCollectiblesPrice();
		getTotalDifferentPrice();
		getTotalElectonicsPrice();
		getTotalHomeAndDecorPrice();
		getTotalMotorsPrice();
		getTotalSportingGoodsPrice();
		getTotalOutdoorsPrice();

		getAllExpensesDetails();
		getTotalPrice();
	}

	public void getAllExpensesDetails(){
		//		Log.d(ItemsDataBase.TAG, "--Get All Expenses Details---");
		for (HashMap.Entry entry : expenseHashMap.entrySet()) {
			//			Log.v(ItemsDataBase.TAG, "["+entry.getKey() + "] " + entry.getValue());
		}
		//		Log.d(ItemsDataBase.TAG, "--Get All Expenses Details---");
	}

	public static int getTotalPrice(){
		//		Log.d(ItemsDataBase.TAG, "-------Get Total Price-------");
		Integer sum=0;
		for (Integer i : priceList) {
			sum += i;
		}
		//		Log.v(ItemsDataBase.TAG, "Total= "+sum);
		//		Log.d(ItemsDataBase.TAG, "-------Get Total Price-------");
		return sum;
	}

	// Get prices by category

	public Integer getTotalFashionPrice(){
		Integer isFashion = MainActivity.itemsDataBase.getSumByCayegory("Fashion");
		//		Log.i(ItemsDataBase.TAG, "Total expenses on fashion: "+isFashion);
		totalFashionPrice = isFashion;
		return isFashion;
	}

	public Integer getTotalHomeAndDecorPrice(){
		Integer isHomeAndDecor = MainActivity.itemsDataBase.getSumByCayegory("Home & Decor");
		//		Log.i(ItemsDataBase.TAG, "Total expenses on home & decor: "+isHomeAndDecor);
		totalHomeAndDecorPrice = isHomeAndDecor;
		return isHomeAndDecor;
	}

	public Integer getTotalOutdoorsPrice(){
		Integer isOutdoors = MainActivity.itemsDataBase.getSumByCayegory("Outdoors");
		//		Log.i(ItemsDataBase.TAG, "Total expenses on outdoors: "+isOutdoors);
		totalOutdoors = isOutdoors;
		return isOutdoors;
	}

	public Integer getTotalElectonicsPrice(){
		Integer isElectronics = MainActivity.itemsDataBase.getSumByCayegory("Electronics");
		//		Log.i(ItemsDataBase.TAG, "Total expenses on electronics: "+isElectronics);
		totalElectronics = isElectronics;
		return isElectronics;
	}

	public Integer getTotalSportingGoodsPrice(){
		Integer isSportingGoods = MainActivity.itemsDataBase.getSumByCayegory("Sporting Goods");
		//		Log.i(ItemsDataBase.TAG, "Total expenses on spoorting goods: "+isSportingGoods);
		totalSportingGoods = isSportingGoods;
		return isSportingGoods;
	}

	public Integer getTotalMotorsPrice(){
		Integer isMotors = MainActivity.itemsDataBase.getSumByCayegory("Motors");
		//		Log.i(ItemsDataBase.TAG, "Total expenses on motors: "+isMotors);
		totalMotors = isMotors;
		return isMotors;
	}

	public Integer getTotalArtAndCollectiblesPrice(){
		Integer isArtAndCollectibles = MainActivity.itemsDataBase.getSumByCayegory("Art & Collectibles");
		//		Log.i(ItemsDataBase.TAG, "Total expenses on art & collectibles: "+isArtAndCollectibles);
		totalArtAndCollectibles = isArtAndCollectibles;
		return isArtAndCollectibles;
	}

	public Integer getTotalDifferentPrice(){
		Integer isDifferent = MainActivity.itemsDataBase.getSumByCayegory("Different");
		//		Log.i(ItemsDataBase.TAG, "Total expenses on different: "+isDifferent);
		totalDifferent = isDifferent;
		return isDifferent;
	}
}
