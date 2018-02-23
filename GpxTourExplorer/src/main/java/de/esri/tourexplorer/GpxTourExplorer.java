package de.esri.tourexplorer;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.MapView;

import de.esri.tourexplorer.core.App;
import de.esri.tourexplorer.ui.MapRootPane;
import de.esri.tourexplorer.ui.BottomPane;
import de.esri.tourexplorer.ui.menus.CustomizeMenu;
import de.esri.tourexplorer.ui.menus.FileMenu;
import de.esri.tourexplorer.ui.toolbars.AnalysisToolBar;
import de.esri.tourexplorer.ui.toolbars.NavigationToolBar;
import de.esri.tourexplorer.ui.toolbars.StandardToolBar;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GpxTourExplorer extends Application{
	private ArcGISMap map;
    private MapView mapView;  

	@Override
	public void start(Stage primaryStage) throws Exception {
		ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud8073461844,none,4N5X0H4AH4GF8YAJM165");
		Pane root = new Pane();
	    Scene scene = new Scene(root);	
	    scene.getStylesheets().add("/res/css/styles.css");
	    Image appIcon = new Image(getClass().getResourceAsStream("/res/images/AppIcon16.png"));	 
	    primaryStage.setTitle("Gpx Tour Explorer");
	    primaryStage.setWidth(1410);
	    primaryStage.setHeight(1030);
	    primaryStage.getIcons().add(appIcon);
	    primaryStage.setScene(scene);
	    primaryStage.show();
	    
	    showSplashScreen(scene);
	}
	
	private void showSplashScreen(Scene scene){
		BorderPane  splashPane = new BorderPane();
		splashPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		HBox topBox = new HBox();
		topBox.setAlignment(Pos.CENTER);
		topBox.setBackground(new Background(new BackgroundFill(Color.rgb(23, 57, 98), CornerRadii.EMPTY, Insets.EMPTY)));
		Label header = new Label("GPX Tour Explorer");
		header.setFont(new Font("Arial", 46));
		header.setTextFill(Color.rgb(255, 141, 0));
		topBox.getChildren().add(header);
		splashPane.setTop(topBox);
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setBackground(new Background(new BackgroundFill(Color.rgb(255, 141, 0), CornerRadii.EMPTY, Insets.EMPTY)));
		Image mountainImage = new Image("/res/images/Mountain.jpg");
		ImageView mountainImageView = new ImageView(mountainImage);
		grid.add(mountainImageView, 0, 0);
		Image mountainbikeImage = new Image("/res/images/Mountainbike.jpg");
		ImageView mountainbikeImageView = new ImageView(mountainbikeImage);
		grid.add(mountainbikeImageView, 1, 0);
		Image roadbikeImage = new Image("/res/images/Roadbike.jpg");
		ImageView roadbikeImageView = new ImageView(roadbikeImage);
		grid.add(roadbikeImageView, 0, 1);
		Image hikingImage = new Image("/res/images/Hiking.jpg");
		ImageView hikingImageView = new ImageView(hikingImage);
		grid.add(hikingImageView, 1, 1);

		splashPane.setCenter(grid);
		scene.setRoot(splashPane);
		splashPane.setOpacity(0);
		
		FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.5), splashPane);
		fadeIn.setFromValue(0.0);
		fadeIn.setToValue(1.0);
		
		PauseTransition pause = new PauseTransition(Duration.seconds(4.5));
		
		FadeTransition fadeOut = new FadeTransition(Duration.seconds(1.0), splashPane);
		fadeOut.setFromValue(1.0);
		fadeOut.setToValue(0.0);
		
		SequentialTransition fadeSplash = new SequentialTransition();
		fadeSplash.getChildren().addAll(fadeIn, pause, fadeOut);
		fadeSplash.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				showApplication(scene);
			}
		});		
		fadeSplash.play();
	}	
	
	private void showApplication(Scene scene){
	    BorderPane  appPane = new BorderPane();	    
	    
	    BorderPane  contentPane = new BorderPane();	 
	    
	    MapRootPane mapRootPane = new MapRootPane();
	    map = new ArcGISMap();
	    map.setBasemap(Basemap.createNationalGeographic());	         
	    
	    mapView = new MapView();
	    mapView.setMap(map);
	    App.getInstance().setMapView(mapView);
	    mapRootPane.addGeoView(mapView);
	    contentPane.setCenter(mapRootPane);
	    
	    // Menu
	    MenuBar menuBar = new MenuBar();
	    FileMenu fileMenu = new FileMenu(mapView);	    
	    CustomizeMenu customizeMenu = new CustomizeMenu(mapView);
	    menuBar.getMenus().addAll(fileMenu, customizeMenu);	    
	    appPane.setTop(menuBar);
	       
	    StandardToolBar standardToolBar = new StandardToolBar(mapView);
	    NavigationToolBar navigationToolBar = new NavigationToolBar(mapView);
	    AnalysisToolBar analysisToolBar = new AnalysisToolBar(mapView);
	    FlowPane toolBarPane = new FlowPane();
	    toolBarPane.getChildren().addAll(standardToolBar, navigationToolBar, analysisToolBar);
	    contentPane.setTop(toolBarPane);	
	    BottomPane bottomPane = new BottomPane();	    
	    contentPane.setBottom(bottomPane);
	    appPane.setCenter(contentPane);
	    
	    scene.setRoot(appPane);
	    
		FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.0), appPane);
		fadeIn.setFromValue(0.0);
		fadeIn.setToValue(1.0);
		fadeIn.play();
	}
	
	@Override
	public void stop() throws Exception {
		if(mapView != null){
			mapView.dispose();			
		}
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}
}
