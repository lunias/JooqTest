package org.listbuilder.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.When;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.GroupBuilder;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SceneBuilder;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ListViewBuilder;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuBarBuilder;
import javafx.scene.control.MenuBuilder;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MenuItemBuilder;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ProgressIndicatorBuilder;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFieldBuilder;
import javafx.scene.control.ToolBar;
import javafx.scene.control.ToolBarBuilder;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageViewBuilder;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPaneBuilder;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBoxBuilder;
import javafx.stage.Stage;
import javafx.util.Callback;

import org.listbuilder.common.Database;
import org.listbuilder.model.Unit;
import org.listbuilder.model.UnitListModel;

public class ListBuilderMain extends Application {

	Stage stage;

	MenuItem fileMenu;
	MenuItem helpMenu;
	
	Button backButton;
	TextField searchTextField;
	Button searchButton;
	Label currentItemLabel;
	ProgressIndicator progressIndicator;
	

	public ListView<Unit> listView;

	public static void main(String[] args) {
		if (!Database.isInitialized()) {
			Database.resetDatabase();
		}
		
		Application.launch(args);
	}

	@Override
	public void start(final Stage primaryStage) throws Exception {
		stage = primaryStage;

		Scene scene = SceneBuilder
				.create()
				.width(1000)
				.height(660)
				.stylesheets("org/listbuilder/ui/listbuilder.css")
				.root(BorderPaneBuilder
						.create()
						.top(VBoxBuilder.create()
								.children(createMenuBar(), createToolBar())
								.build()).left(createListView()).build())
				.build();

		
		listView.disableProperty().bind(UnitListModel.INSTANCE.queryActive);
		progressIndicator.visibleProperty()
						 .bind(UnitListModel.INSTANCE.queryActive);
		stage.setScene(scene);
		stage.setTitle("List Builder");
		stage.show();
	}

	private MenuBar createMenuBar() {
		MenuBar menuBar = MenuBarBuilder
				.create()
				.menus(MenuBuilder
						.create()
						.text("File")
						.items(MenuItemBuilder
								.create()
								.text("Exit")
								.accelerator(
										KeyCombination.keyCombination("Ctrl+Q"))
								.onAction(new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent e) {
										Database.dispose();
										Platform.exit();
									}
								}).build()).build(),
						MenuBuilder
								.create()
								.text("Options")
								.items(MenuItemBuilder.create().text("Sorting")
										.build()).build(),
						MenuBuilder
								.create()
								.text("Help")
								.items(MenuItemBuilder.create().text("About")
										.build()).build()).build();
		return menuBar;
	}

	private ToolBar createToolBar() {
		Region strut = new Region();
		Region spring = new Region();
		
		ImageView searchImageView = ImageViewBuilder.create()
				.image(new Image(getClass().getResourceAsStream("img/search.png")))
				.build();
		
		ImageView cancelImageView = ImageViewBuilder.create()
				.image(new Image(getClass().getResourceAsStream("img/cancel.png")))
				.build();
		
		ToolBar toolBar = ToolBarBuilder.create()
				.items(
						backButton = ButtonBuilder.create().id("backButton")
						.graphic(new ImageView(
								new Image(getClass()
										.getResourceAsStream("img/back.png")))
						)
						.onAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent e) {
								// TODO make this do something
								System.out.println("CLICKED BACK");
							}
						})
						.build(),
						HBoxBuilder.create()
						.spacing(5)
						.children(
								searchTextField = TextFieldBuilder.create()
								.prefColumnCount(15)
								.onAction(new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent e) {
										System.out.println("ACTION IN SEARCH FIELD");
										UnitListModel.INSTANCE.unitSearchByName(searchTextField.getText());
										searchTextField.setText("");
									}
								})
								.build(),
								searchButton = ButtonBuilder.create()
								.id("searchButton")
								.onAction(new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent e) {
										System.out.println("CLICKED SEARCH");
										UnitListModel.INSTANCE.unitSearchByName(searchTextField.getText());
										searchTextField.setText("");
									}
								})
								.build()
						).build(),
						strut,
						currentItemLabel = LabelBuilder.create()
						.id("titleLabel")
						.build(),
						spring,
						GroupBuilder.create()
						.children(
								progressIndicator = ProgressIndicatorBuilder.create()
								.scaleX(0.7)
								.scaleY(0.7)
								.progress(-1)
								.build()								
								).build()
				)
				.build();
		
		backButton.disableProperty().bind(UnitListModel.INSTANCE.queryActive);
		
		searchButton.graphicProperty().bind(
				new When(UnitListModel.INSTANCE.queryActive)
				.then(cancelImageView)
				.otherwise(searchImageView));
		
		
		
		strut.setPrefWidth(200);
		strut.setMinWidth(Region.USE_PREF_SIZE);
		strut.setMaxWidth(Region.USE_PREF_SIZE);
		HBox.setHgrow(spring, Priority.ALWAYS);

		return toolBar;
	}

	private Node createListView() {
		listView = ListViewBuilder.<Unit> create()
				.items(UnitListModel.INSTANCE.unitList).editable(false).build();

		listView.setCellFactory(new Callback<ListView<Unit>, ListCell<Unit>>() {
			@Override
			public ListCell<Unit> call(ListView<Unit> list) {
				ListCell<Unit> unitCell = new UnitListCell();
				unitCell.setEditable(false);
				return unitCell;
			}
		});

		return listView;
	}

}
