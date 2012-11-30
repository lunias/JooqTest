package org.listbuilder.ui;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.FlowPaneBuilder;
import javafx.scene.layout.HBoxBuilder;

import org.listbuilder.model.Unit;

public class UnitCell extends ListCell<Unit> {

	ListView listView;
	Node unitTextNode;
	Unit unit;
	
	Node createDisplayNode() {
		listView = getListView();
		
		Node displayNode = HBoxBuilder.create()
				.spacing(5)
				.children(unitTextNode = createUnitText()
				).build();
		
		return displayNode;
	}
	
	@Override
	public void updateItem(Unit item, boolean empty) {
		unit = item;
		
		super.updateItem(unit, empty);
		if (item != null) {
			setGraphic(createDisplayNode());
		}
	}
	
	Node createUnitText() {
		String unitName = unit.getName();
		int pointValue = unit.getPointValue();
		
		FlowPane flowPane = FlowPaneBuilder.create()
				.hgap(0)
				.vgap(0)
				.build();
		flowPane.setPrefWidth(listView.getScene().getWindow().getWidth() - 100);
		
		Node wordNode = new Label(unitName + " (" + pointValue + ")");		
		
		flowPane.getChildren().add(wordNode);
		return flowPane;		
	}
	
}
