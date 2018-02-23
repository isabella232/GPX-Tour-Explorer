package de.esri.tourexplorer.ui.dialogs.data;

import java.io.File;
import java.util.List;
import java.util.ResourceBundle;

import com.esri.arcgisruntime.data.Geodatabase;
import com.esri.arcgisruntime.data.GeodatabaseFeatureTable;

import de.esri.tourexplorer.core.App;
import de.esri.tourexplorer.core.App.LocaleChangeListener;
import de.esri.tourexplorer.data.DataType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class FilePane extends BorderPane implements LocaleChangeListener{
	private ResourceBundle res;
	private ListView<ListItemData> fileList;
	private ListItemData selectedItem;
	private Label dirLabel;
	
	public FilePane(){
		App.getInstance().addLocaleChangeListener(this);
		
		HBox topPane = new HBox();
		topPane.setSpacing(15);
		topPane.setPadding(new Insets(10,2,10,2));
		topPane.setAlignment(Pos.CENTER_LEFT);
		Button upButton = new Button();		
		ImageView upImage = new ImageView("/res/images/UpOneLevel16.png");
		upButton.setGraphic(upImage);
		upButton.setOnAction(new EventHandler<ActionEvent>() {			
			@Override
			public void handle(ActionEvent event) {
				goUp();
			}
		});
		dirLabel = new Label();		
		topPane.getChildren().addAll(upButton, dirLabel);
		setTop(topPane);
		
		ObservableList<ListItemData> items = FXCollections.observableArrayList();
		fileList = new ListView<>(items);	
		showRoots();
		fileList.setCellFactory(listView -> new ListCell<ListItemData>() {
		    private ImageView imageView = new ImageView();
		    @Override
		    public void updateItem(ListItemData itemData, boolean empty) {
		        super.updateItem(itemData, empty);
		        if (empty) {
		            setText(null);
		            setGraphic(null);
		        } else {
		            Image image = getListIcon(itemData.getType());
		            imageView.setImage(image);
		            setText(itemData.toString());
		            setGraphic(imageView);
		        }
		    }
		});
		fileList.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent click) {
		        if (click.getClickCount() == 2) {
		        	selectedItem = fileList.getSelectionModel().getSelectedItem();
		        	if(selectedItem.getType().equals(DataType.DIRECTORY)){
		        		openDirectory(selectedItem);
		        	}else if(selectedItem.getType().equals(DataType.GEODATABASE)){
		        		openGeodatabase(selectedItem);
		        	}
		        }
		    }
		});
		setCenter(fileList);				
	}

	public void init(){
		res = ResourceBundle.getBundle("res.locale.AddData");
		//addServerPane.setText(res.getString("addGisServer"));
		//addButton.setText(res.getString("add"));
	}
	
	private Image getListIcon(DataType dataType){
		Image icon = null;
		if(dataType.equals(DataType.DIRECTORY)){
			icon =  new Image("/res/images/Folder16.png");
		}else if(dataType.equals(DataType.TPK)){
			icon =  new Image("/res/images/LayerBasemap16.png");
		}else if(dataType.equals(DataType.GEODATABASE)){
			icon =  new Image("/res/images/Geodatabase16.png");
		}else if(dataType.equals(DataType.FEATURECLASS)){
			icon =  new Image("/res/images/Layer16.png");
		}else if(dataType.equals(DataType.GPX)){
			icon =  new Image("/res/images/GpxFile.png");
		}			
		return icon;
	}
	
	private void showRoots(){
		dirLabel.setText("");
		dirLabel.setTooltip(new Tooltip());
		fileList.getItems().clear();
		File[] roots = File.listRoots();
		for(int i = 0; i < roots.length; i++){
			String rootDir = roots[i].getAbsolutePath();
			ListItemData data = new ListItemData();
			data.setName(rootDir);
			data.setPath(rootDir);
			data.setType(DataType.DIRECTORY);
			fileList.getItems().add(data);
		}
	}
	
	private void openDirectory(ListItemData item){
		String path = item.getPath();
		dirLabel.setText(path);
		dirLabel.setTooltip(new Tooltip(path));
		File dir = new File(path);
		if(dir.exists()){			
			fileList.getItems().clear();
			File[] files =  dir.listFiles();
			if(files != null){
				for(int i = 0; i < files.length; i++){
					File file = files[i];
					if(file.isDirectory()){
						if(file.canRead() && file.listFiles() != null){
							String dirName = file.getName();
							String dirPath = file.getAbsolutePath();
							ListItemData newItem = new ListItemData();
							newItem.setName(dirName);			
							newItem.setPath(dirPath);
							newItem.setType(DataType.DIRECTORY);
							fileList.getItems().add(newItem);
						}
					}else{
						String fileName = file.getName();
						String ext = null;
						int idx = fileName.lastIndexOf('.');
						if (idx > 0) {
							ext = fileName.substring(idx+1);
						}
						if(ext != null){
							ListItemData newItem = new ListItemData();
							newItem.setName(fileName);			
							newItem.setPath(file.getAbsolutePath());
							if(ext.equalsIgnoreCase("tpk")){
								newItem.setType(DataType.TPK);
								fileList.getItems().add(newItem);
							}else if(ext.equalsIgnoreCase("geodatabase")){
								newItem.setType(DataType.GEODATABASE);
								fileList.getItems().add(newItem);
							}else if(ext.equalsIgnoreCase("gpx")){
								newItem.setType(DataType.GPX);
								fileList.getItems().add(newItem);
							}
						}
					}	
				}
			}
		}						
	}
	
	private void openGeodatabase(ListItemData item){
		String path = item.getPath();
		Geodatabase gdb = new Geodatabase(path);
		gdb.addDoneLoadingListener(() -> {
			fileList.getItems().clear();
			List<GeodatabaseFeatureTable> tables = gdb.getGeodatabaseFeatureTables();
			for(GeodatabaseFeatureTable table : tables){				
				String name = table.getTableName();
				ListItemData newItem = new ListItemData();
				newItem.setName(name);			
				newItem.setPath(path);
				newItem.setType(DataType.FEATURECLASS);
//				if(type.equals(GeometryType.POINT)){
//					newItem.setType(DataType.POINT_FC);
//				}else if(type.equals(GeometryType.POLYLINE)){
//					newItem.setType(DataType.POLYLINE_FC);
//				}else if(type.equals(GeometryType.POLYGON)){
//					newItem.setType(DataType.POLYGON_FC);
//				}
				fileList.getItems().add(newItem);
			}
		});
		gdb.loadAsync();
	}	
	
	private void goUp(){
		File currentDir = new File(selectedItem.getPath());
		File parentDir = currentDir.getParentFile();
		if(parentDir != null){
			ListItemData parentItem = new ListItemData();		
			parentItem.setName(parentDir.getName());			
			parentItem.setPath(parentDir.getAbsolutePath());
			parentItem.setType(DataType.DIRECTORY);
			selectedItem = parentItem;
			openDirectory(parentItem);			
		}else{
			showRoots();
		}
	}
	
	@Override
	public void localeChanged() {
		init();		
	}
	
	public boolean hasSelection(){
		boolean hasSelection = false;
		if(fileList.getSelectionModel().getSelectedItems().size() > 0){
			hasSelection = true;
		}		
		return hasSelection;
	}
	
	public String getSelectedName(){
		String name = "";
		ListItemData selectedItem = fileList.getSelectionModel().getSelectedItem();
		if(selectedItem != null){
			name = selectedItem.getName();
		}
		return name;
	}
	
	public String getSelectedPath(){
		ListItemData selectedItem = fileList.getSelectionModel().getSelectedItem();
		return selectedItem.getPath();
	}
	
	public DataType getSelectedType(){
		ListItemData selectedItem = fileList.getSelectionModel().getSelectedItem();
		return selectedItem.getType();
	}
	
	private class ListItemData{
		private String name;
		private String path;
		private DataType type;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getPath() {
			return path;
		}
		public void setPath(String path) {
			this.path = path;
		}						
		public DataType getType() {
			return type;
		}
		public void setType(DataType type) {
			this.type = type;
		}
		public String toString(){
			return name;
		}
	}
}
