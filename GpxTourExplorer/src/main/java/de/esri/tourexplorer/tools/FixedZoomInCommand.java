package de.esri.tourexplorer.tools;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.GeoView;

import de.esri.tourexplorer.core.ViewpointManager;

public class FixedZoomInCommand extends Command{
	private double factor = 0.8;

	public FixedZoomInCommand(GeoView geoView) {
		super(geoView);
	}
	
	@Override
	public void execute() {
		Viewpoint viewpoint = geoView.getCurrentViewpoint(Viewpoint.Type.CENTER_AND_SCALE);
		double scale = viewpoint.getTargetScale();
		double newScale = scale * factor;
		Point center = (Point)viewpoint.getTargetGeometry();
		Viewpoint newViewpoint = new Viewpoint(center, newScale);
		geoView.setViewpoint(newViewpoint);
		ViewpointManager.getInstance(geoView).addViewpoint(newViewpoint);
	}
}
