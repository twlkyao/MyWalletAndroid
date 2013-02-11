package il.co.idanmoshe.digitalwallet;

import Utils.idanUtils;
import android.app.ActionBar;
import android.app.Application;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

public class TabSwipe1 extends FragmentActivity implements
		ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab_swipe1);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		Integer expenseCount, incomeCount, balance;
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayUseLogoEnabled(false);
		expenseCount = MainActivity.itemsDataBase.getTotalExpenseSum();
		incomeCount = MainActivity.itemsDataBase.getTotalIncomeSum();
		balance =(Integer) expenseCount - incomeCount;
		actionBar.setTitle("Current balance: " + balance + " $");
		actionBar.setSubtitle("Total expenses: " + expenseCount + " $, Total income: " + incomeCount + " $");

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_tab_swipe1, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = null;			
			switch (position) {
			case 0:
				fragment = new AddExpenses();
				break;
			case 1:
				fragment = new AddIncomeSource();
				break;
			case 2:
				fragment = new HistoryExpense();
				break;
			case 3:
				fragment = new HistoryIncome();
				break;
			default:
				fragment = new AddExpenses();
				break;
			}
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 7 total pages.
			return 7;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.add_expense);
			case 1:
				return getString(R.string.add_income);
			case 2:
				return getString(R.string.title_activity_history);
			case 3:
				return getString(R.string.title_activity_history_income);
			case 4:
				return getString(R.string.statistics_expenses);
			case 5:
				return getString(R.string.statistics_income);
			case 6:
				return getString(R.string.menu_settings);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class AddExpenses extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";
		
		public AddExpenses() {
			idanUtils idanUtils=new idanUtils();

			String isDate = idanUtils.getFixedDateStamp();
			String isTime = idanUtils.getFixedTimeStamp();
			String strDescription;
			String isAudioNote, productCategory, paymentOption;
			Integer strPrice;

//			EditText etDescreption = (EditText)findViewById(R.id.editText1);
//			EditText etPrice = (EditText)findViewById(R.id.editText2);
			Spinner spnPaymentOption, spnProductCategory;
			Button btnTakePhoto, btnLoadImageFromGallery;
			ImageButton btnPhotoHandle, btnRecordAudioNote, btnOK;
			ImageButton tbLocation;
			
//			spnPaymentOption=(Spinner)findViewById(R.id.spinner1);
//			spnProductCategory=(Spinner)findViewById(R.id.spinner2);
//			btnOK=(ImageButton)findViewById(R.id.imageButtonOkExpense);
//			btnTakePhoto=(Button)findViewById(R.id.button1);
//			btnRecordAudioNote=(ImageButton)findViewById(R.id.imageButtonAudioNote);
//			btnPhotoHandle = (ImageButton) findViewById(R.id.imageButtonPhoto);
//			btnLoadImageFromGallery=(Button)findViewById(R.id.button3);
//			tbLocation = (ImageButton) findViewById(R.id.imageButton1);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.add_expense, null);
			
			
			
			
			return v;
		}
		@Override
		public void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			// TODO Auto-generated method stub
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	public static class AddIncomeSource extends Fragment {
		public static final String ARG_SECTION_NUMBER = "section_number";
		
		public AddIncomeSource() {
		}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.add_income, null);
//			Bitmap bit2 = il.co.idanmoshe.digitalwallet.AddExpense.bitmap2;
			
			
			
			return v;
		}
	}
	
	public static class HistoryExpense extends Fragment {
		public static final String ARG_SECTION_NUMBER = "section_number";
		
		public HistoryExpense() {
		}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			View v = inflater.inflate(R.layout.history_activity, null);
			return v;
		}
	}
	
	public static class HistoryIncome extends Fragment {
		public static final String ARG_SECTION_NUMBER = "section_number";
		
		public HistoryIncome() {
		}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			View v = inflater.inflate(R.layout.history_income_layout, null);
			return v;
		}
	}
}
