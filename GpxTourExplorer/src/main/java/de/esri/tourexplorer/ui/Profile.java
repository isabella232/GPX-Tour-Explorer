package de.esri.tourexplorer.ui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import com.esri.arcgisruntime.geometry.AngularUnit;
import com.esri.arcgisruntime.geometry.AngularUnitId;
import com.esri.arcgisruntime.geometry.GeodeticCurveType;
import com.esri.arcgisruntime.geometry.GeodeticDistanceResult;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.LinearUnit;
import com.esri.arcgisruntime.geometry.LinearUnitId;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;

import de.esri.tourexplorer.core.App;
import de.esri.tourexplorer.core.App.LocaleChangeListener;
import de.esri.tourexplorer.data.gpx.Gpx;
import de.esri.tourexplorer.data.gpx.GpxParser;
import de.esri.tourexplorer.data.gpx.Track;
import de.esri.tourexplorer.data.gpx.TrackSegment;
import de.esri.tourexplorer.data.gpx.Waypoint;
import javafx.geometry.Insets;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

public class Profile extends BorderPane implements LocaleChangeListener{
	private ResourceBundle res;
	private Tab profileTab;
	private Tab dataTab;
	private Label header;
	private Label distanceLabel;
	private Label timeLabel;
	private Label speedLabel;
	private Label minHeightLabel;
	private Label maxHeightLabel;
	private Label heightDiffLabel;
	private NumberAxis xAxis;
	private NumberAxis yAxis;
	private AreaChart<Number,Number> ac;
	private XYChart.Series profileSeries;
	
	public Profile(){
		App.getInstance().addLocaleChangeListener(this);
	}
	
	public void loadGpxFile(String gpxFile){
		TabPane tabPane = new TabPane();	
		profileTab = new Tab();
		dataTab = new Tab();
		tabPane.getTabs().addAll(profileTab, dataTab);
		
		try {
			InputStream in = new FileInputStream(gpxFile);
			GpxParser parser = new GpxParser();
			Gpx gpx = parser.parse(in);
			Track track = gpx.getTracks().get(0);
			double minHeight = Double.MAX_VALUE;
			double maxHeight = Double.MIN_VALUE;
			double heightDiff = 0.0;
			double length = 0.0;
			double lastLat = 0.0;
			double lastLon = 0.0;
			long startTime = 0;
			long lastTime = 0;
			List<Double[]> parts = new ArrayList<Double[]>();
			
			// get number of points
			int numPoints = 0;
			List<TrackSegment> trackSegments = track.getTrackSegments();
			for(TrackSegment trackSegment : trackSegments){
				numPoints += trackSegment.getTrackPoints().size();
			}
			int factor = numPoints / 1000;
			
			int count = 0;
			for(TrackSegment trackSegment : trackSegments){
				List<Waypoint> waypoints =  trackSegment.getTrackPoints();
				for(int i = 0; i < waypoints.size(); i+=factor){
					Waypoint waypoint = waypoints.get(i);
					double lat = waypoint.getLatitude();
					double lon = waypoint.getLongitude();
					double height = waypoint.getElevation();
					if(height < minHeight){
						minHeight = height;
					}
					if(height > maxHeight){
						maxHeight = height;
					}
					double distance = 0.0; 
					if(count > 0){
						// calculate the distance between point and last point
						Point firstPoint = new Point(lastLon, lastLat, SpatialReference.create(4326));
						Point lastPoint = new Point(lon, lat, SpatialReference.create(4326));
						GeodeticDistanceResult distanseResult = GeometryEngine.distanceGeodetic(firstPoint, lastPoint, new LinearUnit(LinearUnitId.METERS), new AngularUnit(AngularUnitId.DEGREES), GeodeticCurveType.GEODESIC);
						distance = distanseResult.getDistance();
						length += distance;
						parts.add(new Double[]{length, height});
					}
					lastLat = lat;
					lastLon = lon;	
					if(i == 0){
						startTime = waypoint.getTime().getTime();
					}
					lastTime = waypoint.getTime().getTime();
					count++;
				}
			}
			long timeDiff = lastTime - startTime;
			String timeStr = String.format("%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(timeDiff),
                    TimeUnit.MILLISECONDS.toMinutes(timeDiff) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeDiff)),
                    TimeUnit.MILLISECONDS.toSeconds(timeDiff) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeDiff)));
			double speed = length / timeDiff * 3600;
			heightDiff = maxHeight - minHeight;
