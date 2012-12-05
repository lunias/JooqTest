package org.listbuilder.ui;

import static org.jooq.h2.generated.Tables.FACTION;
import static org.jooq.h2.generated.Tables.TYPE;
import static org.jooq.h2.generated.Tables.UNIT;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.When;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.GroupBuilder;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SceneBuilder;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.CheckMenuItemBuilder;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ContextMenuBuilder;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ListViewBuilder;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuBarBuilder;
import javafx.scene.control.MenuBuilder;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuButtonBuilder;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MenuItemBuilder;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ProgressIndicatorBuilder;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.RadioMenuItemBuilder;
import javafx.scene.control.SeparatorMenuItemBuilder;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBuilder;
import javafx.scene.control.TableView;
import javafx.scene.control.TableViewBuilder;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFieldBuilder;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.control.ToolBarBuilder;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TooltipBuilder;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageViewBuilder;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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
import org.listbuilder.model.UnitTableModel;

public class ListBuilderMain extends Application {

	Stage stage;

	MenuItem fileMenu;
	MenuItem helpMenu;
	
	MenuButton menuButton;
	TextField searchTextField;
	Tooltip searchTooltip;
	Button searchButton;
	Label currentItemLabel;
	ProgressIndicator progressIndicator;
	
	RadioMenuItem andRadioItem;

	public ListView<Unit> listView;
	public TableView<Unit> tableView;
	
