package de.esri.tourexplorer.tools;

import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.Viewpoint.Type;
import com.esri.arcgisruntime.mapping.view.GeoView;
import com.esri.arcgisruntime.mapping.view.MapView;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class ZoomOutTool extends RectangleTool{
	public ZoomOutTool(GeoView geoView) {
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
			Viewpoint viewPoint = mapView.getCurrentViewpoint(Type.BOUNDING_GEOMETRY);
			Envelope currentExtent = (Envelope)viewPoint.getTargetGeometry();
			double currentWidth = currentExtent.getWidth();
			double currentHeight = currentExtent.getHeight();
			
			Point2D screenPoint1 = new Point2D(x1, y1);
			Point2D screenPoint2 = new Point2D(x2, y2);
			Point mapPoint1 = mapView.screenToLocation(screenPoint1);
			Point mapPoint2 = mapView.screenToLocation(screenPoint2);
			double mp1x = mapPoint1.getX();
			double mp1y = mapPoint1.getY();
			double mp2x = mapPoint2.getX();
			double mp2y = mapPoint2.getY();			
			double minx = mp1x < mp2x ? mp1x : mp2x;
			double miny = mp1y < mp2y ? mp1y : mp2y;
			double maxx = mp2x > mp1x ? mp2x : mp1x;
			double maxy = mp2y > mp1y ? mp2y : mp1y;
			//System.out.println("Zoom Extent, minx: " + minx + ", miny: " + miny + ", maxx: " + maxx + ", maxy: " + maxy);
			double centerX = (maxx + minx) / 2;
			double centerY = (maxy + miny) / 2;
			//System.out.println("Zoom Extent Center, X: " + centerX + ", Y: " + centerY);
			double width = maxx - minx;
			double height = maxy - miny;
			if(width > 0 && height > 0){
				//System.out.println("Zoom Extent Width: " + width + ", Height: " + height);
				double factorX = currentWidth / width;
				double factorY = currentHeight / height;
				//System.out.println("Factor X: " + factorX + ", Factor Y: " + factorY);
				double newMinx = centerX - currentWidth * factorX / 2;
				double newMiny = centerY - currentHeight * factorY / 2;
				double newMaxx = centerX + currentWidth * factorX / 2;
				double newMaxy = centerY + currentHeight * factorY / 2;
				//System.out.println("New Extent, minx: " + newMinx + ", miny: " + newMiny + ", maxx: " + newMaxx + ", maxy: " + newMaxy);
				
				Envelope extent = new Envelope(newMinx, newMiny, newMaxx, newMaxy, mapView.getSpatialReference());
				mapView.setViewpointGeometryAsync(extent);
			}
		}
	}
}
