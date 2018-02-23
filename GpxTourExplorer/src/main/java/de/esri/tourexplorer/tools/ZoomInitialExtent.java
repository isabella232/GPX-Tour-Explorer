package de.esri.tourexplorer.tools;

import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.view.GeoView;
import com.esri.arcgisruntime.mapping.view.MapView;

public class ZoomInitialExtent extends Command{

	public ZoomInitialExtent(GeoView geoView) {
		super(geoView);
	}

	@Override
	public void execute() {
		if(geoView instanceof MapView){
			MapView mapView = (MapView)geoView;
			ArcGISMap map = mapView.getMap();
			Geometry geometry = map.getInitialViewpoint().getTargetGeometry();
			mapView.setViewpointGeometryAsync(geometry);	
		}
	}
}
