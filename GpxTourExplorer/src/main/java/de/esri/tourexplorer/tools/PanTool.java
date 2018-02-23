package de.esri.tourexplorer.tools;

import com.esri.arcgisruntime.mapping.view.GeoView;

import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;

public class PanTool extends Tool{

	public PanTool(GeoView geoView) {
		super(geoView);		
	}

	@Override
	public void activate() {
		geoView.setEnableMousePan(true);
		geoView.setEnableTouchPan(true);
		geoView.setCursor(Cursor.HAND);
	}

	@Override
	public void deactivate() {
		geoView.setEnableMousePan(false);
		geoView.setEnableTouchPan(false);
		geoView.setCursor(Cursor.DEFAULT);
	}

	@Override
	protected void mousePressed(MouseEvent event) {
	}

	@Override
	protected void mouseReleased(MouseEvent event) {
	}

	@Override
	protected void mouseClicked(MouseEvent event) {
	}

	@Override
	protected void mouseMoved(MouseEvent event) {
	}

	@Override
	protected void mouseDragged(MouseEvent event) {
	}
}
