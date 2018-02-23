package de.esri.tourexplorer.ui.dialogs.data;

import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.portal.Portal;
import com.esri.arcgisruntime.portal.PortalItem;

import javafx.geometry.Insets;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class BasemapGallery extends ScrollPane{
	private ToggleGroup basemapToggle = new ToggleGroup();
	private ToggleButton imageryButton = new ToggleButton();
	private ToggleButton imageryLabelsButton = new ToggleButton();
	private ToggleButton streetsButton = new ToggleButton();
	private ToggleButton topoButton = new ToggleButton();
	private ToggleButton darkGrayButton = new ToggleButton();
	private ToggleButton lightGrayButton = new ToggleButton();
	private ToggleButton natgeoButton = new ToggleButton();
	private ToggleButton oceansButton = new ToggleButton();
	private ToggleButton terrainButton = new ToggleButton();
	private ToggleButton osmButton = new ToggleButton();
	
	public BasemapGallery(){
		setPrefSize(544, 410);
		GridPane gridPane = new GridPane();
		gridPane.setHgap(10.0);
		gridPane.setVgap(10.0);
		gridPane.setPadding(new Insets(10,5,10,5));
		
		Image imageryImage = new Image("/res/images/AGO_Imagery.jpg", 150, 100, true, false);
		Image imageryLabelsImage = new Image("/res/images/AGO_Imagery_Labels.jpg", 150, 100, true, false);
		Image streetsImage = new Image("/res/images/AGO_World_Street_Map.jpg", 150, 100, true, false);
		Image topoImage = new Image("/res/images/AGO_Topo_Map.jpg", 150, 100, true, false);
		Image darkGrayImage = new Image("/res/images/AGO_Dark_Gray.png", 150, 100, true, false);
		Image lightGrayImage = new Image("/res/images/AGO_Light_Gray.jpg", 150, 100, true, false);
		Image natGeoImage = new Image("/res/images/AGO_Nat_Geo.jpg", 150, 100, true, false);
		Image oceansImage = new Image("/res/images/AGO_Oceans.jpg", 150, 100, true, false);
		Image terrainImage = new Image("/res/images/AGO_Terrain_Labels.jpg", 150, 100, true, false);
		Image osmImage = new Image("/res/images/AGO_OSM.jpg", 150, 100, true, false);
		
		imageryButton.setText("Imagery");
		imageryButton.setGraphic(new ImageView(imageryImage));
		imageryButton.setContentDisplay(ContentDisplay.TOP);
		imageryButton.setToggleGroup(basemapToggle);
		imageryLabelsButton.setText("Imagery with Labels");
		imageryLabelsButton.setGraphic(new ImageView(imageryLabelsImage));
		imageryLabelsButton.setContentDisplay(ContentDisplay.TOP);
		imageryLabelsButton.setToggleGroup(basemapToggle);
		streetsButton.setText("Streets");
		streetsButton.setGraphic(new ImageView(streetsImage));
		streetsButton.setContentDisplay(ContentDisplay.TOP);
		streetsButton.setToggleGroup(basemapToggle);
		topoButton.setText("Topographic");
		topoButton.setGraphic(new ImageView(topoImage));
		topoButton.setContentDisplay(ContentDisplay.TOP);
		topoButton.setToggleGroup(basemapToggle);
		darkGrayButton.setText("Dark Gray");
		darkGrayButton.setGraphic(new ImageView(darkGrayImage));
		darkGrayButton.setContentDisplay(ContentDisplay.TOP);
		darkGrayButton.setToggleGroup(basemapToggle);
		lightGrayButton.setText("Light Gray");
		lightGrayButton.setGraphic(new ImageView(lightGrayImage));
		lightGrayButton.setContentDisplay(ContentDisplay.TOP);
		lightGrayButton.setToggleGroup(basemapToggle);
		natgeoButton.setText("National Geographic");
		natgeoButton.setGraphic(new ImageView(natGeoImage));
		natgeoButton.setContentDisplay(ContentDisplay.TOP);
		natgeoButton.setToggleGroup(basemapToggle);
		oceansButton.setText("Oceans");
		oceansButton.setGraphic(new ImageView(oceansImage));
		oceansButton.setContentDisplay(ContentDisplay.TOP);
		oceansButton.setToggleGroup(basemapToggle);
		terrainButton.setText("Terrain with Labels");
		terrainButton.setGraphic(new ImageView(terrainImage));
		terrainButton.setContentDisplay(ContentDisplay.TOP);
		terrainButton.setToggleGroup(basemapToggle);
		osmButton.setText("OpenStreetMap");
		osmButton.setGraphic(new ImageView(osmImage));
		osmButton.setContentDisplay(ContentDisplay.TOP);
		osmButton.setToggleGroup(basemapToggle);
		
		gridPane.add(imageryButton, 0, 0);
		gridPane.add(imageryLabelsButton, 1, 0);
		gridPane.add(streetsButton, 2, 0);
		gridPane.add(topoButton, 0, 1);
		gridPane.add(darkGrayButton, 1, 1);
		gridPane.add(lightGrayButton, 2, 1);
		gridPane.add(natgeoButton, 0, 2);
		gridPane.add(oceansButton, 1, 2);
		gridPane.add(terrainButton, 2, 2);
		//gridPane.add(osmButton, 0, 3);
		
		setContent(gridPane);
	}
	
	public Basemap getSelectedBasemap(){
		Basemap basemap = Basemap.createStreets();
		ToggleButton selectedButton = (ToggleButton)basemapToggle.getSelectedToggle();
		if(selectedButton != null){
			if(selectedButton.equals(imageryButton)){
				basemap = Basemap.createImagery();
			}else if(selectedButton.equals(imageryLabelsButton)){
				basemap = Basemap.createImageryWithLabels();
			}else if(selectedButton.equals(streetsButton)){
				basemap = Basemap.createStreets();
			}else if(selectedButton.equals(topoButton)){
				basemap = Basemap.createTopographic();
			}else if(selectedButton.equals(darkGrayButton)){
				basemap = Basemap.createDarkGrayCanvasVector();	// 1970c1995b8f44749f4b9b6e81b5ba45
			}else if(selectedButton.equals(lightGrayButton)){
				basemap = Basemap.createLightGrayCanvas();
			}else if(selectedButton.equals(natgeoButton)){
				basemap = Basemap.createNationalGeographic();
			}else if(selectedButton.equals(oceansButton)){
				basemap = Basemap.createOceans();
			}else if(selectedButton.equals(terrainButton)){
				basemap = Basemap.createTerrainWithLabels();
			}else if(selectedButton.equals(osmButton)){
				Portal ago = new Portal("http://www.arcgis.com/home/");
				PortalItem item = new PortalItem(ago, "b834a68d7a484c5fb473d4ba90d35e71");
				basemap = new Basemap(item);
			}			
		}
		
		return basemap;
	}
}
