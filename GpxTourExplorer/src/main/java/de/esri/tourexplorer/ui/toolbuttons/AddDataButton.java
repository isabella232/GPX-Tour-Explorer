package de.esri.tourexplorer.ui.toolbuttons;

import com.esri.arcgisruntime.mapping.view.GeoView;

import de.esri.tourexplorer.tools.AddDataCommand;
import javafx.scene.image.Image;

public class AddDataButton extends CommandButton{
	private AddDataCommand addData;
	
	public AddDataButton(GeoView geoView){
		this(geoView, StandardSize.LARGE);
	}	
	
	public AddDataButton(GeoView geoView, StandardSize size){
		super(geoView, size);
		this.size = size;
		this.smallImage = new Image(getClass().getResourceAsStream("/res/images/AddData16.png"));
		this.largeImage = new Image(getClass().getResourceAsStream("/res/images/AddData32.png"));
		applyImage();
		addData = new AddDataCommand(geoView);
	}

	@Override
	public void execute() {
		addData.execute();	
	}
}
