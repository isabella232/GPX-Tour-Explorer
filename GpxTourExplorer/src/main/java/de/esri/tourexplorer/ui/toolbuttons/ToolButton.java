package de.esri.tourexplorer.ui.toolbuttons;

import com.esri.arcgisruntime.mapping.view.GeoView;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class ToolButton extends ToggleButton{
	public enum StandardSize{ SMALL, LARGE }
	
	protected GeoView geoView;	
	protected StandardSize size = StandardSize.SMALL;
	protected Image buttonImage;
	protected Image smallImage;
	protected Image largeImage;

	public ToolButton(GeoView geoView){
		this(geoView, StandardSize.LARGE);
	}
	
	public ToolButton(GeoView geoView, StandardSize size){
		this.geoView = geoView;
		this.size = size;		
		this.getStyleClass().add("tool");
		ToolManager.getInstance().addTool(this);
		this.setOnAction(new EventHandler<ActionEvent>() {			
			@Override
			public void handle(ActionEvent event) {
				if(ToolButton.this.isSelected()){
					ToolManager.getInstance().setActiveTool(ToolButton.this);
					activate();
				}else{
					deactivate();
				}
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
	
	protected abstract void activate();
	
	protected abstract void deactivate();
}
