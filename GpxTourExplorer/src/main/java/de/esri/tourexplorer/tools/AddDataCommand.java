package de.esri.tourexplorer.tools;

import java.util.Optional;

import com.esri.arcgisruntime.data.Geodatabase;
import com.esri.arcgisruntime.data.GeodatabaseFeatureTable;
import com.esri.arcgisruntime.data.TileCache;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.layers.FeatureCollectionLayer;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.LayerList;
import com.esri.arcgisruntime.mapping.view.GeoView;
import com.esri.arcgisruntime.mapping.view.MapView;

import de.esri.tourexplorer.core.App;
import de.esri.tourexplorer.data.DataInfo;
import de.esri.tourexplorer.data.DataType;
import de.esri.tourexplorer.data.gpx.GpxUtil;
import de.esri.tourexplorer.ui.BottomPane;
import de.esri.tourexplorer.ui.Profile;
import de.esri.tourexplorer.ui.dialogs.data.AddDataDialog;

public class AddDataCommand extends Command{
	private AddDataDialog dialog;

	public AddDataCommand(GeoView geoView) {
		super(geoView);
	}

	@Override
	public void execute() {
		if(dialog == null){
			dialog = new AddDataDialog();
			dialog.setOwner(geoView.getScene().getWindow());
		}
		Optional<Object> result = dialog.showAndWait();
		if(result.isPresent()){
			Object data = result.get();
			if(data instanceof DataInfo){
				DataInfo dataInfo = (DataInfo)data;
				DataType dataType = dataInfo.getDataType();
				if(dataType.equals(DataType.BASEMAP)){
					Basemap basemap = (Basemap)dataInfo.getData();
					setBasemap(basemap);
				}else if (dataType.equals(DataType.TPK)){					
					addTilePackage(dataInfo.getPath());
				}else if (dataType.equals(DataType.FEATURECLASS)){
					addFeatureclass(dataInfo.getName(), dataInfo.getPath());
				}else if (dataType.equals(DataType.GPX)){
					addGpxFile(dataInfo.getPath());
				}				
			}
		}
	}
	
	private void setBasemap(Basemap basemap){
		MapView mapView = (MapView)geoView;
		mapView.getMap().setBasemap(basemap);
	}
	
	private void addTilePackage(String path){
		TileCache tileCache = new TileCache(path);
		ArcGISTiledLayer tiledLayer = new ArcGISTiledLayer(tileCache);
		App.getInstance().getMap().getOperationalLayers().add(tiledLayer);
	}
	
	private void addFeatureclass(String name, String path){
		Geodatabase gdb = new Geodatabase(path);
		gdb.addDoneLoadingListener(new Runnable(){
			@Override
			public void run() {
				GeodatabaseFeatureTable table = gdb.getGeodatabaseFeatureTable(name);
				FeatureLayer layer = new FeatureLayer(table);
				App.getInstance().getMap().getOperationalLayers().add(layer);
			}			
		});
		gdb.loadAsync();
	}	
	
	private void addGpxFile(String path){
		LayerList layerList = App.getInstance().getMap().getOperationalLayers();
		for(int i = 0; i < layerList.size(); i++){
			Layer layer = layerList.get(i);
			if(layer.getId().equals("TrackLayer")){
				layerList.remove(layer);
			}
		}
		
		FeatureCollectionLayer gpxLayer = GpxUtil.createTracksLayer(path, null);
		gpxLayer.addDoneLoadingListener(new Runnable() {			
			@Override
			public void run() {
				Envelope env = gpxLayer.getFullExtent();
				//System.out.println("Width: " + env.getWidth());
				//System.out.println("Height: " + env.getHeight());				
				//System.out.println("Ext: " + env.toJson());
				if(env.getWidth() != Double.NaN && env.getHeight() != Double.NaN){
					try{
						App.getInstance().getMapView().setViewpointGeometryAsync(env, 100);
					}catch(Exception ex){					
					}
				}
			}
		});
		App.getInstance().getMap().getOperationalLayers().add(gpxLayer);
		
		BottomPane bottomPane = (BottomPane)App.getInstance().getComponent(BottomPane.class.getName());
		Profile profile = new Profile();
		profile.loadGpxFile(path);
		bottomPane.setContent(profile);
	}	
}
