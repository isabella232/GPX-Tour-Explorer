package de.esri.tourexplorer.ui.dialogs.data;

import java.util.ResourceBundle;

import de.esri.tourexplorer.core.App;
import de.esri.tourexplorer.core.App.LocaleChangeListener;
import de.esri.tourexplorer.data.DataInfo;
import de.esri.tourexplorer.data.DataType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.stage.Window;
import javafx.util.Callback;

public class AddDataDialog extends Dialog<Object> implements LocaleChangeListener{
	private ResourceBundle res;
	private Tab basemapTab;
	private Tab fileTab;
	private ButtonType addButtonType;
	private ButtonType cancelButtonType;
	
	public AddDataDialog(){
		App.getInstance().addLocaleChangeListener(this);
		
		TabPane tabPane = new TabPane();
		basemapTab = new Tab();
	    Image basemapImage = new Image("/res/images/Basemap.png");
	    ImageView basemapImageView = new ImageView(basemapImage);	    
	    basemapTab.setGraphic(basemapImageView);
	    basemapTab.setClosable(false);
	    BasemapGallery basemapGallery = new BasemapGallery();
	    basemapTab.setContent(basemapGallery);
	    tabPane.getTabs().add(basemapTab);		
		fileTab = new Tab();
		Image folderImage = new Image("/res/images/OpenFile.png");
		ImageView folderImageView = new ImageView(folderImage);		
		fileTab.setGraphic(folderImageView);
		fileTab.setClosable(false);
		FilePane filePane = new FilePane();
		fileTab.setContent(filePane);
		tabPane.getTabs().add(fileTab);
		
		getDialogPane().setContent(tabPane);
		
		setResultConverter(new Callback<ButtonType, Object>() {
			@Override
			public Object call(ButtonType buttonType) {
				Object retVal = null;
				if(buttonType == addButtonType){
					DataInfo dataInfo = new DataInfo();
					if(tabPane.getSelectionModel().getSelectedItem().equals(basemapTab)){
						dataInfo.setDataType(DataType.BASEMAP);
						dataInfo.setData(basemapGallery.getSelectedBasemap());
					}else if(tabPane.getSelectionModel().getSelectedItem().equals(fileTab)){
						if(filePane.hasSelection()){
							String name = filePane.getSelectedName();
							String path = filePane.getSelectedPath();
							DataType type = filePane.getSelectedType();
							dataInfo.setDataType(type);
							dataInfo.setName(name);
							dataInfo.setPath(path);
						}
					}
					retVal = dataInfo;
				}				
				return retVal;
			}
		});
		init();	
	}
	
	private void init(){
		res = ResourceBundle.getBundle("res.locale.AddData");
		setTitle(res.getString("title"));
		basemapTab.setText(res.getString("basemapTab"));
		fileTab.setText(res.getString("fileTab"));		
		getDialogPane().getButtonTypes().removeAll(addButtonType, cancelButtonType);
		addButtonType = new ButtonType(res.getString("add"), ButtonData.OK_DONE);
		cancelButtonType = new ButtonType(res.getString("cancel"), ButtonData.CANCEL_CLOSE);
		getDialogPane().getButtonTypes().addAll(addButtonType, cancelButtonType);
	}
	
	public void setOwner(Window owner){
		initOwner(owner);
	}
	
	@Override
	public void localeChanged() {
		init();	
	}
}
