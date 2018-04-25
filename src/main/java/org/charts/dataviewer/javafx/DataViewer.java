package org.charts.dataviewer.javafx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import charts.dataviewer.api.config.DataViewerConfiguration;
import charts.dataviewer.api.data.PlotData;
import charts.dataviewer.api.data.ResetData;
import charts.dataviewer.service.ChartServiceServer;
import charts.dataviewer.service.ChartsOpenedConnections;
import charts.dataviewer.topmenu.TopMenu;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * DataViewer class provides the creation of: 1) A JavaFX GUI. 2) A Jetty
 * WebServer (if not created/running already). 3) A WebSocket Endpoint.
 * 
 * DataViewer extends JavaFX BorderPane.
 * 
 * @author irodis
 *
 */
public class DataViewer extends BorderPane {

	private final static Logger logger = LoggerFactory.getLogger(DataViewer.class.getName());

	// Unique device id
	private int udID = (int) (System.currentTimeMillis() & 0xfffffff);

	private boolean enableFireBug = false;

	private WebEngine webEngine;
	private WebView webView;

	private DataViewerConfiguration latestConfig;
	private PlotData plotData = new PlotData();

	private TopMenu topMenu;

	public DataViewer() {
		logger.debug("DataViewer with id [{}] is being created! ", udID);
		createWebsocketEndpoint();
		runServer();
		createView();
	}

	private void runServer() {
		if (!ChartServiceServer.getInstance().getServer().isRunning()) {
			logger.debug("*--------------------------------------------*");
			logger.debug("\tStarting Jetty Server..");
			Thread serverThread = new Thread(ChartServiceServer.getInstance());
			serverThread.start();
			try {
				serverThread.join();
				logger.debug("*--------------------------------------------*");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private String getUrlToLoad() {
		return ChartServiceServer.getInstance().getDataViewerURL();
	}

	private void createWebsocketEndpoint() {
		ChartServiceServer.getInstance().addEndpoint(udID);
	}

	/**
	 * Updates plot data.
	 * 
	 * @param PlotData
	 *            plotData
	 */
	public void updatePlot(PlotData plotData) {
		ChartsOpenedConnections.getInstance().sendMessage(udID, plotData.serialize());
		this.plotData = plotData;
		topMenu.udpateTopMenu(this.latestConfig, this.plotData);
	}

	/**
	 * Removes all the traces from the DataViewer.
	 */
	public void resetPlot() {
		ChartsOpenedConnections.getInstance().sendMessage(udID, new ResetData().serialize());
	}

	/**
	 * Update DataViewer Configuration.
	 * 
	 * @param DataViewerConfiguration
	 *            config
	 */
	public void updateConfiguration(DataViewerConfiguration config) {
		latestConfig = config;
		topMenu.udpateTopMenu(this.latestConfig, this.plotData);
		ChartsOpenedConnections.getInstance().sendMessage(udID, config.serialize());
	}

	/**
	 * Enabling Firebug console to debug Javascirpt in the JavaFX webview.
	 * 
	 * @param engine
	 */
	private void enableFirebug(final WebEngine engine) {
		engine.executeScript(
				"if (!document.getElementById('FirebugLite')){E = document['createElement' + 'NS'] && document.documentElement.namespaceURI;E = E ? document['createElement' + 'NS'](E, 'script') : document['createElement']('script');E['setAttribute']('id', 'FirebugLite');E['setAttribute']('src', 'https://getfirebug.com/' + 'firebug-lite.js' + '#startOpened');E['setAttribute']('FirebugLite', '4');(document['getElementsByTagName']('head')[0] || document['getElementsByTagName']('body')[0]).appendChild(E);E = new Image;E['setAttribute']('src', 'https://getfirebug.com/' + '#startOpened');}");
	}

	private void createView() {
		webView = new WebView();
		webView.setContextMenuEnabled(false);
		webEngine = webView.getEngine();
		webEngine.documentProperty().addListener(new ChangeListener<Document>() {
			@Override
			public void changed(ObservableValue<? extends Document> prop, Document oldDoc, Document newDoc) {
				logger.debug("WebView loaded dataviewer with id : [{}]", udID);
				if (enableFireBug)
					enableFirebug(webEngine);
			}
		});
		String urlToLoad = getUrlToLoad() + udID;
		logger.debug("Loading: [{}]", urlToLoad);
		webEngine.load(urlToLoad);

		setStyle("-fx-background-color: white;");
		setCenter(webView);
		setTop(topMenu = new TopMenu(plotData, latestConfig, this));
	}

	/**
	 * Get the Unique ID of the created Dataviewer.
	 */
	public int getUniqueID() {
		return udID;
	}

}
