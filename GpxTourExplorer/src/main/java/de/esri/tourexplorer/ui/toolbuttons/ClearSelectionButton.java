package de.esri.tourexplorer.ui.toolbuttons;

import com.esri.arcgisruntime.mapping.view.GeoView;

import de.esri.tourexplorer.tools.ClearSelectionCommand;
import javafx.scene.image.Image;

public class ClearSelectionButton extends CommandButton{
	private ClearSelectionCommand clearSelectionCommand;
	
	public ClearSelectionButton(GeoView geoView){
		this(geoView, StandardSize.LARGE);
	}	
	
	public ClearSelectionButton(GeoView geoView, StandardSize size){
		super(geoView, size);
		this.size = size;
		this.smallImage = new Image(getClass().getResourceAsStream("/res/images/ClearSelection16.png"));
		this.largeImage = new Image(getClass().getResourceAsStream("/res/images/ClearSelection32.png"));
		applyImage();
		clearSelectionCommand = new ClearSelectionCommand(geoView);
	}

	@Override
	public void execute() {
		clearSelectionCommand.execute();
	}
}
