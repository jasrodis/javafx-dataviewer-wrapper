package org.charts.dataviewer.javafx;

import org.charts.dataviewer.DataViewer;
import org.charts.dataviewer.Viewer;
import org.charts.dataviewer.api.config.DataViewerConfiguration;
import org.charts.dataviewer.api.data.PlotData;
import org.charts.dataviewer.javafx.menubar.TopMenuBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class JavaFxDataViewer extends BorderPane implements Viewer {

	private static final Logger log = LoggerFactory.getLogger(JavaFxDataViewer.class);

	private DataViewer dataviewer = new DataViewer();

	private boolean enableFireBug = false;

	private DataViewerConfiguration latestConfig;
	private PlotData plotData = new PlotData();

	private TopMenuBar topMenuBar;

	public JavaFxDataViewer() {
		createView();
	}

	private void createView() {
		WebView webView = new WebView();
		webView.setContextMenuEnabled(false);
		WebEngine webEngine = webView.getEngine();
		webEngine.documentProperty().addListener(cl -> {
			log.debug("WebView loaded dataviewer with id : [{}]", dataviewer.getUniqueID());
			if (enableFireBug)
				enableFirebug(webEngine);
		});
		String urlToLoad = dataviewer.getUrl();
		log.debug("Loading: [{}]", urlToLoad);
		webEngine.load(urlToLoad);

		setStyle("-fx-background-color: white;");
		setCenter(webView);

		topMenuBar = new TopMenuBar(plotData, latestConfig, dataviewer);
		setTop(topMenuBar);
	}

	/**
	 * Enabling Firebug console to debug Javascirpt in the JavaFX webview.
	 * 
	 * @param engine
	 */
	private void enableFirebug(final WebEngine engine) {
		// TODO: Create js file
		engine.executeScript(
				"if (!document.getElementById('FirebugLite')){E = document['createElement' + 'NS'] && document.documentElement.namespaceURI;E = E ? document['createElement' + 'NS'](E, 'script') : document['createElement']('script');E['setAttribute']('id', 'FirebugLite');E['setAttribute']('src', 'https://getfirebug.com/' + 'firebug-lite.js' + '#startOpened');E['setAttribute']('FirebugLite', '4');(document['getElementsByTagName']('head')[0] || document['getElementsByTagName']('body')[0]).appendChild(E);E = new Image;E['setAttribute']('src', 'https://getfirebug.com/' + '#startOpened');}");
	}

	@Override
	public void updatePlot(PlotData plotData) {
		dataviewer.updatePlot(plotData);
		topMenuBar.udpateTopMenu(this.latestConfig, this.plotData);
	}

	@Override
	public void resetPlot() {
		dataviewer.resetPlot();
	}

	@Override
	public void updateConfiguration(DataViewerConfiguration config) {
		dataviewer.updateConfiguration(config);
		topMenuBar.udpateTopMenu(this.latestConfig, this.plotData);
	}

}
