package de.esri.tourexplorer.tools;

import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.LayerList;
import com.esri.arcgisruntime.mapping.view.GeoView;
import com.esri.arcgisruntime.mapping.view.MapView;

public class ClearSelectionCommand extends Command{

	public ClearSelectionCommand(GeoView geoView) {
		super(geoView);
	}

	@Override
	public void execute() {
		if(geoView instanceof MapView){
			MapView mapView = (MapView)geoView;
			ArcGISMap map = mapView.getMap();		
			LayerList layerList = map.getOperationalLayers();
			for(int i = 0; i < layerList.size(); i++){
				Layer layer = layerList.get(i);
				if(layer instanceof FeatureLayer){
					FeatureLayer featureLayer = (FeatureLayer)layer;
					featureLayer.clearSelection();
				}
			}
		}
	}
}