//			System.out.println("Anzahl Punkte: "+count);
//			System.out.println("Strecke: "+length);
//			System.out.println("Zeit: "+timeStr);
//			System.out.println("Geschwindigkeit: "+speed);
//			System.out.println("Min Höhe: "+minHeight);
//			System.out.println("Max Höhe: "+maxHeight);
//			System.out.println("Höhenmeter: "+heightDiff);
			
			// tour data
			BorderPane dataPane = new BorderPane();
			HBox headerBox = new HBox();
			//headerBox.setBackground(new Background(new BackgroundFill(Color.rgb(255, 141, 0), CornerRadii.EMPTY, Insets.EMPTY)));
			headerBox.setPadding(new Insets(5, 15, 5, 15));
			header = new Label();
			headerBox.getChildren().add(header);
			header.setFont(new Font(32.0));
			dataPane.setTop(headerBox);
			HBox dataBox = new HBox();
			dataBox.setPadding(new Insets(5, 15, 5, 15));
			Font font = new Font(24.0);
			GridPane grid = new GridPane();
			grid.setHgap(20);
			grid.setVgap(10);
			distanceLabel = new Label();
			distanceLabel.setFont(font);
			grid.add(distanceLabel, 0, 0);
			timeLabel = new Label();
			timeLabel.setFont(font);
			grid.add(timeLabel, 0, 1);
			speedLabel = new Label();
			speedLabel.setFont(font);
			grid.add(speedLabel, 0, 2);
			minHeightLabel = new Label();
			minHeightLabel.setFont(font);
			grid.add(minHeightLabel, 0, 3);
			maxHeightLabel = new Label();
			maxHeightLabel.setFont(font);
			grid.add(maxHeightLabel, 0, 4);
			heightDiffLabel = new Label();
			heightDiffLabel.setFont(font);
			grid.add(heightDiffLabel, 0, 5);
			
			Label distanceValueLabel = new Label(String.format("%.3f km",(length / 1000)));
			distanceValueLabel.setFont(font);
			grid.add(distanceValueLabel, 1, 0);
			Label timeValueLabel = new Label(timeStr + " h");
			timeValueLabel.setFont(font);
			grid.add(timeValueLabel, 1, 1);
			Label speedValueLabel = new Label(String.format("%.2f km/h", speed));
			speedValueLabel.setFont(font);
			grid.add(speedValueLabel, 1, 2);
			Label minHeightValueLabel = new Label(String.format("%.2f m", minHeight));
			minHeightValueLabel.setFont(font);
			grid.add(minHeightValueLabel, 1, 3);
			Label maxHeightValueLabel = new Label(String.format("%.2f m", maxHeight));
			maxHeightValueLabel.setFont(font);
			grid.add(maxHeightValueLabel, 1, 4);
			Label heightDiffValueLabel = new Label(String.format("%.2f m", heightDiff));
			heightDiffValueLabel.setFont(font);
			grid.add(heightDiffValueLabel, 1, 5);
			
			dataBox.getChildren().add(grid);
			dataPane.setCenter(dataBox);
			dataTab.setContent(dataPane);
			
			// profile chart
	        xAxis = new NumberAxis(0, length, 1000);
	        yAxis = new NumberAxis(minHeight, maxHeight, 100);
	        ac = new AreaChart<>(xAxis,yAxis);	        
	        ac.setCreateSymbols(false);
	 
	        profileSeries = new XYChart.Series();	        	       
	        
	        for(Double[] part : parts){
	        	profileSeries.getData().add(new XYChart.Data(part[0], part[1]));
	        }	      	        	        
	        ac.getData().addAll(profileSeries);
	        
	        profileTab.setContent(ac);
			setCenter(tabPane);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		init();
	}

	private void init(){
		res = ResourceBundle.getBundle("res.locale.Profile");
		profileTab.setText(res.getString("profileTab"));
		dataTab.setText(res.getString("tourTab"));
		header.setText(res.getString("header"));
		distanceLabel.setText(res.getString("distance"));
		timeLabel.setText(res.getString("time"));
		speedLabel.setText(res.getString("speed"));
		minHeightLabel.setText(res.getString("minHeight"));
		maxHeightLabel.setText(res.getString("maxHeight"));
		heightDiffLabel.setText(res.getString("heightDiff"));
		xAxis.setLabel(res.getString("xAxis"));
		yAxis.setLabel(res.getString("yAxis"));
		ac.setTitle(res.getString("title"));
		profileSeries.setName(res.getString("name"));
	}
	
	@Override
	public void localeChanged() {
		init();
	}
}
