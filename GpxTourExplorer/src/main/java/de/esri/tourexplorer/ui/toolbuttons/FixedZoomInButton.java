package de.esri.tourexplorer.ui.toolbuttons;

import com.esri.arcgisruntime.mapping.view.GeoView;

import de.esri.tourexplorer.tools.FixedZoomInCommand;
import javafx.scene.image.Image;

public class FixedZoomInButton extends CommandButton{
	private FixedZoomInCommand fixedZoomIn;
	
	public FixedZoomInButton(GeoView geoView){
		this(geoView, StandardSize.LARGE);
	}	
	
	public FixedZoomInButton(GeoView geoView, StandardSize size){
		super(geoView, size);
		this.size = size;
		this.smallImage = new Image(getClass().getResourceAsStream("/res/images/FixedZoomIn16.png"));
		this.largeImage = new Image(getClass().getResourceAsStream("/res/images/FixedZoomIn32.png"));
		applyImage();
		fixedZoomIn = new FixedZoomInCommand(geoView);
	}

	@Override
	public void execute() {
		fixedZoomIn.execute();				
	}
}
