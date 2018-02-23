package de.esri.tourexplorer.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.mapping.GeoElement;
import com.esri.arcgisruntime.mapping.view.GeoView;
import com.esri.arcgisruntime.mapping.view.IdentifyLayerResult;

import de.esri.tourexplorer.ui.dialogs.IdentifyDialog;
import javafx.geometry.Point2D;

public class IdentifyTool extends CircleTool{
	private IdentifyDialog dialog;

	public IdentifyTool(GeoView geoView) {
		super(geoView);
	}

	@Override
	protected void execute(double centerX, double centerY, double radius) {
		if(radius > 100.0){
			radius = 100.0;
		}
		if(radius < 5){
			radius = 5;
		}
		Point2D centerPoint = new Point2D((float)centerX, (float)centerY);		 
		ListenableFuture<List<IdentifyLayerResult>> identifyResults = geoView.identifyLayersAsync(centerPoint, radius, false, 500);
		identifyResults.addDoneListener(new Runnable() {
			@Override
			public void run() {
				try {
					List<IdentifyLayerResult> identifyLayerResults = identifyResults.get();
					showIdentifyResults(identifyLayerResults);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private void showIdentifyResults(List<IdentifyLayerResult> identifyLayerResults){
		IdentifyResult identifyResult = new IdentifyResult();
		for (IdentifyLayerResult identifyLayerResult : identifyLayerResults) {
			String layerName = identifyLayerResult.getLayerContent().getName();
			for (GeoElement identifiedElement : identifyLayerResult.getElements()) {
				if (identifiedElement instanceof Feature) {
	    			  Feature feature = (Feature) identifiedElement;
	    			  IdentifyObject identifyObject = new IdentifyObject(layerName, feature);
	    			  identifyResult.addIdentifyObject(layerName, identifyObject);
	    		  }
			}
			List<IdentifyLayerResult> subLayerResults = identifyLayerResult.getSublayerResults();
			for(IdentifyLayerResult subLayerResult : subLayerResults){
				String subLayerName = subLayerResult.getLayerContent().getName();
				for (GeoElement subLayerElement : subLayerResult.getElements()) {
					if (subLayerElement instanceof Feature) {
		    			  Feature feature = (Feature) subLayerElement;
		    			  IdentifyObject identifyObject = new IdentifyObject(subLayerName, feature);
		    			  identifyResult.addIdentifyObject(subLayerName, identifyObject);
		    		  }
				}
			}
		}
		
		if(dialog == null){
			dialog = new IdentifyDialog();
			dialog.setOwner(geoView.getScene().getWindow());
			dialog.setIdentifyResult(identifyResult);			
		}else{
			dialog.setIdentifyResult(identifyResult);			
		}
		dialog.show();
	}
	
	public class IdentifyObject{
		private String layerName;
		private Feature feature;
		
		public IdentifyObject(){			
		}
		
		public IdentifyObject(String layerName, Feature feature){
			this.layerName = layerName;
			this.feature = feature;
		}
		
		public String getLayerName() {
			return layerName;
		}

		public void setLayerName(String layerName) {
			this.layerName = layerName;
		}

		public Feature getFeature() {
			return feature;
		}

		public void setFeature(Feature feature) {
			this.feature = feature;
		}
		
		@Override
		public String toString() {
			String info = "<Feature>";	
			Map<String, String> textAttrs = new HashMap<>();
			Map<String, Object> attrs = feature.getAttributes();
			for (Map.Entry<String, Object> attrList : attrs.entrySet()){
				String attrName = attrList.getKey();	
				Object value = attrList.getValue();
				//System.out.println("Attr Name: "+ attrName + ", Value: " + value);
				if(value instanceof String){
					String text = (String)value;					
					if(text != null && !text.trim().equals("") && !attrName.equals("Shape")){
						if(!text.matches("[-+]?\\d*\\.?\\d+")){
							textAttrs.put(attrName, text);
						}																
					}
				}
			}
			if(textAttrs.size() > 0){
				String nameAttr = textAttrs.entrySet().stream().filter(map -> map.getKey().equalsIgnoreCase("name")).map(map -> map.getValue()).collect(Collectors.joining());
				if(nameAttr != null && ! nameAttr.equals("")){
					info = nameAttr;
				}else{
					nameAttr = textAttrs.entrySet().stream().filter(map -> map.getKey().toLowerCase().contains("name")).map(map -> map.getValue()).collect(Collectors.joining());
					if(nameAttr != null && ! nameAttr.equals("")){
						info = nameAttr;
					}else{
						info = textAttrs.values().iterator().next();
					}
				}	
			}			
			return info;
		}
	}
	
	public class IdentifyResult{
		private Map<String, List<IdentifyObject>> identifiedObjects = new HashMap<>();
		
		public void addIdentifyObject(String layerName, IdentifyObject identifyObject){
			if(identifiedObjects.containsKey(layerName)){
				List<IdentifyObject> layerObjects = identifiedObjects.get(layerName);
				layerObjects.add(identifyObject);
			}else{
				List<IdentifyObject> layerObjects = new ArrayList<>();
				layerObjects.add(identifyObject);
				identifiedObjects.put(layerName, layerObjects);
			}
		}

		public Map<String, List<IdentifyObject>> getIdentifiedObjects() {
			return identifiedObjects;
		}				
	}
}
