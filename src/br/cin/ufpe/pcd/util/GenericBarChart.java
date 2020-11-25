package br.cin.ufpe.pcd.util;

import java.util.List;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.Styler.LegendPosition;

public class GenericBarChart {
	
	private String title;
	private String xAxisTitle;
	private String yAxisTitle;
	
	private List<ChartSeries> data;
	
	public GenericBarChart() {

	}
	
	public GenericBarChart(String title, String xAxisTitle, String yAxisTitle) {
		this.title = title;
		this.xAxisTitle = xAxisTitle;
		this.yAxisTitle = yAxisTitle;
	}
	
	public void show() {
		CategoryChart chart = this.createChart();
		new SwingWrapper<CategoryChart>(chart).displayChart();
	}

	public CategoryChart createChart() {

		// Create Chart
		CategoryChart chart = new CategoryChartBuilder()
														.width(800)
														.height(600)
														.title(this.title)
														.xAxisTitle(this.xAxisTitle)
														.yAxisTitle(this.yAxisTitle)
														.build();

		// Customize Chart
		chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
		chart.getStyler().setHasAnnotations(true);

		// Series
		for (ChartSeries chartSeries : data) {			
			chart.addSeries (chartSeries.getName(), chartSeries.getxData(), chartSeries.getyData());
		}

		return chart;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getxAxisTitle() {
		return xAxisTitle;
	}

	public void setxAxisTitle(String xAxisTitle) {
		this.xAxisTitle = xAxisTitle;
	}

	public String getyAxisTitle() {
		return yAxisTitle;
	}

	public void setyAxisTitle(String yAxisTitle) {
		this.yAxisTitle = yAxisTitle;
	}

	public List<ChartSeries> getData() {
		return data;
	}

	public void setData(List<ChartSeries> data) {
		this.data = data;
	}

}
