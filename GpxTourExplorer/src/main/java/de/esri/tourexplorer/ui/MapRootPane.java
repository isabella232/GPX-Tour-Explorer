package de.esri.tourexplorer.ui;

import com.esri.arcgisruntime.mapping.view.GeoView;

import javafx.scene.layout.BorderPane;

public class MapRootPane extends BorderPane{
	
	public MapRootPane(){		
	}
	
	public void addGeoView(GeoView geoView){
		super.setCenter(geoView);
	}
}
