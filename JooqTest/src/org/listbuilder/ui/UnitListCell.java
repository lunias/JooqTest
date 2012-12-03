package org.listbuilder.ui;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TooltipBuilder;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.FlowPaneBuilder;
import javafx.scene.layout.HBoxBuilder;

import org.listbuilder.model.Unit;
import org.listbuilder.model.UnitTableModel;

public class UnitListCell extends ListCell<Unit> {

	ListView<Unit> listView;
	Node unitTextNode;
	Unit unit;
	
	Node createDisplayNode() {
		listView = getListView();
		
		Node displayNode = HBoxBuilder.create()
				.spacing(5)
				.children(unitTextNode = createUnitText())
				.onMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent mouseEvent) {
						if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
							if(mouseEvent.getClickCount() == 2){
								UnitTableModel.INSTANCE.addUnit(unit);
							}
						}
					}
				})
				.build();
		
		return displayNode;
	}
	
	@Override
	public void updateItem(Unit item, boolean empty) {
		unit = item;
		
		super.updateItem(unit, empty);
		if (item != null) {
			setGraphic(createDisplayNode());
			setTooltip(createToolTip());
		}
	}		
	
	Node createUnitText() {
		String unitName = unit.getName();
		
		FlowPane flowPane = FlowPaneBuilder.create()
				.hgap(0)
				.vgap(0)				
				.build();
		flowPane.setPrefWidth(100);
		
		Node wordNode = new Label(unitName);		
		
		flowPane.getChildren().add(wordNode);
		return flowPane;		
	}
	
	Tooltip createToolTip() {
		Tooltip toolTip = TooltipBuilder.create()
				.text("Point Value: " + unit.getPointValue())
				.build();
		return toolTip;
	}
	
}
