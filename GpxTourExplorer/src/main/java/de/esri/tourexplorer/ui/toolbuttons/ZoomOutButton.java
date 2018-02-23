package de.esri.tourexplorer.ui.toolbuttons;

import com.esri.arcgisruntime.mapping.view.GeoView;

import de.esri.tourexplorer.tools.ZoomOutTool;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class ZoomOutButton extends ToolButton{
	private ZoomOutTool zoomOut;
	
	public ZoomOutButton(GeoView geoView){
		this(geoView, StandardSize.LARGE);
	}
	
	public ZoomOutButton(GeoView geoView, StandardSize size){
		super(geoView, size);
		this.smallImage = new Image(getClass().getResourceAsStream("/res/images/ZoomOut16.png"));
		this.largeImage = new Image(getClass().getResourceAsStream("/res/images/ZoomOut32.png"));
		applyImage();	
		zoomOut = new ZoomOutTool(geoView);
	}
	
	public void setLineColor(Color lineColor){
		zoomOut.setLineColor(lineColor);
	}
	
	public void setLineWidth(double lineWidth){
		zoomOut.setLineWidth(lineWidth);
	}
	
	@Override
	public void activate() {		
		zoomOut.activate();	
	}

	@Override
	public void deactivate() {		
		zoomOut.deactivate();
	}
}
