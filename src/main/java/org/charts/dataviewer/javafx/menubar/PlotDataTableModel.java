package org.charts.dataviewer.javafx.menubar;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class PlotDataTableModel {

	private final ObjectProperty<Object> xdata;
	private final ObjectProperty<Object> ydata;
	private final ObjectProperty<Object> zdata;

	public PlotDataTableModel(Object xData, Object yData, Object zData) {
		this.xdata = new SimpleObjectProperty<>();
		this.ydata = new SimpleObjectProperty<>();
		this.zdata = new SimpleObjectProperty<>();
		setXdata(xData);
		setYdata(yData);
		setZdata(zData);
	}

	public Object getXdata() {
		return xdata.get();
	}

	public void setXdata(Object xData) {
		this.xdata.set(xData);
	}

	public Object getYdata() {
		return ydata.get();
	}

	public void setYdata(Object yData) {
		this.ydata.set(yData);
	}

	public Object getZdata() {
		return zdata.get();
	}

	public void setZdata(Object zData) {
		this.zdata.set(zData);
	}

}
