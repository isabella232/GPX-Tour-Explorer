package de.esri.tourexplorer.ui;

import de.esri.tourexplorer.core.App;
import javafx.scene.layout.BorderPane;

public class BottomPane extends BorderPane{
	private BorderPane contentPane;
	private boolean showingContent;

	public BottomPane(){
		App.getInstance().registerComponent(BottomPane.class.getName(), this);
	}
	
	public void setContent(BorderPane contentPane){
		this.contentPane = contentPane;
	}
	
	public void showContent(boolean show){
		if(show && contentPane != null){
			setCenter(contentPane);
			showingContent = true;
		}else{
			setCenter(null);
			showingContent = false;
		}
	}
	
	public boolean isShowingContent(){
		return showingContent;
	}
}
