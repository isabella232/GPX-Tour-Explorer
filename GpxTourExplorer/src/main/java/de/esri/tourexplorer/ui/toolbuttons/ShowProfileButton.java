package de.esri.tourexplorer.ui.toolbuttons;

import com.esri.arcgisruntime.mapping.view.GeoView;

import de.esri.tourexplorer.tools.ShowProfileCommand;
import javafx.scene.image.Image;

public class ShowProfileButton extends CommandButton{
	private ShowProfileCommand showProfile;

	public ShowProfileButton(GeoView geoView){
		this(geoView, StandardSize.LARGE);
	}
	
	public ShowProfileButton(GeoView geoView, StandardSize size){
		super(geoView, size);
		this.size = size;
		this.smallImage = new Image(getClass().getResourceAsStream("/res/images/Profile16.png"));
		this.largeImage = new Image(getClass().getResourceAsStream("/res/images/Profile32.png"));
		applyImage();
		showProfile = new ShowProfileCommand(geoView);
	}	
	
	@Override
	public void execute() {
		showProfile.execute();	
	}
}
