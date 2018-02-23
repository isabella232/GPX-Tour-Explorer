package de.esri.tourexplorer.ui.toolbuttons;

import javafx.scene.control.ToggleGroup;

public class ToolManager {
	private static ToolManager instance;
	private ToggleGroup toolGroup;
	private ToolButton activeTool;

	private ToolManager(){	
		toolGroup = new ToggleGroup();
	}
	
	public static ToolManager getInstance(){
		if(instance == null){
			instance = new ToolManager();			
		}
		return instance;
	}
	
	public void addTool(ToolButton tool){
		tool.setToggleGroup(toolGroup);
	}
	
	public void setActiveTool(ToolButton tool){
		if(activeTool != null){
			activeTool.deactivate();			
		}
		activeTool = tool;	
	}
}
