package de.esri.tourexplorer.ui.dialogs;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.esri.arcgisruntime.data.Feature;

import de.esri.tourexplorer.core.App;
import de.esri.tourexplorer.core.App.LocaleChangeListener;
import de.esri.tourexplorer.tools.IdentifyTool.IdentifyObject;
import de.esri.tourexplorer.tools.IdentifyTool.IdentifyResult;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;

public class IdentifyDialog extends Dialog<Object> implements LocaleChangeListener{
	private ResourceBundle res;
	private IdentifyResult identifyResult;
	private TreeView<IdentifyItem> featuresTree;
	private TableView<Attribute> attributeTable;
	private TableColumn fieldCol;
	private TableColumn valueCol;
	private Label infoLabel;
	private ButtonType closeButtonType;
	private int featureCount;
	
	public IdentifyDialog(){
		App.getInstance().addLocaleChangeListener(this);		
		Image dialogIcon = new Image("/res/images/IdentifyTool16.png");
		Stage stage = (Stage)getDialogPane().getScene().getWindow();
		stage.getIcons().add(dialogIcon);
		//stage.setOnCloseRequest(event -> stage.hide());
		initModality(Modality.NONE);
		setResizable(true);		
		
		BorderPane contentPane = new BorderPane();
		contentPane.setPadding(new Insets(5,5,0,5));
		
		SplitPane splitPane = new SplitPane();
		splitPane.setOrientation(Orientation.VERTICAL);
		BorderPane featuresPane = new BorderPane();
		featuresPane.setPrefSize(360, 200);
		IdentifyItem rootIdentifyItem = new IdentifyItem("");
		TreeItem<IdentifyItem> root = new TreeItem<IdentifyItem>(rootIdentifyItem);
		featuresTree = new TreeView<IdentifyItem>(root);
		root.setExpanded(true);
		featuresTree.setShowRoot(false);		
		featuresTree.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<IdentifyItem>>() {
			@Override
			public void changed(ObservableValue<? extends TreeItem<IdentifyItem>> observable,
				TreeItem<IdentifyItem> oldValue, TreeItem<IdentifyItem> newValue) {
				if(newValue != null){
					IdentifyItem identifyItem = newValue.getValue();
					if(identifyItem.getType() == ITEM_TYPE.FEATURE){
						IdentifyObject identifyObject = identifyItem.getIdentifyObject();
						showSelectedFeature(identifyObject);					
					}					
				}
			}
		});				
		featuresPane.setCenter(featuresTree);
		
		// attributes table
		BorderPane attributePane = new BorderPane();
		attributePane.setPrefSize(360, 200);
		attributeTable = new TableView<Attribute>();
		fieldCol = new TableColumn();
		fieldCol.setPrefWidth(160);
		fieldCol.setCellValueFactory(new PropertyValueFactory("name"));
		valueCol = new TableColumn();
		valueCol.setPrefWidth(200);
		valueCol.setCellValueFactory(new PropertyValueFactory("value"));
		attributeTable.getColumns().addAll(fieldCol, valueCol);		
		attributePane.setCenter(attributeTable);
		
		splitPane.getItems().addAll(featuresPane, attributePane);
		contentPane.setCenter(splitPane);
		
		HBox statusBar = new HBox();
		statusBar.setPadding(new Insets(5, 4, 0, 4));
		infoLabel = new Label();
		statusBar.getChildren().add(infoLabel);
		contentPane.setBottom(statusBar);		
		
		getDialogPane().setContent(contentPane);
		closeButtonType = new ButtonType("", ButtonData.CANCEL_CLOSE);
		getDialogPane().getButtonTypes().add(closeButtonType);
		
