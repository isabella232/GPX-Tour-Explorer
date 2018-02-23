package de.esri.tourexplorer.ui.toolbuttons;

import com.esri.arcgisruntime.mapping.view.GeoView;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class CommandButton extends Button{
	public enum StandardSize{ SMALL, LARGE }
	
	protected StandardSize size = StandardSize.SMALL;
	protected Image buttonImage;
	protected Image smallImage;
	protected Image largeImage;
	
	public CommandButton(GeoView geoView){
		this(geoView, StandardSize.LARGE);
	}
	
	public CommandButton(GeoView geoView, StandardSize size){
		this.size = size;
		this.getStyleClass().add("command");
		
		this.setOnAction(new EventHandler<ActionEvent>() {			
			@Override
			public void handle(ActionEvent event) {
				execute();
			}
		});
	}
	
	public StandardSize getStandardSize() {
		return size;
	}

	public void setStandardSize(StandardSize size) {
		this.size = size;
		applyImage();
	}
	
	public void setImage(Image image){
		ImageView imageView = new ImageView(image);
		this.setGraphic(imageView);
	}

	protected void applyImage(){
		if(size == StandardSize.SMALL){
			buttonImage = smallImage;
		}else if(size == StandardSize.LARGE){
			buttonImage = largeImage;
		}
		if(buttonImage != null){
			ImageView imageView = new ImageView(buttonImage);
			this.setGraphic(imageView);
		}
	}
	
	public abstract void execute();
}
