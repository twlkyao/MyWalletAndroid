package il.co.idanmoshe.digitalwallet;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.R.color;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

public class IncomePieGraph {

	protected int[] values = { 
			GetIncomeTableData.totalSalary, GetIncomeTableData.totalMoneyTransfer, GetIncomeTableData.totalTaxRefund, 
			GetIncomeTableData.totalPension, GetIncomeTableData.totalPersonalSavings, GetIncomeTableData.totalPartTimeWork, 
			GetIncomeTableData.totalSocialSecurity, GetIncomeTableData.totalDifferent};

	protected String[] categoriesNames = { "Salary", "Money Transfer", "Tax Refund", "Pension", "Personal Savings", 
			"Part-Time Work", "Social Security", "Different"};

	protected Intent getIntent(Context context) {

		CategorySeries series = new CategorySeries("My Wallet - Statistics - Pie Graph");
		int k = 0;
		for (int value : values) {
			series.add(categoriesNames[k], value);
			k++;
		}

		int[] colors=null;

		if (values.length == 8) {
			colors = new int[] { Color.BLUE, Color.GREEN, Color.MAGENTA, Color.YELLOW, Color.CYAN, Color.LTGRAY, Color.BLACK, Color.DKGRAY };
		}
		else if (values.length == 7) {
			colors = new int[] { Color.BLUE, Color.GREEN, Color.MAGENTA, Color.YELLOW, Color.CYAN, Color.LTGRAY, Color.BLACK };
		}
		else if (values.length == 6) {
			colors = new int[] { Color.BLUE, Color.GREEN, Color.MAGENTA, Color.YELLOW, Color.CYAN, Color.LTGRAY };
		}
		else if (values.length == 5) {
			colors = new int[] { Color.BLUE, Color.GREEN, Color.MAGENTA, Color.YELLOW, Color.CYAN };
		}
		else if (values.length == 4) {
			colors = new int[] { Color.BLUE, Color.GREEN, Color.MAGENTA, Color.YELLOW };
		}
		else if (values.length == 3) {
			colors = new int[] { Color.BLUE, Color.GREEN, Color.MAGENTA };
		}
		else if (values.length == 2) {
			colors = new int[] { Color.BLUE, Color.GREEN };
		}
		else if (values.length == 1) {
			colors = new int[] { Color.BLUE };
		}
		else if (values.length == 0) {
			colors = new int[] { Color.BLACK };
		}

		DefaultRenderer renderer = new DefaultRenderer();
		renderer.setLabelsColor(Color.RED);
		renderer.setChartTitle("Total Income");
		renderer.setLabelsTextSize(20);
		renderer.setChartTitleTextSize(20);
		for (int color : colors) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(color);
			renderer.addSeriesRenderer(r);
		}
		renderer.setChartTitle("Pie Chart  - Income - Statistics");
		renderer.setChartTitleTextSize(7);
		renderer.setZoomButtonsVisible(true);

		Intent intent = ChartFactory.getPieChartIntent(context, series, renderer, "Pie");
		return intent;
	}

}
