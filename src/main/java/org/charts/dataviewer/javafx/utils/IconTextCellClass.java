package org.charts.dataviewer.javafx.utils;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class IconTextCellClass extends ListCell<String> {

	private Label label;

	@Override
	protected void updateItem(String item, boolean empty) {
		super.updateItem(item, empty);
		if (item == null || empty) {
			setItem(null);
			setGraphic(null);
		} else {
			setText(item);
			ImageView image = getImageView(item);
			label = new Label("", image);
			setGraphic(label);
		}
	}

	private ImageView getImageView(String imageName) {
		ImageView imageView = null;
		ImageView lineTraceImage = new ImageView((new Image(getClass().getResourceAsStream(StaticVariables.LINETRACE_ICON))));
		ImageView barTraceImage = new ImageView((new Image(getClass().getResourceAsStream(StaticVariables.BARTRACE_ICON))));
		ImageView scatterTraceImage = new ImageView((new Image(getClass().getResourceAsStream(StaticVariables.SCATTERTRACE_ICON))));
		ImageView lineAndMarksTraceImage = new ImageView((new Image(getClass().getResourceAsStream(StaticVariables.LINEANDMARKSTRACE_ICON))));

		switch (imageName) {
		case StaticVariables.LINE:
			imageView = lineTraceImage;
			break;
		case StaticVariables.BAR:
			imageView = barTraceImage;
			break;
		case StaticVariables.SCATTER:
			imageView = scatterTraceImage;
			break;
		case StaticVariables.LINEANDMARKS:
			imageView = lineAndMarksTraceImage;
			break;
		default:
			imageName = null;
		}
		return imageView;
	}
};