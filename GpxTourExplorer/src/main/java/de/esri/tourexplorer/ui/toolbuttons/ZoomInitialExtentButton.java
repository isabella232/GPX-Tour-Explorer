package de.esri.tourexplorer.ui.toolbuttons;

import com.esri.arcgisruntime.mapping.view.GeoView;
import com.esri.arcgisruntime.mapping.view.SceneView;

import de.esri.tourexplorer.tools.ZoomInitialExtent;
import javafx.scene.image.Image;

public class ZoomInitialExtentButton extends CommandButton{
	private ZoomInitialExtent zoomInitialExtent;
	
	public ZoomInitialExtentButton(GeoView geoView){
		this(geoView, StandardSize.LARGE);
	}
	
	public ZoomInitialExtentButton(GeoView geoView, StandardSize size){
		super(geoView, size);
		this.size = size;
		this.smallImage = new Image(getClass().getResourceAsStream("/res/images/InitialExtent16.png"));
		this.largeImage = new Image(getClass().getResourceAsStream("/res/images/InitialExtent32.png"));
		applyImage();
		if(geoView instanceof SceneView){
			this.setDisable(true);
		}
		zoomInitialExtent = new ZoomInitialExtent(geoView);	
	}

	@Override
	public void execute() {
		zoomInitialExtent.execute();		
	}
}
