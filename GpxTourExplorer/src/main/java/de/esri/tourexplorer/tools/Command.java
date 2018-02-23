package de.esri.tourexplorer.tools;

import com.esri.arcgisruntime.mapping.view.GeoView;

public abstract class Command {
	protected GeoView geoView;
	
	public Command(GeoView geoView){
		this.geoView = geoView;
	}
	
	public abstract void execute();
}
