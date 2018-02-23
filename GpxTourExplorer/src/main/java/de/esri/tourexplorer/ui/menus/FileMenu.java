package de.esri.tourexplorer.ui.menus;

import java.util.ResourceBundle;

import com.esri.arcgisruntime.mapping.view.GeoView;

import de.esri.tourexplorer.core.App;
import de.esri.tourexplorer.core.App.LocaleChangeListener;
import de.esri.tourexplorer.tools.AddDataCommand;
import de.esri.tourexplorer.tools.NewMapCommand;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FileMenu extends Menu implements LocaleChangeListener{
	private ResourceBundle res;
	protected MenuItem newMenuItem;
	protected MenuItem openMenuItem;
	protected MenuItem saveMenuItem;
	protected MenuItem addDataMenuItem;
	protected AddDataCommand addData;
	protected NewMapCommand newProject;

	public FileMenu(GeoView geoView){
		App.getInstance().addLocaleChangeListener(this);
		newMenuItem = new MenuItem();
		
		Image newImage = new Image("/res/images/NewProject16.png");
		ImageView newImageView = new ImageView(newImage);
		newMenuItem.setGraphic(newImageView);
		newProject = new NewMapCommand(geoView);
		newMenuItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				newProject.execute();
			}
		});
		
		addDataMenuItem = new MenuItem();
		Image addDataImage = new Image("/res/images/AddData16.png");
		ImageView addDataImageView = new ImageView(addDataImage);
		addDataMenuItem.setGraphic(addDataImageView);	
		addData = new AddDataCommand(geoView);
		addDataMenuItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				addData.execute();
			}
		});
		
		getItems().addAll(newMenuItem, addDataMenuItem);
		init();
	}
	
	private void init(){
		res = ResourceBundle.getBundle("res.locale.FileMenu");
		setText(res.getString("title"));
		newMenuItem.setText(res.getString("newMenuItem"));
		addDataMenuItem.setText(res.getString("addDataMenuItem"));
	}

	@Override
	public void localeChanged() {
		init();
	}
}
