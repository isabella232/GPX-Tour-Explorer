package de.esri.tourexplorer.core;

import java.util.ArrayList;
import java.util.List;

import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.DrawStatus;
import com.esri.arcgisruntime.mapping.view.DrawStatusChangedEvent;
import com.esri.arcgisruntime.mapping.view.DrawStatusChangedListener;
import com.esri.arcgisruntime.mapping.view.GeoView;
import com.esri.arcgisruntime.mapping.view.NavigationChangedEvent;
import com.esri.arcgisruntime.mapping.view.NavigationChangedListener;
import com.esri.arcgisruntime.mapping.view.ViewpointChangedEvent;
import com.esri.arcgisruntime.mapping.view.ViewpointChangedListener;

public class ViewpointManager {
	private static ViewpointManager instance;
	private GeoView geoView;
	private List<Viewpoint> viewpointList = new ArrayList<Viewpoint>(); 
	private int index = -1;
	private boolean navigating = false;
	private boolean navigationViewpointChanged = false;
	private List<ViewpointListener> listeners = new ArrayList<ViewpointListener>();
	
	private ViewpointManager(GeoView geoView){	
		this.geoView = geoView;	
		
		geoView.addDrawStatusChangedListener(new DrawStatusChangedListener() {
			
			@Override
			public void drawStatusChanged(DrawStatusChangedEvent ev) {
				//System.out.println("DrawStatus changed");
				if(index < 0){
					DrawStatus drawStatus = ev.getDrawStatus();
					if(drawStatus == DrawStatus.COMPLETED){
						GeoView geoView = ev.getSource();
						Viewpoint viewpoint = geoView.getCurrentViewpoint(Viewpoint.Type.BOUNDING_GEOMETRY);
						addViewpoint(viewpoint);
					}					
				}				
			}
		});
		
		geoView.addNavigationChangedListener(new NavigationChangedListener() {
			
			@Override
			public void navigationChanged(NavigationChangedEvent ev) {				
				boolean isNavigating = ev.isNavigating();				
				//System.out.println("Navigation changed: " + isNavigating);
				if(isNavigating){
					navigating = true;
				}else{
					if(navigationViewpointChanged){
						GeoView geoView = ev.getSource();
						Viewpoint viewpoint = geoView.getCurrentViewpoint(Viewpoint.Type.BOUNDING_GEOMETRY);				
						addViewpoint(viewpoint);						
					}
					navigating = false;
					navigationViewpointChanged = false;
				}
			}
		});
		
		geoView.addViewpointChangedListener(new ViewpointChangedListener() {
			
			@Override
			public void viewpointChanged(ViewpointChangedEvent arg0) {
				//System.out.println("Viewpoint changed");
				if(navigating){
					navigationViewpointChanged = true;
				}
			}
		});
	}
	
	public static ViewpointManager getInstance(GeoView geoView){
		if(instance == null){
			instance = new ViewpointManager(geoView);			
		}
		return instance;
	}
	
	public void addViewpoint(Viewpoint viewpoint){
		index++;
		// first delete the entries with index grater than the current index
		if(index < viewpointList.size()){
			for(int i = viewpointList.size() - 1; i >= index; i--){
				viewpointList.remove(i);
			}
		}
		viewpointList.add(viewpoint);		
		notfiyViewpointChanged();
	}
	
	public void goToPreviousViewpoint(){		
		if(index >= 1){
			index--;
			Viewpoint previousViewpoint = viewpointList.get(index);
			geoView.setViewpoint(previousViewpoint);
			notfiyViewpointChanged();
		}
	}
	
	public void goToNextViewpoint(){		
		if(index < viewpointList.size() - 1){
			index++;
			Viewpoint nextViewpoint = viewpointList.get(index);
			geoView.setViewpoint(nextViewpoint);
			notfiyViewpointChanged();
		}
	}
	
	private void notfiyViewpointChanged(){
		boolean hasPrevious = index > 0;	
		boolean hasNext = index < viewpointList.size() - 1;	
		for(ViewpointListener listener: listeners){
			listener.viewpointChanged(hasPrevious, hasNext);
		}
	}
	
	public void addViewpointListener(ViewpointListener listener){
		listeners.add(listener);
	}
	
	public void removeViewpointListener(ViewpointListener listener){
		listeners.remove(listener);
	}
	
	public interface ViewpointListener{
		void viewpointChanged(boolean hasPrevious, boolean hasNext);
	}
}
