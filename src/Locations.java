import java.util.ArrayList;
import java.util.List;

public class Locations extends ArrayList<Point2D> {
	private double perimeter;
	public Locations(List<Point2D> points) {
		super();
		addAll(points);
		perimeter = perimeter(this);
	}
	
	public Locations(Point2D... points) {
		super();
		for(Point2D point : points) {
			add(point);
		}
		perimeter = perimeter(this);
	}
	
	public static double perimeter(List<Point2D> points) {
		if(points.size() < 3) {
			throw new IllegalArgumentException();
		}
		
		double perimeter = 0;
		for(int pointIndex = 0; pointIndex < points.size(); pointIndex++){
			perimeter += TdoaUtils.distance(points.get(pointIndex), points.get((pointIndex + 1) % points.size()));
		}
		
		return perimeter;
	}

	public double getPerimeter() {
		return perimeter;
	}

	public void setPerimeter(double perimeter) {
		this.perimeter = perimeter;
	}
}
