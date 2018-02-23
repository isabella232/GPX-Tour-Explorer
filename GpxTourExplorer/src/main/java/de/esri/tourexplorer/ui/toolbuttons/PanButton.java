package de.esri.tourexplorer.ui.toolbuttons;

import com.esri.arcgisruntime.mapping.view.GeoView;

import de.esri.tourexplorer.tools.PanTool;
import javafx.scene.image.Image;

public class PanButton extends ToolButton{
	private PanTool pan;

	public PanButton(GeoView geoView){
		this(geoView, StandardSize.LARGE);
	}
	
	public PanButton(GeoView geoView, StandardSize size){
		super(geoView, size);
		this.smallImage = new Image(getClass().getResourceAsStream("/res/images/Pan16.png"));
		this.largeImage = new Image(getClass().getResourceAsStream("/res/images/Pan32.png"));
		applyImage();
		pan = new PanTool(geoView);
	}

	@Override
	public void activate() {
		pan.activate();	
	}

	@Override
	public void deactivate() {
		pan.deactivate();		
	}
}
