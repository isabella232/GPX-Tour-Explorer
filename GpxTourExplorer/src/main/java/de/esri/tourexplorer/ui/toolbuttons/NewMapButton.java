package de.esri.tourexplorer.ui.toolbuttons;

import com.esri.arcgisruntime.mapping.view.GeoView;

import de.esri.tourexplorer.tools.NewMapCommand;
import javafx.scene.image.Image;

public class NewMapButton extends CommandButton{
	private NewMapCommand newProject;

	public NewMapButton(GeoView geoView){
		this(geoView, StandardSize.LARGE);
	}	
	
	public NewMapButton(GeoView geoView, StandardSize size){
		super(geoView, size);
		this.size = size;
		this.smallImage = new Image(getClass().getResourceAsStream("/res/images/NewProject16.png"));
		this.largeImage = new Image(getClass().getResourceAsStream("/res/images/NewProject32.png"));
		applyImage();
		newProject = new NewMapCommand(geoView);
	}

	@Override
	public void execute() {
		newProject.execute();	
	}
}
