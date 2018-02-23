package de.esri.tourexplorer.ui.toolbuttons;

import com.esri.arcgisruntime.mapping.view.GeoView;

import de.esri.tourexplorer.core.ViewpointManager;
import de.esri.tourexplorer.core.ViewpointManager.ViewpointListener;
import javafx.scene.image.Image;

public class NextViewpointButton extends CommandButton{
	private ViewpointManager viewpointManager;
	
	public NextViewpointButton(GeoView geoView){
		this(geoView, StandardSize.LARGE);
	}
	
	public NextViewpointButton(GeoView geoView, StandardSize size){
		super(geoView, size);
		this.size = size;
		this.smallImage = new Image(getClass().getResourceAsStream("/res/images/RightArrow16.png"));
		this.largeImage = new Image(getClass().getResourceAsStream("/res/images/RightArrow32.png"));
		applyImage();
		this.setDisable(true);
		viewpointManager = ViewpointManager.getInstance(geoView);
		viewpointManager.addViewpointListener(new ViewpointListener() {			
			@Override
			public void viewpointChanged(boolean hasPrevious, boolean hasNext) {				
				NextViewpointButton.this.setDisable(!hasNext);
			}
		});
	}

	@Override
	public void execute() {
		viewpointManager.goToNextViewpoint();		
	}
}
