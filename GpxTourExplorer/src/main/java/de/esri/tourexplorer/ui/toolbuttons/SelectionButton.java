package de.esri.tourexplorer.ui.toolbuttons;

import com.esri.arcgisruntime.mapping.view.GeoView;

import de.esri.tourexplorer.tools.SelectionTool;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class SelectionButton extends ToolButton{
	private SelectionTool selectionTool; 

	public SelectionButton(GeoView geoView){
		this(geoView, StandardSize.LARGE);
	}
	
	public SelectionButton(GeoView geoView, StandardSize size){
		super(geoView, size);
		this.smallImage = new Image(getClass().getResourceAsStream("/res/images/SelectionTool16.png"));
		this.largeImage = new Image(getClass().getResourceAsStream("/res/images/SelectionTool32.png"));
		applyImage();	
		selectionTool = new SelectionTool(geoView);
	}
	
	public void setLineColor(Color lineColor){
		selectionTool.setLineColor(lineColor);
	}
	
	public void setLineWidth(double lineWidth){
		selectionTool.setLineWidth(lineWidth);
	}
	
	@Override
	public void activate() {		
		selectionTool.activate();	
	}

	@Override
	public void deactivate() {
		selectionTool.deactivate();
	}
}
