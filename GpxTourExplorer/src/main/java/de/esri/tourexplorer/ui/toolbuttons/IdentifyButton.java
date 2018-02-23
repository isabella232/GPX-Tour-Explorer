package de.esri.tourexplorer.ui.toolbuttons;

import com.esri.arcgisruntime.mapping.view.GeoView;

import de.esri.tourexplorer.tools.IdentifyTool;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class IdentifyButton extends ToolButton{
	private IdentifyTool identifyTool;

	public IdentifyButton(GeoView geoView){
		this(geoView, StandardSize.LARGE);
	}
	
	public IdentifyButton(GeoView geoView, StandardSize size){
		super(geoView, size);
		this.smallImage = new Image(getClass().getResourceAsStream("/res/images/IdentifyTool16.png"));
		this.largeImage = new Image(getClass().getResourceAsStream("/res/images/IdentifyTool32.png"));
		applyImage();	
		identifyTool = new IdentifyTool(geoView);
	}
	
	public void setLineColor(Color lineColor){
		identifyTool.setLineColor(lineColor);
	}
	
	public void setLineWidth(double lineWidth){
		identifyTool.setLineWidth(lineWidth);
	}
	
	@Override
	public void activate() {		
		identifyTool.activate();	
	}

	@Override
	public void deactivate() {
		identifyTool.deactivate();	
	}
}
