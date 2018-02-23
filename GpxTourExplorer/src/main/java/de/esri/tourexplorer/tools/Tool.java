package de.esri.tourexplorer.tools;

import com.esri.arcgisruntime.mapping.view.GeoView;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public abstract class Tool implements EventHandler<MouseEvent>{
	protected GeoView geoView;
	protected boolean isShiftDown;
	protected boolean isControlDown;
	
	public Tool(GeoView geoView){
		this.geoView = geoView;
	}
	
	public  void activate(){
		geoView.addEventHandler(MouseEvent.ANY, this);
	}	

	public void deactivate(){
		geoView.removeEventHandler(MouseEvent.ANY, this);
	}
	
	@Override
	public void handle(MouseEvent event) {		
		if(event.getEventType() == MouseEvent.MOUSE_PRESSED){
			mousePressed(event);
		}else if(event.getEventType() == MouseEvent.MOUSE_RELEASED){
			mouseReleased(event);
		}else if(event.getEventType() == MouseEvent.MOUSE_CLICKED){
			mouseClicked(event);
		}else if(event.getEventType() == MouseEvent.MOUSE_MOVED){
			mouseMoved(event);
		}else if(event.getEventType() == MouseEvent.MOUSE_DRAGGED){
			mouseDragged(event);
		}		
	}
	
	protected abstract void mousePressed(MouseEvent event);
	
	protected abstract void mouseReleased(MouseEvent event);
	
	protected abstract void mouseClicked(MouseEvent event);
	
	protected abstract void mouseMoved(MouseEvent event);
	
	protected abstract void mouseDragged(MouseEvent event);
}
