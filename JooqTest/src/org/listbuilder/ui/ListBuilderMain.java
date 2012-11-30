package org.listbuilder.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SceneBuilder;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ListViewBuilder;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuBarBuilder;
import javafx.scene.control.MenuBuilder;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MenuItemBuilder;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPaneBuilder;
import javafx.stage.Stage;
import javafx.util.Callback;

import org.listbuilder.model.ListBuilderModel;
import org.listbuilder.model.Unit;

public class ListBuilderMain extends Application {

	Stage stage;

	MenuItem fileMenu;
	MenuItem helpMenu;
	
	public ListView listView;

	public static void main(String[] args) {
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
				.root(BorderPaneBuilder.create().top(createMenuBar())
						.center(createListView()).build()).build();

		stage.setScene(scene);
		stage.setTitle("List Builder");
		stage.show();
		
		ListBuilderModel.instance.unitSearchByName("");
		listView.getSelectionModel().select(-1);
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
										Platform.exit();
									}
								}).build()).build(),
						MenuBuilder
								.create()
								.text("Help")
								.items(MenuItemBuilder.create().text("About")
										.build()).build()).build();
		return menuBar;
	}
	
	private Node createListView() {
		listView = ListViewBuilder.create()
				.items(ListBuilderModel.instance.allUnits)
				.editable(false)
				.build();
		
		listView.setCellFactory(new Callback<ListView<Unit>, ListCell<Unit>>() {
			@Override
			public ListCell<Unit> call(ListView<Unit> list) {
				ListCell unitCell = new UnitCell();
				unitCell.setEditable(false);
				return unitCell;
			}
		});
		
		return listView;
	}

}
