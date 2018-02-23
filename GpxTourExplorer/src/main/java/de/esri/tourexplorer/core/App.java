package de.esri.tourexplorer.core;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.view.MapView;

public class App {
	private static App instance;
	private List<LocaleChangeListener> localeChangeListeners = new ArrayList<LocaleChangeListener>();
	private MapView mapView;
	private Hashtable<String, Object> components = new Hashtable<>();
	
	private App(){	
	}
	
	public static App getInstance(){
		if(instance == null){
			instance = new App();	
		}
		return instance;
	}
	
	public void setMapView(MapView mapView){
		this.mapView = mapView;
	}
	
	public MapView getMapView(){
		return mapView;
	}
	
	public ArcGISMap getMap(){
		ArcGISMap map = null;
		if(mapView != null){
			map = mapView.getMap();
		}
		return map;
	}
	
	public void registerComponent(String key, Object component){
		components.put(key, component);
	}
	
	public Object getComponent(String key){
		Object component = null;
		if(components.containsKey(key)){
			component = components.get(key);
		}
		return component;
	}

	public void notifyLocaleChanged(){
		for(LocaleChangeListener listener: localeChangeListeners){
			listener.localeChanged();
		}
	}
	
	public void addLocaleChangeListener(LocaleChangeListener listener){
		localeChangeListeners.add(listener);
	}
	
	public void removeLocaleChangeListener(LocaleChangeListener listener){
		localeChangeListeners.remove(listener);
	}
	
	public interface LocaleChangeListener{
		void localeChanged();
	}
}
