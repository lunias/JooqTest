package org.listbuilder.ui;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ModalDialog extends Stage {

	private Stage stage;
	private Scene scene;
	private Type type;
	
	private BorderPane pane = new BorderPane();
	private ImageView iconImageView = new ImageView();
	private Label message = new Label();
	private HBox buttonBox = new HBox(10);
	
	private int buttonCount = 0;
	private int buttonSelected = -1;
	private int buttonCancel = -1;
	private List<String> buttonLabels;	
	
	private final Logger LOG = LoggerFactory.getLogger(ModalDialog.class);
	
	public enum Type {
		CONFIRM, INFO, QUESTION
	}
	
	public ModalDialog() {
		initDialog(Type.INFO);
	}
	
	public ModalDialog(Type t) {
		initDialog(t);
	}

	private void initDialog(Type t) {
		stage = new Stage();		
		type = t;
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setMaxWidth(Screen.getPrimary().getVisualBounds().getWidth()/2);
	}
	
	public int showDialog() {
		populateStage();
		
		if (buttonCount == 0) {
			addOKButton();
		}
		
		stage.setResizable(false);
		stage.sizeToScene();
		stage.centerOnScreen();
		stage.showAndWait();
		
		return (buttonSelected == -1 ? buttonCancel : buttonSelected);
	}
	
	private void populateStage() {
		String icon;
		switch(type) {
		case CONFIRM:
			icon = "img/option.png";
			addOKCancelButtons();
			break;
		default:
			icon = "img/search.png";
		}
		
		try {
			Image imgIcon = new Image(getClass().getResourceAsStream(icon));
			//iconImageView.setPreserveRatio(true);
			//iconImageView.setFitHeight(48);
			iconImageView.setImage(imgIcon);			
		} catch (Exception e) {
			LOG.error("Could not load icon file " + icon, e);
		}

        BorderPane.setAlignment(iconImageView, Pos.CENTER);
        BorderPane.setMargin(iconImageView, new Insets(5,5,5,5));
        pane.setLeft(iconImageView);
        
        BorderPane.setAlignment(message, Pos.CENTER);
        BorderPane.setMargin(message, new Insets(5,5,5,5));
        pane.setCenter(message);
        
        scene = new Scene(pane);
        stage.setScene(scene);
	}
	
	private void addOKButton() {
		List<String> labels = new ArrayList<>(1);
		labels.add("OK");
		
		addButtons(labels, 0, 0);
	}
	
	private void addOKCancelButtons() {
		List<String> labels = new ArrayList<>(2);
        labels.add("OK");
        labels.add("Cancel");
        
        addButtons(labels);
	}
	
	public void addButtons(List<String> labels) {
		addButtons(labels, -1, -1);
    }
	
	public void addButtons(List<String> labels, int defaultBtn, int cancelBtn) {
		buttonLabels = labels;
		
		for (int i=0; i<labels.size(); i++) {
            final Button btn = new Button(labels.get(i));
            
            btn.setDefaultButton(i==defaultBtn);
            btn.setCancelButton(i==cancelBtn);

            if ( i == defaultBtn ) {
                Platform.runLater( new Runnable() {
                    @Override
                    public void run() {
                        btn.requestFocus();
                    }
                } );
            }
            
            buttonCancel = cancelBtn;
            
            btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
                public void handle(ActionEvent evt) {
                    buttonSelected = buttonLabels.indexOf(((Button) evt.getSource()).getText());
                    stage.close();
                }
            });
            buttonBox.getChildren().add(btn);
        }
        
        buttonBox.setAlignment(Pos.CENTER);
        
        BorderPane.setAlignment(buttonBox, Pos.CENTER);
        BorderPane.setMargin(buttonBox, new Insets(5,5,5,5));
        pane.setBottom(buttonBox);
        buttonCount = labels.size();
    }
	
	public void setTitleText(String title) {
		stage.setTitle(title);
	}
	
	public void setMessageText(String message) {
		this.message.setText(message);
	}
	
}
