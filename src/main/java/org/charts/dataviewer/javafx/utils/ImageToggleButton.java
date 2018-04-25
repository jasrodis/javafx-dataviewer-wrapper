package org.charts.dataviewer.javafx.utils;

import javafx.event.EventHandler;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class ImageToggleButton extends ToggleButton {

	private final String STYLE_NORMAL = "-fx-background-color: transparent; -fx-padding: 5, 5, 5, 5;";
	private final String STYLE_PRESSED = "-fx-background-color: transparent; -fx-padding: 6 4 4 6;";

	private boolean toggle = false;

	public ImageToggleButton(String imageurl) {
		setGraphic(new ImageView(new Image(getClass().getResourceAsStream(imageurl))));
		setStyle(STYLE_NORMAL);

		setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (toggle) {
					setStyle(STYLE_NORMAL);
					toggle = false;
				} else {
					setStyle(STYLE_PRESSED);
					toggle = true;
				}
			}
		});
	}
}