package de.esri.tourexplorer.tools;

import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.view.GeoView;

import de.esri.tourexplorer.core.App;

public class NewMapCommand extends Command{

	public NewMapCommand(GeoView geoView) {
		super(geoView);
	}

	@Override
	public void execute() {
		ArcGISMap map = new ArcGISMap();
		App.getInstance().getMapView().setMap(map);
	}
}
