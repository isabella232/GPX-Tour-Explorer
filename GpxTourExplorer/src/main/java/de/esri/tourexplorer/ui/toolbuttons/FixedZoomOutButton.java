package de.esri.tourexplorer.ui.toolbuttons;

import com.esri.arcgisruntime.mapping.view.GeoView;

import de.esri.tourexplorer.tools.FixedZoomOutCommand;
import javafx.scene.image.Image;

public class FixedZoomOutButton extends CommandButton{
	private FixedZoomOutCommand fixedZoomOut;
	
	public FixedZoomOutButton(GeoView geoView){
		this(geoView, StandardSize.LARGE);
	}
	
	public FixedZoomOutButton(GeoView geoView, StandardSize size){
		super(geoView, size);
		this.size = size;
		this.smallImage = new Image(getClass().getResourceAsStream("/res/images/FixedZoomOut16.png"));
		this.largeImage = new Image(getClass().getResourceAsStream("/res/images/FixedZoomOut32.png"));
		applyImage();
		fixedZoomOut = new FixedZoomOutCommand(geoView);		
	}

	@Override
	public void execute() {
		fixedZoomOut.execute();		
	}
}
