package de.esri.tourexplorer.tools;

import com.esri.arcgisruntime.data.QueryParameters;
import com.esri.arcgisruntime.data.QueryParameters.SpatialRelationship;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.layers.FeatureLayer.SelectionMode;
import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.LayerList;
import com.esri.arcgisruntime.mapping.view.GeoView;
import com.esri.arcgisruntime.mapping.view.MapView;

import javafx.geometry.Point2D;

public class SelectionTool extends RectangleTool{

	public SelectionTool(GeoView geoView) {
		super(geoView);
		setLineWidth(1.0);
	}
	
	@Override
	protected void execute(double x1, double y1, double x2, double y2) {
		if(geoView instanceof MapView){
			MapView mapView = (MapView)geoView;
			ArcGISMap map = mapView.getMap();
			LayerList layerList = map.getOperationalLayers();
			
			// get the map extent of the selection rectangle
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
			Envelope extent = new Envelope(minx, miny, maxx, maxy, mapView.getSpatialReference());
			
			// create a spatial query with this extent
			QueryParameters queryParameters = new QueryParameters();
			queryParameters.setSpatialRelationship(SpatialRelationship.INTERSECTS);
			queryParameters.setGeometry(extent);
			
			// the selection mode
			SelectionMode selectionMode = SelectionMode.NEW;
			if(isShiftDown){
				selectionMode = SelectionMode.ADD;
			}
			if(isControlDown){
				selectionMode = SelectionMode.SUBTRACT;
			}
			
			// loop through the feature layers and select features
			for(int i = 0; i < layerList.size(); i++){
				Layer layer = layerList.get(i);
				if(layer instanceof FeatureLayer){
					FeatureLayer featureLayer = (FeatureLayer)layer;
					featureLayer.selectFeaturesAsync(queryParameters, selectionMode);
				}
			}
		}
	}
}
