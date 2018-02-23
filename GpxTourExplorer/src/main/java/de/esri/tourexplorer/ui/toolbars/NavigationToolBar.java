package de.esri.tourexplorer.ui.toolbars;

import com.esri.arcgisruntime.mapping.view.GeoView;

import de.esri.tourexplorer.ui.toolbuttons.FixedZoomInButton;
import de.esri.tourexplorer.ui.toolbuttons.FixedZoomOutButton;
import de.esri.tourexplorer.ui.toolbuttons.NextViewpointButton;
import de.esri.tourexplorer.ui.toolbuttons.PanButton;
import de.esri.tourexplorer.ui.toolbuttons.PreviousViewpointButton;
import de.esri.tourexplorer.ui.toolbuttons.ToolManager;
import de.esri.tourexplorer.ui.toolbuttons.ZoomInButton;
import de.esri.tourexplorer.ui.toolbuttons.ZoomInitialExtentButton;
import de.esri.tourexplorer.ui.toolbuttons.ZoomOutButton;
import javafx.scene.control.ToolBar;

public class NavigationToolBar extends ToolBar{

	public NavigationToolBar(GeoView geoView){
		setId("NavigationToolBar");
		ZoomInButton zoomIn = new ZoomInButton(geoView);
		ZoomOutButton zoomOut = new ZoomOutButton(geoView);
		PanButton panTool = new PanButton(geoView);
		panTool.setSelected(true);
		panTool.activate();
		ToolManager.getInstance().setActiveTool(panTool);
		ZoomInitialExtentButton zoomInitialExtent = new ZoomInitialExtentButton(geoView);
		FixedZoomInButton fixedZoomIn = new FixedZoomInButton(geoView);
		FixedZoomOutButton fixedZoomOut = new FixedZoomOutButton(geoView);
		PreviousViewpointButton previousViewpoint = new PreviousViewpointButton(geoView);
		NextViewpointButton nextViewpoint = new NextViewpointButton(geoView);
		
		this.getItems().addAll(zoomIn, zoomOut, panTool, zoomInitialExtent, fixedZoomIn, fixedZoomOut, previousViewpoint, nextViewpoint);
	}
}