	private static final int OK = 0;
	private static final int CANCEL = 1;

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
								.build())
						.left(createListView())
						.center(VBoxBuilder.create()
								.spacing(5)
								.children(createTableView())
								.build())
						.build())
				.build();

		
		listView.disableProperty().bind(UnitListModel.INSTANCE.queryActive);
		progressIndicator.visibleProperty()
						 .bind(UnitListModel.INSTANCE.queryActive);		
		
		stage.setScene(scene);
		stage.setTitle("List Builder");
		stage.setMinWidth(800);
		stage.setMinHeight(600);
		stage.show();
	}

	private MenuBar createMenuBar() {
		MenuBar menuBar = MenuBarBuilder
				.create()
				.menus(MenuBuilder
						.create()
						.text("_File")
						.items(MenuItemBuilder.create()
								.text("Open File...")
								.accelerator(
										KeyCombination.keyCombination("Ctrl+O"))
								.build(),
								MenuItemBuilder.create()
								.text("Save")
								.accelerator(
										KeyCombination.keyCombination("Ctrl+S"))
								.build(),
								MenuItemBuilder.create()
								.text("Save As...")								
								.build(),
								MenuItemBuilder.create()
								.text("Print")
								.accelerator(
										KeyCombination.keyCombination("Ctrl+P"))
								.build(),
								SeparatorMenuItemBuilder.create()
								.build(),
								MenuItemBuilder.create()
								.text("Exit")
								.accelerator(
										KeyCombination.keyCombination("Ctrl+Q"))
								.onAction(new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent e) {
										ModalDialog confirmDialog = new ModalDialog(ModalDialog.Type.CONFIRM);
										confirmDialog.setTitleText("Confirm Exit");
										confirmDialog.setMessageText("Are you sure you want to exit List Builder?");
										if (confirmDialog.showDialog() == OK) {
											Platform.exit();
										}
									}
								}).build()).build(),
						MenuBuilder
								.create()
								.text("_Edit")
								.items(MenuItemBuilder.create()
										.text("Add Unit")
										.build(),
										MenuItemBuilder.create()
										.text("Edit / Remove Unit")
										.build(),
										SeparatorMenuItemBuilder.create()
										.build(),
										MenuItemBuilder.create()
										.text("Export Database")
										.onAction(new EventHandler<ActionEvent>() {
											@Override
											public void handle(ActionEvent e) {
												Database.exportDatabase();
											}
										})
										.build(),
										MenuItemBuilder.create()
										.text("Reset Database")
										.onAction(new EventHandler<ActionEvent>() {
											@Override
											public void handle(ActionEvent e) {
												Database.resetDatabase();
											}
										})
										.build()
								).build(),
						MenuBuilder
								.create()
								.text("_Options")
								.items(CheckMenuItemBuilder.create()
										.text("Allow Reinforcements")
										.build(),
										CheckMenuItemBuilder.create()
										.text("Freestyle Factions")
										.build())
								.build(),
						MenuBuilder
								.create()
								.text("_Help")
								.items(MenuItemBuilder.create().text("About")
										.build()).build()).build();
		return menuBar;
	}

	private ToolBar createToolBar() {
		Region strut = new Region();
		Region spring = new Region();
		
		ToggleGroup toggleGroup = new ToggleGroup();
		
		ImageView searchImageView = ImageViewBuilder.create()
				.image(new Image(getClass().getResourceAsStream("img/search.png")))
				.build();
		
		ImageView cancelImageView = ImageViewBuilder.create()
				.image(new Image(getClass().getResourceAsStream("img/cancel.png")))
				.build();
		
		ToolBar toolBar = ToolBarBuilder.create()
				.items(menuButton = MenuButtonBuilder.create()
						.id("menuButton")
						.cursor(Cursor.HAND)
						.prefWidth(15)
						.graphic(new ImageView(
								new Image(getClass()
										.getResourceAsStream("img/menu.png")))
						)
						.items(MenuBuilder.create()
								.text("Search Columns")
								.items(CheckMenuItemBuilder.create()
										.text("Name")
										.selected(true)
										.onAction(new EventHandler<ActionEvent>() {
											@Override
											public void handle(ActionEvent e) {
												UnitListModel.INSTANCE.toggleSearchOnColumn(UNIT.NAME);
											}
										})
										.build(),
										CheckMenuItemBuilder.create()
										.text("Point Value")
										.onAction(new EventHandler<ActionEvent>() {
											@Override
											public void handle(ActionEvent e) {
												UnitListModel.INSTANCE.toggleSearchOnColumn(UNIT.POINT);
											}
										})
										.build(),
										CheckMenuItemBuilder.create()
										.text("Faction")
										.onAction(new EventHandler<ActionEvent>() {
											@Override
											public void handle(ActionEvent e) {
												UnitListModel.INSTANCE.toggleSearchOnColumn(FACTION.NAME);
											}
										})
										.build(),
										CheckMenuItemBuilder.create()
										.text("Type")
										.onAction(new EventHandler<ActionEvent>() {
											@Override
											public void handle(ActionEvent e) {
												UnitListModel.INSTANCE.toggleSearchOnColumn(TYPE.TYPE_);
											}
										})
										.build())
								.build(),
								MenuBuilder.create()
								.text("Search Operator")
								.items(andRadioItem = RadioMenuItemBuilder.create()
										.text("AND")										
										.toggleGroup(toggleGroup)
										.build(),
										RadioMenuItemBuilder.create()
										.selected(true)
										.text("OR")
										.toggleGroup(toggleGroup)
										.build())
								.build(),
								CheckMenuItemBuilder.create()
								.text("Faction Lock")
								.selected(true)
								.onAction(new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent event) {
										System.out.println("Faction lock click");
									}							
								})
								.build())
						.build(),
						HBoxBuilder.create()
						.spacing(5)
						.children(
								searchTextField = TextFieldBuilder.create()
								.prefColumnCount(15)
								.tooltip(searchTooltip = TooltipBuilder.create().build())
								.onAction(new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent e) {
										UnitListModel.INSTANCE.unitSearch(searchTextField.getText());
										searchTextField.setText("");
									}
								})
								.build(),
								searchButton = ButtonBuilder.create()
								.id("searchButton")
								.cursor(Cursor.HAND)
								.onAction(new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent e) {
										UnitListModel.INSTANCE.unitSearch(searchTextField.getText());
										searchTextField.setText("");
									}
								})
								.build()
						).build(),
						strut,
						currentItemLabel = LabelBuilder.create()
						.id("titleLabel")
						.text("List Builder")
						.build(),
						spring,
						GroupBuilder.create()
						.children(
								progressIndicator = ProgressIndicatorBuilder.create()
								.scaleX(0.4)
								.scaleY(0.4)
								.progress(-1)
								.build()								
								).build()
				)
				.build();
		
		menuButton.disableProperty().bind(UnitListModel.INSTANCE.queryActive);
		
		searchButton.graphicProperty().bind(
				new When(UnitListModel.INSTANCE.queryActive)
				.then(cancelImageView)
				.otherwise(searchImageView));
		
		searchTooltip.textProperty().bind(UnitListModel.INSTANCE.searchColumns);
		
		andRadioItem.selectedProperty().bindBidirectional(UnitListModel.INSTANCE.andOperator);
		
		strut.setPrefWidth(300);
		strut.setMinWidth(Region.USE_PREF_SIZE);
		strut.setMaxWidth(Region.USE_PREF_SIZE);
		HBox.setHgrow(spring, Priority.ALWAYS);		

		return toolBar;
	}

	private Node createListView() {
		final ContextMenu rightClickMenu = ContextMenuBuilder.create()
				.items(MenuItemBuilder.create()
						.text("Add Unit")
						.onAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								Unit selected = listView.getSelectionModel().getSelectedItem();
								if (selected != null) {
									UnitTableModel.INSTANCE.addUnit(selected);
								}
							}							
						})
						.build(),
						SeparatorMenuItemBuilder.create()
						.build(),
						MenuItemBuilder.create()
						.text("Unit Details")
						.onAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								Unit selected = listView.getSelectionModel().getSelectedItem();
								if (selected != null) {
									System.out.println("Unit Details");
								}
							}
						})
						.build())
				.build();
		
		listView = ListViewBuilder.<Unit> create()
				.items(UnitListModel.INSTANCE.getUnitList())
				.editable(false)
				.prefWidth(280)				
				.build();

		listView.setCellFactory(new Callback<ListView<Unit>, ListCell<Unit>>() {
			@Override
			public ListCell<Unit> call(ListView<Unit> list) {
				ListCell<Unit> unitCell = new UnitListCell();
				unitCell.setEditable(false);
				unitCell.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						System.out.println("Clicked");
						if (event.getButton() == MouseButton.SECONDARY
								&& !UnitListModel.INSTANCE.isEmpty()) {
							System.out.println("Not Empty");
							rightClickMenu.show(stage, event.getScreenX(),
									event.getScreenY());
						}
					}
				});
				return unitCell;
			}
		});		

		return listView;
	}
	
	private Node createTableView() {		
		final ContextMenu rightClickMenu = ContextMenuBuilder.create()				
				.items(MenuItemBuilder.create()
						.text("Remove Unit (Selected)")
						.onAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								Unit selected = tableView.getSelectionModel().getSelectedItem();
								if (selected != null) {
									UnitTableModel.INSTANCE.removeUnit(selected);
								}
							}							
						})
						.build(),
						MenuItemBuilder.create()
						.text("Remove All (Selected)")
						.onAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								Unit selected = tableView.getSelectionModel().getSelectedItem();
								if (selected != null) {
									UnitTableModel.INSTANCE.completeRemoveUnit(selected);
								}
							}
						})
						.build(),
						SeparatorMenuItemBuilder.create()
						.build(),
						MenuItemBuilder.create()
						.text("Remove All")
						.onAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								ModalDialog confirmDialog = new ModalDialog(ModalDialog.Type.CONFIRM);
								confirmDialog.setTitleText("Confirm Remove All");
								confirmDialog.setMessageText("This action will remove all units from the list. Are you sure?");
								if (confirmDialog.showDialog() == OK) {
									UnitTableModel.INSTANCE.removeAllUnits();									
								}
							}
						})
						.build(),
						SeparatorMenuItemBuilder.create()
						.build(),
						MenuItemBuilder.create()
						.text("Unit Details")
						.onAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								Unit selected = tableView.getSelectionModel().getSelectedItem();
								if (selected != null) {
									System.out.println("Unit Details for " + selected.getName());
								}
							}
						})
						.build())
				.build();		

		tableView = TableViewBuilder.<Unit> create()
				.placeholder(new Label("Add a model to start"))
				.tableMenuButtonVisible(true)
				.items(UnitTableModel.INSTANCE.getUnitList())
				.onMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						if (event.getButton() == MouseButton.SECONDARY
								&& !UnitTableModel.INSTANCE.isEmpty()) {
							rightClickMenu.show(stage, event.getScreenX(),
									event.getScreenY());
						}
					}
				})
				.build();
		
		TableColumn<Unit, Integer> quantityColumn = TableColumnBuilder.<Unit, Integer>create()
				.text("Quantity")
				.cellValueFactory(new PropertyValueFactory<Unit, Integer>("quantity"))
				.prefWidth(75)
				.build();
		
		TableColumn<Unit, String> factionColumn = TableColumnBuilder.<Unit, String>create()
				.text("Faction")
				.cellValueFactory(new PropertyValueFactory<Unit, String>("faction"))
				.prefWidth(100)
				.build();
		
		TableColumn<Unit, String> nameColumn = TableColumnBuilder.<Unit, String>create()
				.text("Name")
				.cellValueFactory(new PropertyValueFactory<Unit, String>("name"))
				.prefWidth(175)
				.build();
		
		TableColumn<Unit, Integer> pointColumn = TableColumnBuilder.<Unit, Integer>create()
				.text("Point Value")
				.cellValueFactory(new PropertyValueFactory<Unit, Integer>("pointValue"))
				.prefWidth(100)
				.build();
		
		TableColumn<Unit, String> typeColumn = TableColumnBuilder.<Unit, String>create()
				.text("Type")
				.cellValueFactory(new PropertyValueFactory<Unit, String>("type"))
				.prefWidth(75)
				.build();
		
		tableView.getColumns().addAll(quantityColumn, factionColumn, typeColumn, nameColumn, pointColumn);
		
		return tableView;
	}

}
