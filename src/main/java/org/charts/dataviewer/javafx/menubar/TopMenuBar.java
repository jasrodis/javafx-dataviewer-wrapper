package org.charts.dataviewer.javafx.menubar;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.charts.dataviewer.DataViewer;
import org.charts.dataviewer.api.config.DataViewerConfiguration;
import org.charts.dataviewer.api.data.PlotData;
import org.charts.dataviewer.api.trace.GenericTrace;
import org.charts.dataviewer.javafx.utils.IconTextCellClass;
import org.charts.dataviewer.javafx.utils.ImageButton;
import org.charts.dataviewer.javafx.utils.ImageToggleButton;
import org.charts.dataviewer.javafx.utils.StaticVariables;
import org.charts.dataviewer.utils.AxisType;
import org.charts.dataviewer.utils.TraceMode;
import org.charts.dataviewer.utils.TraceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class TopMenuBar extends HBox {

	private final static Logger logger = LoggerFactory.getLogger(TopMenuBar.class);

	// Dataviewer
	private DataViewer dataviewer;
	private DataViewerConfiguration latestConfig;
	private PlotData plotData;
	// Date
	private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private Label dateLabel = new Label();

	// Table
	private TableView<PlotDataTableModel> table = new TableView<>();

	// Counters
	private int openedWindowsCtr = 0;
	private int numberOfTimesClickedCtr = 0;

	// ComboBox
	private ComboBox<String> traceNameComboBox;

	public TopMenuBar(PlotData plotData, DataViewerConfiguration latestConfig, DataViewer dataviewer) {

		initTable();
		this.dataviewer = dataviewer;
		this.plotData = plotData;
		this.latestConfig = latestConfig;

		// HBox
		HBox.setHgrow(this, Priority.ALWAYS);
		setAlignment(Pos.CENTER_RIGHT);
		setMaxHeight(5.0);
		setSpacing(0.0);
		setFillHeight(true);

		// ComboBoxes
		traceNameComboBox = new ComboBox<>();
		traceNameComboBox.setOnAction(event -> Platform.runLater(() -> updateTableValues(traceNameComboBox)));

		ComboBox<String> traceTypeComboBox = new ComboBox<>();
		traceTypeComboBox.getItems().addAll(StaticVariables.LINE, StaticVariables.BAR, StaticVariables.SCATTER,
				StaticVariables.LINEANDMARKS);
		traceTypeComboBox.setStyle("-fx-background-color: transparent;");
		traceTypeComboBox.setMaxWidth(Double.MIN_VALUE);
		traceTypeComboBox.setButtonCell(new IconTextCellClass());
		traceTypeComboBox.setTooltip(new Tooltip(StaticVariables.TRACETYPE_TP));
		traceTypeComboBox.setOnAction(event -> Platform.runLater(() -> updateTraceType(traceTypeComboBox)));
		traceTypeComboBox.getSelectionModel().select(0);
		traceTypeComboBox.setCellFactory(p -> new IconTextCellClass());

		// Spacer
		Region middleRegion = new Region();
		middleRegion.setMinSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
		HBox.setHgrow(middleRegion, Priority.ALWAYS);

		// Buttons
		ImageButton logarithmicButton = new ImageButton(StaticVariables.LOG_ICON);
		logarithmicButton.setOnAction(event -> changeToLogScale());
		logarithmicButton.setTooltip(new Tooltip(StaticVariables.LOG_TP));

		ImageButton tableButton = new ImageButton(StaticVariables.TABLE_ICON);
		tableButton.setOnAction(event -> Platform.runLater(() -> createAndShowTableWindow(traceNameComboBox)));
		tableButton.setTooltip(new Tooltip(StaticVariables.TABLE_TP));

		ImageToggleButton legendButton = new ImageToggleButton(StaticVariables.LEGEND_ICON);
		legendButton.setOnAction(this::showLegendButtonClicked);
		legendButton.setTooltip(new Tooltip(StaticVariables.LEGEND_TP));

		ImageButton exportButton = new ImageButton(StaticVariables.EXPORT_ICON);
		exportButton.setOnAction(event -> Platform.runLater(this::exportToCsv));
		exportButton.setTooltip(new Tooltip(StaticVariables.EXPORT_TP));

		dateLabel.setText(dateFormat.format(new Date()));

		// Add components.
		getChildren().addAll(logarithmicButton, tableButton, legendButton, exportButton, traceTypeComboBox,
				middleRegion, dateLabel);
	}

	private void exportToCsv() {
		try {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Export to .csv");
			fileChooser.getExtensionFilters().add(new ExtensionFilter("CSV files", "*.csv"));
			File file = fileChooser.showSaveDialog(new Stage());
			if (file == null) {
				return;
			}

			BufferedWriter br = new BufferedWriter(new FileWriter(file.getPath()));
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < plotData.getAllTraces().size(); i++) {
				sb.append("\n");
				sb.append(plotData.getAllTraces().get(i).getTraceName());
				sb.append("\n");
				for (int j = 0; j < plotData.getAllTraces().get(i).getxArray().length; j++) {
					if (plotData.getAllTraces().get(i).getxArray() == null) {
						br.close();
						return;
					}
					sb.append(plotData.getAllTraces().get(i).getxArray()[j]);
					if (plotData.getAllTraces().get(i).getyArray() != null) {
						sb.append(",");
						sb.append(plotData.getAllTraces().get(i).getyArray()[j]);
					}
					if (plotData.getAllTraces().get(i).getzArray() != null) {
						sb.append(",");
						sb.append(plotData.getAllTraces().get(i).getzArray()[j]);
					}
					sb.append("\n");
				}
			}
			br.write(sb.toString());
			br.close();
		} catch (IOException e) {
			logger.error("FileWriterException ", e);
		}
		return;
	}

	private void updateTraceType(ComboBox<String> traceTypeComboBox) {
		String selected = traceTypeComboBox.getSelectionModel().getSelectedItem();
		switch (selected) {
		case StaticVariables.LINE:
			for (int i = 0; i < plotData.getAllTraces().size(); i++) {
				plotData.getAllTraces().get(i).setTraceType(TraceType.LINE);
				plotData.getAllTraces().get(i).setTraceMode(TraceMode.LINES);
			}
			break;
		case StaticVariables.BAR:
			for (int i = 0; i < plotData.getAllTraces().size(); i++) {
				plotData.getAllTraces().get(i).setTraceType(TraceType.BAR);
			}
			break;
		case StaticVariables.SCATTER:
			for (int i = 0; i < plotData.getAllTraces().size(); i++) {
				plotData.getAllTraces().get(i).setTraceType(TraceType.SCATTER);
				plotData.getAllTraces().get(i).setTraceMode(TraceMode.MARKERS);
			}
			break;
		case StaticVariables.LINEANDMARKS:
			for (int i = 0; i < plotData.getAllTraces().size(); i++) {
				plotData.getAllTraces().get(i).setTraceType(TraceType.LINE);
				plotData.getAllTraces().get(i).setTraceMode(TraceMode.MARKERS_AND_LINES);
			}
			break;
		default:
			break;

		}
		dataviewer.updatePlot(plotData);
	}

	private void showLegendButtonClicked(ActionEvent event) {
		ImageToggleButton source = (ImageToggleButton) event.getSource();
		if (source.isSelected()) {
			latestConfig.setLegendInsidePlot(true);
		} else {
			latestConfig.setLegendInsidePlot(false);
		}
		dataviewer.updateConfiguration(latestConfig);
	}

	private void changeToLogScale() {
		// Y scale
		if (numberOfTimesClickedCtr == 0) {
			latestConfig.setyAxisType(AxisType.LOG);
			numberOfTimesClickedCtr++;
		}
		// X scale
		else if (numberOfTimesClickedCtr == 1) {
			latestConfig.setxAxisType(AxisType.LOG);
			latestConfig.setyAxisType(AxisType.DEFAULT);
			numberOfTimesClickedCtr++;
		}
		// XY scale
		else if (numberOfTimesClickedCtr == 2) {
			latestConfig.setxAxisType(AxisType.LOG);
			latestConfig.setyAxisType(AxisType.LOG);
			numberOfTimesClickedCtr++;
		}
		// Default
		else if (numberOfTimesClickedCtr == 3) {
			latestConfig.setxAxisType(AxisType.DEFAULT);
			latestConfig.setyAxisType(AxisType.DEFAULT);
			numberOfTimesClickedCtr = 0;
		}
		dataviewer.updateConfiguration(latestConfig);
	}

	private void createAndShowTableWindow(ComboBox<String> traceNameComboBox) {
		openedWindowsCtr++;
		List<String> traceNames = new ArrayList<>();
		for (int i = 0; i < plotData.getAllTraces().size(); i++) {
			traceNames.add(plotData.getAllTraces().get(i).getTraceName());
		}
		traceNameComboBox.setItems(FXCollections.observableList(traceNames));
		traceNameComboBox.getSelectionModel().select(0);

		HBox hbox = new HBox();

		traceNameComboBox.getSelectionModel().select(0);
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		hbox.getChildren().addAll(new Label("Please Select a trace :"), spacer, traceNameComboBox);

		updateTableValues(traceNameComboBox);

		Scene scene = new Scene(new Group());
		VBox vbox = new VBox();
		VBox.setVgrow(table, Priority.ALWAYS);
		vbox.setSpacing(5);
		vbox.setPadding(new Insets(10, 10, 10, 10));
		vbox.prefWidthProperty().bind(scene.widthProperty());
		vbox.prefHeightProperty().bind(scene.heightProperty());
		vbox.getChildren().addAll(hbox, table);

		((Group) scene.getRoot()).getChildren().addAll(vbox);

		Stage stage = new Stage();
		stage.setOnCloseRequest(e -> closeTableWindow());
		stage.setWidth(300);
		stage.setHeight(400);
		stage.setScene(scene);
		stage.sizeToScene();
		stage.showAndWait();
	}

	private void closeTableWindow() {
		openedWindowsCtr--;
	}

	private void updateTableValues(ComboBox<String> traceNameComboBox) {
		if (openedWindowsCtr == 0) {
			return;
		}
		int selectedIndex = traceNameComboBox.getSelectionModel().getSelectedIndex();
		if (selectedIndex == -1) {
			return;
		}
		GenericTrace<?> trace = plotData.getAllTraces().get(selectedIndex);
		List<PlotDataTableModel> dataList = new ArrayList<>();
		for (int i = 0; i < trace.getxArray().length; i++) {
			if (trace.getxArray() == null)
				return;
			if (trace.getzArray() == null && trace.getyArray() == null) {
				dataList.add(new PlotDataTableModel(trace.getxArray()[i], 0.0, 0.0));
			} else if (trace.getzArray() == null) {
				dataList.add(new PlotDataTableModel(trace.getxArray()[i], trace.getyArray()[i], 0.0));
			} else {
				dataList.add(new PlotDataTableModel(trace.getxArray()[i], trace.getyArray()[i], trace.getzArray()[i]));
			}
		}
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table.setItems(FXCollections.observableArrayList(dataList));
	}

	public void initTable() {
		table.setItems(FXCollections.observableArrayList());
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		TableColumn<PlotDataTableModel, Object> xDataCol = new TableColumn<>("X");
		xDataCol.setCellValueFactory(new PropertyValueFactory<>("xdata"));
		TableColumn<PlotDataTableModel, Object> yDataCol = new TableColumn<>("Y");
		yDataCol.setCellValueFactory(new PropertyValueFactory<>("ydata"));
		TableColumn<PlotDataTableModel, Object> zDataCol = new TableColumn<>("Z");
		zDataCol.setCellValueFactory(new PropertyValueFactory<>("zdata"));
		table.getColumns().add(xDataCol);
		table.getColumns().add(yDataCol);
		table.getColumns().add(zDataCol);
	}

	private void updateDate() {
		dateLabel.setText(dateFormat.format(new Date()));
	}

	public void udpateTopMenu(DataViewerConfiguration latestConfig, PlotData plotData) {
		this.latestConfig = latestConfig;
		this.plotData = plotData;
		Platform.runLater(this::updateDate);
		Platform.runLater(() -> updateTableValues(traceNameComboBox));
	}

}
