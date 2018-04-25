package org.charts.dataviewer.javafx;

import org.charts.dataviewer.api.config.DataViewerConfiguration;
import org.charts.dataviewer.api.data.PlotData;
import org.charts.dataviewer.javafx.menubar.TopMenuBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class JavaFxDataViewer extends BorderPane {

	private static final Logger log = LoggerFactory.getLogger(JavaFxDataViewer.class);

	private WebEngine webEngine;
	private WebView webView;

	private boolean enableFireBug = false;

	private DataViewerConfiguration latestConfig;
	private PlotData plotData = new PlotData();

	private TopMenuBar topMenuBar;

	private void createView() {
		webView = new WebView();
		webView.setContextMenuEnabled(false);
		webEngine = webView.getEngine();
		webEngine.documentProperty().addListener(new ChangeListener<Document>() {
			@Override
			public void changed(ObservableValue<? extends Document> prop, Document oldDoc, Document newDoc) {
				log.debug("WebView loaded dataviewer with id : [{}]", udID);
				if (enableFireBug)
					enableFirebug(webEngine);
			}
		});
		String urlToLoad = getUrlToLoad() + udID;
		log.debug("Loading: [{}]", urlToLoad);
		webEngine.load(urlToLoad);

		setStyle("-fx-background-color: white;");
		setCenter(webView);
		setTop(topMenuBar = new TopMenu(plotData, latestConfig, this));
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

}
