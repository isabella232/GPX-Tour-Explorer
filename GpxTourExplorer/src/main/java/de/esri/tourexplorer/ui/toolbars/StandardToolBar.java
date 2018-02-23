package de.esri.tourexplorer.ui.toolbars;

import com.esri.arcgisruntime.mapping.view.GeoView;

import de.esri.tourexplorer.ui.toolbuttons.AddDataButton;
import de.esri.tourexplorer.ui.toolbuttons.NewMapButton;
import javafx.scene.control.ToolBar;

public class StandardToolBar extends ToolBar{
	private NewMapButton newProjectButton;
	private AddDataButton addDataButton;

	public StandardToolBar(GeoView geoView){
		setId("StandardToolBar");
		newProjectButton = new NewMapButton(geoView);
		addDataButton = new AddDataButton(geoView);
		
		this.getItems().addAll(newProjectButton, addDataButton);
	}
	
	public void showNewProjectButton(boolean show){
		if(show){
			if(!this.getItems().contains(newProjectButton)){
				this.getItems().add(newProjectButton);
			}			
		}else{
			if(this.getItems().contains(newProjectButton)){
				this.getItems().remove(newProjectButton);
			}
		}
	}
	
	public void showAddDataButton(boolean show){
		if(show){
			if(!this.getItems().contains(addDataButton)){
				this.getItems().add(addDataButton);
			}			
		}else{
			if(this.getItems().contains(addDataButton)){
				this.getItems().remove(addDataButton);
			}
		}
	}
}
