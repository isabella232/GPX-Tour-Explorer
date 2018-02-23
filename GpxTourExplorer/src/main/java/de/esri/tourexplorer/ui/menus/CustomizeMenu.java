package de.esri.tourexplorer.ui.menus;

import java.util.ResourceBundle;

import com.esri.arcgisruntime.mapping.view.GeoView;

import de.esri.tourexplorer.core.App;
import de.esri.tourexplorer.core.App.LocaleChangeListener;
import de.esri.tourexplorer.tools.LanguageCommand;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class CustomizeMenu extends Menu implements LocaleChangeListener{
	private ResourceBundle res;
	protected Menu toolbarsMenu;
	protected CheckMenuItem standardToolbarMenuItem;
	protected CheckMenuItem navigationToolbarMenuItem;
	protected CheckMenuItem analysisToolbarMenuItem;
	protected MenuItem windowsMenuItem;
	protected MenuItem languageMenuItem;
	protected LanguageCommand languageCommand;
	
	public CustomizeMenu(GeoView geoView){
		App.getInstance().addLocaleChangeListener(this);
		toolbarsMenu = new Menu();	
		standardToolbarMenuItem = new CheckMenuItem();
		standardToolbarMenuItem.setSelected(true);
		standardToolbarMenuItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Parent root = geoView.getScene().getRoot();
				Node toolBar = root.lookup("#StandardToolBar");
				if(standardToolbarMenuItem.isSelected()){					
					toolBar.setVisible(true);
					toolBar.setManaged(true);
				}else{
					toolBar.setVisible(false);
					toolBar.setManaged(false);
				}
			}
		});
		navigationToolbarMenuItem = new CheckMenuItem();
		navigationToolbarMenuItem.setSelected(true);
		navigationToolbarMenuItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Parent root = geoView.getScene().getRoot();
				Node toolBar = root.lookup("#NavigationToolBar");
				if(navigationToolbarMenuItem.isSelected()){
					toolBar.setVisible(true);
					toolBar.setManaged(true);
				}else{
					toolBar.setVisible(false);
					toolBar.setManaged(false);
				}
			}
		});
		analysisToolbarMenuItem = new CheckMenuItem();
		analysisToolbarMenuItem.setSelected(true);
		analysisToolbarMenuItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Parent root = geoView.getScene().getRoot();
				Node toolBar = root.lookup("#AnalysisToolBar");
				if(analysisToolbarMenuItem.isSelected()){
					toolBar.setVisible(true);
					toolBar.setManaged(true);
				}else{
					toolBar.setVisible(false);
					toolBar.setManaged(false);
				}
			}
		});
		toolbarsMenu.getItems().addAll(standardToolbarMenuItem, navigationToolbarMenuItem, analysisToolbarMenuItem);
		
		windowsMenuItem = new MenuItem();
		languageMenuItem = new MenuItem();
		languageCommand = new LanguageCommand(geoView);
		languageMenuItem.setOnAction(new EventHandler<ActionEvent>() {			
			@Override
			public void handle(ActionEvent event) {
				languageCommand.execute();
			}
		});
		getItems().addAll(toolbarsMenu, languageMenuItem);
		init();
	}
	
	private void init(){
		res = ResourceBundle.getBundle("res.locale.CustomizeMenu");
		setText(res.getString("title"));
		toolbarsMenu.setText(res.getString("toolbarsMenuItem"));
		standardToolbarMenuItem.setText(res.getString("standardToolBar"));
		navigationToolbarMenuItem.setText(res.getString("navigationToolBar"));
		analysisToolbarMenuItem.setText(res.getString("analysisToolBar"));
		windowsMenuItem.setText(res.getString("windowsMenuItem"));
		languageMenuItem.setText(res.getString("languageMenuItem"));
	}

	@Override
	public void localeChanged() {
		init();
	}
}
