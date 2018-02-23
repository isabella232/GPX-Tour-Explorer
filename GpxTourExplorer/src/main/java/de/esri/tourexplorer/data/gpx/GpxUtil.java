package de.esri.tourexplorer.data.gpx;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureCollection;
import com.esri.arcgisruntime.data.FeatureCollectionTable;
import com.esri.arcgisruntime.data.Field;
import com.esri.arcgisruntime.geometry.GeometryType;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.FeatureCollectionLayer;
import com.esri.arcgisruntime.symbology.Renderer;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleRenderer;

import com.esri.arcgisruntime.symbology.SimpleLineSymbol.Style;

public class GpxUtil {

	public static FeatureCollectionLayer createTracksLayer(String gpxFile, Renderer renderer){
		List<Field> gpxFields = new ArrayList<Field>();
        Field trackNameField = Field.createString("TrackName", "TrackName", 50);
        gpxFields.add(trackNameField);
		FeatureCollectionTable gpxTable = new FeatureCollectionTable(gpxFields, GeometryType.POLYLINE, SpatialReferences.getWgs84());
		if(renderer == null){
			renderer = new SimpleRenderer();
			SimpleLineSymbol sls = new SimpleLineSymbol(Style.SOLID, 0xffff0000, 2.0f);
			((SimpleRenderer)renderer).setSymbol(sls);
		}
		gpxTable.setRenderer(renderer);
		
		try {
			InputStream in = new FileInputStream(gpxFile);
			GpxParser parser = new GpxParser();
			Gpx gpx = parser.parse(in);
			List<Track> tracks = gpx.getTracks();
			for(Track track : tracks){
				List<TrackSegment> trackSegments = track.getTrackSegments();
				for(TrackSegment trackSegment : trackSegments){					
					PointCollection linePoints = new PointCollection(SpatialReferences.getWgs84());
					List<Waypoint> waypoints =  trackSegment.getTrackPoints();
					for(Waypoint waypoint : waypoints){
						linePoints.add(waypoint.getLongitude(), waypoint.getLatitude());
					}
					Polyline polyline = new Polyline(linePoints);
					Feature trackLine = gpxTable.createFeature();
					trackLine.getAttributes().put("TrackName", track.getName());
					trackLine.setGeometry(polyline);
					gpxTable.addFeatureAsync(trackLine);
				}
			}			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		FeatureCollection featureCollection = new FeatureCollection();
	    featureCollection.getTables().add(gpxTable);
	    FeatureCollectionLayer tracksLayer = new FeatureCollectionLayer(featureCollection);
	    tracksLayer.setId("TrackLayer");
	    return tracksLayer;
	}	
}
