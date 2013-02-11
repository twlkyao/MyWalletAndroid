package il.co.idanmoshe.digitalwallet;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.database.Cursor;
import android.util.Log;

public class GetIncomeTableData extends Activity{


	// Get all INCOME TABLE data and save it

	HashMap<Integer, Income>incomeHashMap=new HashMap<Integer, Income>(); // TABLE
	HashMap<Integer, Income>incomeByCategory=new HashMap<Integer, Income>(); // Categories
	static ArrayList<Integer>moneyAmountList=new ArrayList<Integer>();

	ArrayList<Expense>categorySalary = new ArrayList<Expense>(); // Total price - Salary
	ArrayList<Expense>categoryMoneyTransfer = new ArrayList<Expense>(); // Total price - Money Transfer
	ArrayList<Expense>categoryTaxRefund = new ArrayList<Expense>(); // Total price - Tax Refund
	ArrayList<Expense>categoryPension = new ArrayList<Expense>(); // Total price - Pension
	ArrayList<Expense>categoryPersonalSavings = new ArrayList<Expense>(); // Total price - Personal Savings
	ArrayList<Expense>categoryPartTimeWork = new ArrayList<Expense>(); // Total price - Part-Time Work
	ArrayList<Expense>categorySocialSecurity = new ArrayList<Expense>(); // Total price - Social Security
	ArrayList<Expense>categoryDifferent = new ArrayList<Expense>(); // Total price - Different
	
	public static int totalSalary;
	public static int totalMoneyTransfer;
	public static int totalTaxRefund;
	public static int totalPension;
	public static int totalPersonalSavings;
	public static int totalPartTimeWork;
	public static int totalSocialSecurity;
	public static int totalDifferent;

	public void getIncomeDataTable(){

		Cursor expenseCurser=MainActivity.itemsDataBase.queryIncome();
		
		int index=1;
		
		while (expenseCurser.moveToNext()) {   /* Put all expense table to HashMap */
			Income income=new Income(
					expenseCurser.getString(expenseCurser.getColumnIndex(ItemsDataBase.C_INCOME_DESCRIPTION)),
					expenseCurser.getShort(expenseCurser.getColumnIndex(ItemsDataBase.C_INCOME_MONEY_AMOUNT)), 
					expenseCurser.getString(expenseCurser.getColumnIndex(ItemsDataBase.C_INCOME_INCOME_OPTION)), 
					expenseCurser.getString(expenseCurser.getColumnIndex(ItemsDataBase.C_INCOME_PAYEE_NAME)), 
					expenseCurser.getString(expenseCurser.getColumnIndex(ItemsDataBase.C_INCOME_VOICE_RECORD_URL)), 
					expenseCurser.getString(expenseCurser.getColumnIndex(ItemsDataBase.C_INCOME_DATE)), 
					expenseCurser.getString(expenseCurser.getColumnIndex(ItemsDataBase.C_INCOME_TIME)), 
					expenseCurser.getString(expenseCurser.getColumnIndex(ItemsDataBase.C_INCOME_LOCATION)),
					expenseCurser.getString(expenseCurser.getColumnIndex(ItemsDataBase.C_INCOME_PHOTO_URL)));
			incomeHashMap.put(index, income);

			moneyAmountList.add((int) expenseCurser.getShort(expenseCurser.getColumnIndex(ItemsDataBase.C_INCOME_MONEY_AMOUNT)));
			index++;
		}
		
		getTotalSalary();
		getTotalMoneyTransfer();
		getTotalTaxRefund();
		getTotalPension();
		getTotalPersonalSavings();
		getTotalPartTimeWork();
		getTotalSocialSecurity();
		getTotalDifferent();
		
		getAllIncomeDetails();
		getTotalPrice();
	}
	
	public void getAllIncomeDetails(){
//		Log.d(ItemsDataBase.TAG, "--Get All Expenses Details---");
		for (HashMap.Entry entry : incomeHashMap.entrySet()) {
//			Log.v(ItemsDataBase.TAG, "["+entry.getKey() + "] " + entry.getValue());
		}
//		Log.d(ItemsDataBase.TAG, "--Get All Expenses Details---");
	}
	
	public static int getTotalPrice(){
//		Log.d(ItemsDataBase.TAG, "-------Get Total Price-------");
		Integer sum=0;
		for (Integer i : moneyAmountList) {
			sum += i;
		}
//		Log.v(ItemsDataBase.TAG, "Total= "+sum);
//		Log.d(ItemsDataBase.TAG, "-------Get Total Price-------");
		return sum;
	}
	
	// Get prices by category
	
	public Integer getTotalSalary(){
		Integer isSalary = MainActivity.itemsDataBase.getSumByIncomeCayegory("Salary");
//		Log.i(ItemsDataBase.TAG, "Total income on salary: "+isSalary);
		totalSalary = isSalary;
		return isSalary;
	}

	public Integer getTotalMoneyTransfer(){
		Integer isMoneyTransfer = MainActivity.itemsDataBase.getSumByIncomeCayegory("Money Transfer");
//		Log.i(ItemsDataBase.TAG, "Total income on money transfer: "+isMoneyTransfer);
		totalMoneyTransfer = isMoneyTransfer;
		return isMoneyTransfer;
	}

	public Integer getTotalTaxRefund(){
		Integer isTaxRefund = MainActivity.itemsDataBase.getSumByIncomeCayegory("Tax Refund");
//		Log.i(ItemsDataBase.TAG, "Total income on tax refund: "+isTaxRefund);
		totalTaxRefund = isTaxRefund;
		return isTaxRefund;
	}

	public Integer getTotalPension(){
		Integer isPension = MainActivity.itemsDataBase.getSumByIncomeCayegory("Pension");
//		Log.i(ItemsDataBase.TAG, "Total income on pension: "+isPension);
		totalPension = isPension;
		return isPension;
	}

	public Integer getTotalPersonalSavings(){
		Integer isPersonalSavings = MainActivity.itemsDataBase.getSumByIncomeCayegory("Personal Savings");
//		Log.i(ItemsDataBase.TAG, "Total income on personal savings: "+isPersonalSavings);
		totalPersonalSavings = isPersonalSavings;
		return isPersonalSavings;
	}

	public Integer getTotalPartTimeWork(){
		Integer isPartTimeWork = MainActivity.itemsDataBase.getSumByIncomeCayegory("Part-Time Work");
//		Log.i(ItemsDataBase.TAG, "Total income on part-time work: "+isPartTimeWork);
		totalPartTimeWork = isPartTimeWork;
		return isPartTimeWork;
	}

	public Integer getTotalSocialSecurity(){
		Integer isSocialSecurity = MainActivity.itemsDataBase.getSumByIncomeCayegory("Social Security");
//		Log.i(ItemsDataBase.TAG, "Total income on social security: "+isSocialSecurity);
		totalSocialSecurity = isSocialSecurity;
		return isSocialSecurity;
	}

	public Integer getTotalDifferent(){
		Integer isDifferent = MainActivity.itemsDataBase.getSumByIncomeCayegory("Different");
//		Log.i(ItemsDataBase.TAG, "Total income on different: "+isDifferent);
		totalDifferent = isDifferent;
		return isDifferent;
	}

}