		setResultConverter(new Callback<ButtonType, Object>() {
			@Override
			public Object call(ButtonType buttonType) {
				return null;
			}
		});
		init();		
	}
	
	private void init(){
		res = ResourceBundle.getBundle("res.locale.Identify");
		setTitle(res.getString("title"));
		fieldCol.setText(res.getString("attributeName"));
		valueCol.setText(res.getString("attributeValue"));		
		Button closeButton = (Button)getDialogPane().lookupButton(closeButtonType);
		closeButton.setText(res.getString("close"));
		if(featureCount == 1){
			infoLabel.setText(featureCount + " " + res.getString("infoSingle")); 
		}else{
			infoLabel.setText(featureCount + " " + res.getString("infoMultiple"));
		}
	}
	
	public void setOwner(Window owner){
		initOwner(owner);
	}
	
	public void setIdentifyResult(IdentifyResult identifyResult){
		this.identifyResult = identifyResult;
		showIdentifiedObjects();
	}
	
	private void clear(){
		attributeTable.getItems().clear();
		featuresTree.getRoot().getChildren().clear();
		featureCount = 0;
	}
	
	private void showIdentifiedObjects(){
		if(identifyResult != null){
			clear();
			boolean isFirstObject = true;
			Map<String, List<IdentifyObject>> identifiedObjects = identifyResult.getIdentifiedObjects();
			for (Map.Entry<String, List<IdentifyObject>> entry : identifiedObjects.entrySet())
			{
				String layerName = entry.getKey();
				IdentifyItem layerItem = new IdentifyItem(layerName);
				TreeItem<IdentifyItem> layerTreeItem = new TreeItem<IdentifyItem>(layerItem);
				layerTreeItem.setExpanded(true);
				featuresTree.getRoot().getChildren().add(layerTreeItem);
				List<IdentifyObject> identifyObjects = entry.getValue();
				for(IdentifyObject identifyObject : identifyObjects) {
					featureCount++;
					IdentifyItem featureItem = new IdentifyItem(identifyObject);
					TreeItem<IdentifyItem> featureTreeItem = new TreeItem<IdentifyItem>(featureItem);
					layerTreeItem.getChildren().add(featureTreeItem);
					if(isFirstObject){
						showSelectedFeature(identifyObject);
						isFirstObject = false;
					}
				}
			}
			if(featureCount == 1){
				infoLabel.setText(featureCount + " " + res.getString("infoSingle")); 
			}else{
				infoLabel.setText(featureCount + " " + res.getString("infoMultiple"));
			}			
		}
	}
	
	private void showSelectedFeature(IdentifyObject identifyObject){
		Feature feature = identifyObject.getFeature();
		ObservableList<Attribute> data = FXCollections.observableArrayList();
		Map<String, Object> attributes = feature.getAttributes();
		for (Map.Entry<String, Object> attrEntry : attributes.entrySet()){
			String attrName = attrEntry.getKey();
			Object attrValue = attrEntry.getValue();
			String valueStr = "";
			if(attrValue != null){
				valueStr = attrValue.toString();
			}
			data.add(new Attribute(attrName, valueStr));	        
		}
		attributeTable.setItems(data);
	}
	
	@Override
	public void localeChanged() {
		init();
	}
	
	private enum ITEM_TYPE { LAYER, FEATURE }
	
	private class IdentifyItem{		
		private ITEM_TYPE type;

		private String layerName;
		private IdentifyObject identifyObject;
		
		public IdentifyItem(String layerName){
			this.layerName = layerName;
			this.type = ITEM_TYPE.LAYER;
		}
		
		public IdentifyItem(IdentifyObject identifyObject){
			this.identifyObject = identifyObject;
			this.type = ITEM_TYPE.FEATURE;
		}
		
		public ITEM_TYPE getType() {
			return type;
		}
		
		public String getLayerName() {
			return layerName;
		}
		
		public IdentifyObject getIdentifyObject() {
			return identifyObject;
		}

		@Override
		public String toString() {
			String info = "";
			if(type == ITEM_TYPE.LAYER){
				if(layerName != null){
					info = layerName;					
				}
			}else{
				if(identifyObject != null){
					info = identifyObject.toString();
				}
			}
			return info;
		}
	}
	
    public class Attribute{
        private SimpleStringProperty name;
        private SimpleStringProperty value;
        
        public Attribute(String name, String value){
            this.name = new SimpleStringProperty(name);
            this.value = new SimpleStringProperty(value);
        }

        public String getName() {
            return name.get();
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public String getValue() {
            return value.get();
        }

        public void setValue(String value) {
            this.value.set(value);
        }
    }
}
