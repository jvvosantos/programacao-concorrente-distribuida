package br.cin.ufpe.pcd.util;

import java.util.Arrays;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.Styler.LegendPosition;

public class BarChartExample {

	public static void main(String[] args) {

		BarChartExample exampleChart = new BarChartExample();
		CategoryChart chart = exampleChart.getChart();
		new SwingWrapper<CategoryChart>(chart).displayChart();
	}

	public CategoryChart getChart() {

		// Create Chart
		CategoryChart chart = new CategoryChartBuilder()
														.width(800)
														.height(600)
														.title("Score Histogram")
														.xAxisTitle("Score")
														.yAxisTitle("Number").build();

		// Customize Chart
		chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
		chart.getStyler().setHasAnnotations(true);

		// Series
		chart.addSeries (
						"test 1", 
						Arrays.asList(new Integer[] { 0, 1, 2, 3, 4 }), 
						Arrays.asList(new Long[] { 4L, 5L, 9L, 6L, 5L }));

		return chart;
	}
}
