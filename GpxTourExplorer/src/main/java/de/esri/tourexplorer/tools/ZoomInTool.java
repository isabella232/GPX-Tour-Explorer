package de.esri.tourexplorer.tools;

import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.view.GeoView;
import com.esri.arcgisruntime.mapping.view.MapView;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class ZoomInTool extends RectangleTool{

	public ZoomInTool(GeoView geoView) {
		super(geoView);
		setLineColor(Color.RED);
		setLineWidth(1.5);
	}

	@Override
	protected void execute(double x1, double y1, double x2, double y2) {
		//System.out.println("Zoom In, X1: " + x1 + ", Y1: " + y1 + ", X2: " + x2 + ", Y2: " + y2);
		// ToDo: have to figure out how zoom in / zoom out works with SceneView.
		if(geoView instanceof MapView){
			MapView mapView = (MapView)geoView;
			Point2D screenPoint1 = new Point2D(x1, y1);
			Point2D screenPoint2 = new Point2D(x2, y2);
			Point mapPoint1 = mapView.screenToLocation(screenPoint1);
			Point mapPoint2 = mapView.screenToLocation(screenPoint2);
			Envelope extent = new Envelope(mapPoint1, mapPoint2);
			mapView.setViewpointGeometryAsync(extent);
		}
	}
}
