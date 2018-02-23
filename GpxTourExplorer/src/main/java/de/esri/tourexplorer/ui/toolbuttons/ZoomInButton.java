package de.esri.tourexplorer.ui.toolbuttons;

import com.esri.arcgisruntime.mapping.view.GeoView;

import de.esri.tourexplorer.tools.ZoomInTool;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class ZoomInButton extends ToolButton{
	private ZoomInTool zoomIn; 
	
	public ZoomInButton(GeoView geoView){
		this(geoView, StandardSize.LARGE);
	}
	
	public ZoomInButton(GeoView geoView, StandardSize size){
		super(geoView, size);
		this.smallImage = new Image(getClass().getResourceAsStream("/res/images/ZoomIn16.png"));
		this.largeImage = new Image(getClass().getResourceAsStream("/res/images/ZoomIn32.png"));
		applyImage();		
		zoomIn = new ZoomInTool(geoView);
	}
	
	public void setLineColor(Color lineColor){
		zoomIn.setLineColor(lineColor);
	}
	
	public void setLineWidth(double lineWidth){
		zoomIn.setLineWidth(lineWidth);
	}
	
	@Override
	public void activate() {
		zoomIn.activate();	
	}

	@Override
	public void deactivate() {
		zoomIn.deactivate();
	}
}
