package org.charts.dataviewer.javafx.menubar;

import javafx.beans.property.SimpleDoubleProperty;

public class PlotDataTableModel {

	private final SimpleDoubleProperty xdata;
	private final SimpleDoubleProperty ydata;
	private final SimpleDoubleProperty zdata;

	public PlotDataTableModel(double xData, double yData, double zData) {
		this.xdata = new SimpleDoubleProperty();
		this.ydata = new SimpleDoubleProperty();
		this.zdata = new SimpleDoubleProperty();
		setXdata(xData);
		setYdata(yData);
		setZdata(zData);
	}

	public Double getXdata() {
		return xdata.get();
	}

	public void setXdata(Double xData) {
		this.xdata.set(xData);
	}

	public Double getYdata() {
		return ydata.get();
	}

	public void setYdata(Double yData) {
		this.ydata.set(yData);
	}

	public Double getZdata() {
		return zdata.get();
	}

	public void setZdata(Double zData) {
		this.zdata.set(zData);
	}

}
