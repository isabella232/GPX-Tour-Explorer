package de.esri.tourexplorer.tools;

import com.esri.arcgisruntime.mapping.view.GeoView;

import de.esri.tourexplorer.ui.MapRootPane;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public abstract class CircleTool extends Tool{
	private Circle circle;
	private Color lineColor = Color.BLACK;
	private double lineWidth = 1.0;
	private boolean dragging;
	private double centerX;
	private double centerY;

	public CircleTool(GeoView geoView) {
		super(geoView);
		circle = new Circle();
		circle.setFill(Color.TRANSPARENT);
		circle.setStroke(lineColor);
		circle.setStrokeWidth(lineWidth);
	}
	
	@Override
	public void activate() {
		super.activate();
		Parent parent = geoView.getParent();
		if(parent instanceof MapRootPane){
			MapRootPane rootPane = (MapRootPane)geoView.getParent();
			rootPane.getChildren().add(circle);
		}
	}

	@Override
	public void deactivate() {
		super.deactivate();
		Parent parent = geoView.getParent();
		if(parent instanceof MapRootPane){
			MapRootPane rootPane = (MapRootPane)geoView.getParent();
			rootPane.getChildren().remove(circle);
		}
	}
	
	public void setLineColor(Color lineColor){
		this.lineColor = lineColor;
		circle.setStroke(lineColor);
	}
	
	public void setLineWidth(double lineWidth){
		this.lineWidth = lineWidth;
		circle.setStrokeWidth(lineWidth);
	}	

	@Override
	protected void mousePressed(MouseEvent e) {
		dragging = true;
		centerX = e.getX();
		centerY = e.getY();
		circle.setCenterX(centerX);
		circle.setCenterY(centerY);
	}

	@Override
	protected void mouseReleased(MouseEvent e) {
		dragging = false;
		isShiftDown = e.isShiftDown();
		isControlDown = e.isControlDown();
		double x = e.getX();
		double y = e.getY();
		double radius = getRadius(centerX, centerY, x, y);
		circle.setRadius(radius);
		execute(centerX, centerY, radius);
		circle.setCenterX(0);
		circle.setCenterY(0);
		circle.setRadius(0);
	}
	
	private double getRadius(double centerX, double centerY, double x, double y){
		double radius = 0.0;
		double dx = Math.abs(x - centerX);
		double dy = Math.abs(y - centerY);
		radius = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
		return radius;
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
			double radius = getRadius(centerX, centerY, x, y);
			circle.setRadius(radius);		
		}
	}
	
	protected abstract void execute(double centerX, double centerY, double radius);
}
