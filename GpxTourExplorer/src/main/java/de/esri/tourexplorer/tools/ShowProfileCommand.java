package de.esri.tourexplorer.tools;

import com.esri.arcgisruntime.mapping.view.GeoView;

import de.esri.tourexplorer.core.App;
import de.esri.tourexplorer.ui.BottomPane;

public class ShowProfileCommand extends Command{

	public ShowProfileCommand(GeoView geoView) {
		super(geoView);
	}
	
	@Override
	public void execute() {
		BottomPane profilePane = (BottomPane)App.getInstance().getComponent(BottomPane.class.getName());
		profilePane.showContent(!profilePane.isShowingContent());						
	}
}
