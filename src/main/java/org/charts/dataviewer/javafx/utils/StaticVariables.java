package org.charts.dataviewer.javafx.utils;

/**
 * Static final Variables (configuration, icons, tooltip messages lie here)
 *  
 * @author jasrodis
 */
public class StaticVariables {
	
	/********************************************************************************
	 * Config
	 ********************************************************************************/
	public static final int MIN_PORT = 8090;
	public static final int MAX_PORT = 8099;
	public static int PORT = 8090;

	public static final String DATAVIEWER_URL = "http://localhost:8090/view/";

	public static final String SERVLET_MAPPING = "/view/*";
	public static final String RESOURCE_BASE = "./src/main/resources/";
	public static final String WS_ENDPOINT = "/charts/";
	public static final String HTML_FILE = "/webapp/html/charts.html";

	public static final int WS_TIMEOUT = 10000000;

	/********************************************************************************
	 * View
	 ********************************************************************************/
	// ComboBox Selection
	public static final String LINE = "line";
	public static final String BAR = "bar";
	public static final String SCATTER = "scatter";
	public static final String LINEANDMARKS = "line+marks";

	// Tooltips
	public static final String TRACETYPE_TP = "Please select trace type";
	public static final String LOG_TP = "Change to logarithmic scales";
	public static final String TABLE_TP = "Show data on a table";
	public static final String LEGEND_TP = "Show legend";
	public static final String EXPORT_TP = "Export data to csv";

	/********************************************************************************
	 * Icons
	 ********************************************************************************/
	public static final String LOG_ICON = "/icons/logarithmic.png";
	public static final String EXPORT_ICON = "/icons/export.gif";
	public static final String TABLE_ICON = "/icons/table.png";
	public static final String LEGEND_ICON = "/icons/legend.gif";

	public static final String LINETRACE_ICON = "/icons/polyline.png";
	public static final String BARTRACE_ICON = "/icons/bars.png";
	public static final String SCATTERTRACE_ICON = "/icons/scatter.png";
	public static final String LINEANDMARKSTRACE_ICON = "/icons/polyline_with_markers.png";
	
	
}
