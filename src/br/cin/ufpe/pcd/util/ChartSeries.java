package br.cin.ufpe.pcd.util;

import java.util.Arrays;
import java.util.List;

public class ChartSeries {
	
	private String name;
	private List<?> xData;
	private List<? extends Number> yData;
	
	public ChartSeries() {

	}
	
	public ChartSeries(String name, List<?> xData, List<? extends Number> yData) {
		this.name = name;
		this.xData = xData;
		this.yData = yData;
	}

	public ChartSeries(String name, Number[] xDataArray, Number[] yDataArray) {
		this.name = name;
		this.xData = Arrays.asList(xDataArray);
		this.yData = Arrays.asList(yDataArray);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<?> getxData() {
		return xData;
	}

	public void setxData(List<?> xData) {
		this.xData = xData;
	}

	public List<? extends Number> getyData() {
		return yData;
	}

	public void setyData(List<? extends Number> yData) {
		this.yData = yData;
	}

}
