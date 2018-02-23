package de.esri.tourexplorer.ui.toolbuttons;

import com.esri.arcgisruntime.mapping.view.GeoView;

import de.esri.tourexplorer.core.ViewpointManager;
import de.esri.tourexplorer.core.ViewpointManager.ViewpointListener;
import javafx.scene.image.Image;

public class PreviousViewpointButton extends CommandButton{
	private ViewpointManager viewpointManager;
	
	public PreviousViewpointButton(GeoView geoView){
		this(geoView, StandardSize.LARGE);
	}
	
	public PreviousViewpointButton(GeoView geoView, StandardSize size){
		super(geoView, size);
		this.size = size;
		this.smallImage = new Image(getClass().getResourceAsStream("/res/images/LeftArrow16.png"));
		this.largeImage = new Image(getClass().getResourceAsStream("/res/images/LeftArrow32.png"));
		applyImage();
		this.setDisable(true);
		viewpointManager = ViewpointManager.getInstance(geoView);
		viewpointManager.addViewpointListener(new ViewpointListener() {			
			@Override
			public void viewpointChanged(boolean hasPrevious, boolean hasNext) {				
				PreviousViewpointButton.this.setDisable(!hasPrevious);
			}
		});
	}

	@Override
	public void execute() {
		viewpointManager.goToPreviousViewpoint();		
	}
}
