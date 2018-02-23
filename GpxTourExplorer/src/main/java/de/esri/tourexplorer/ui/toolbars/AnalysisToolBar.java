package de.esri.tourexplorer.ui.toolbars;

import com.esri.arcgisruntime.mapping.view.GeoView;

import de.esri.tourexplorer.ui.toolbuttons.ClearSelectionButton;
import de.esri.tourexplorer.ui.toolbuttons.IdentifyButton;
import de.esri.tourexplorer.ui.toolbuttons.SelectionButton;
import de.esri.tourexplorer.ui.toolbuttons.ShowProfileButton;
import javafx.scene.control.ToolBar;

public class AnalysisToolBar extends ToolBar{

	public AnalysisToolBar(GeoView geoView){
		setId("AnalysisToolBar");
		SelectionButton selectionTool = new SelectionButton(geoView);
		ClearSelectionButton clearSelection = new ClearSelectionButton(geoView);
		IdentifyButton identify = new IdentifyButton(geoView);
		ShowProfileButton profileButton = new ShowProfileButton(geoView);
		
		this.getItems().addAll(selectionTool, clearSelection, identify, profileButton);
	}	
}
