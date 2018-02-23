package de.esri.tourexplorer.tools;

import com.esri.arcgisruntime.mapping.view.GeoView;

import de.esri.tourexplorer.ui.MapRootPane;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class RectangleTool extends Tool{
	private Rectangle rect;
	private Color lineColor = Color.BLACK;
	private double lineWidth = 1.0;
	private boolean dragging;
	private double ancorX;
	private double ancorY;
	private double newX;
	private double newY;
	private double newWidth;
	private double newHeight;

	public RectangleTool(GeoView geoView) {
		super(geoView);
		rect = new Rectangle();
		rect.setFill(Color.TRANSPARENT);
		rect.setStroke(lineColor);
		rect.setStrokeWidth(lineWidth);
	}
	
	@Override
	public void activate() {
		super.activate();
		geoView.setCursor(Cursor.CROSSHAIR);
		Parent parent = geoView.getParent();
		if(parent instanceof MapRootPane){
			MapRootPane rootPane = (MapRootPane)geoView.getParent();
			rootPane.getChildren().add(rect);
		}
	}

	@Override
	public void deactivate() {
		super.deactivate();
		geoView.setCursor(Cursor.DEFAULT);
		Parent parent = geoView.getParent();
		if(parent instanceof MapRootPane){
			MapRootPane rootPane = (MapRootPane)geoView.getParent();
			rootPane.getChildren().remove(rect);
		}
	}
	
	public void setLineColor(Color lineColor){
		this.lineColor = lineColor;
		rect.setStroke(lineColor);
	}
	
	public void setLineWidth(double lineWidth){
		this.lineWidth = lineWidth;
		rect.setStrokeWidth(lineWidth);
	}

	@Override
	protected void mousePressed(MouseEvent e) {
		dragging = true;
		ancorX = e.getX();
		ancorY = e.getY();
		rect.setX(ancorX);
		rect.setY(ancorY);
	}

	@Override
	protected void mouseReleased(MouseEvent e) {
		dragging = false;
		isShiftDown = e.isShiftDown();
		isControlDown = e.isControlDown();
		double x = e.getX();
		double y = e.getY();
		setNewValues(x, y);
		execute(ancorX, ancorY, x, y);
		rect.setX(0);
		rect.setY(0);
		rect.setWidth(0);
		rect.setHeight(0);
	}

	@Override
	protected void mouseClicked(MouseEvent e) {
		
	}

	@Override
	protected void mouseMoved(MouseEvent e) {
		
	}

	@Override
	protected void mouseDragged(MouseEvent e) {
		if(dragging){
			double x = e.getX();
			double y = e.getY();
			setNewValues(x, y);		
		}
	}
	
	private void setNewValues(double x, double y){
		if(x >= ancorX){
			newX = ancorX;
			newWidth = x - ancorX;
		}else{
			newX = x;
			newWidth = ancorX - x;
		}
		if(y >= ancorY){
			newY = ancorY;
			newHeight = y - ancorY;
		}else{
			newY = y;
			newHeight = ancorY - y;
		}
		rect.setX(newX);
		rect.setY(newY);
		rect.setWidth(newWidth);
		rect.setHeight(newHeight);
	}

	protected abstract void execute(double x1, double y1, double x2, double y2);
}
