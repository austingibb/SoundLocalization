import java.util.Iterator;
import java.util.List;

public class TdoaUtils {
	public static double distance(Point2D p1, Point2D p2) {
		return Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2));
	}
	
	public static double distance(Point3D p1, Point3D p2) {
		return Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2) + Math.pow(p1.getZ() - p2.getZ(), 2));
	}
	
	public static Point2D centerOfPoints(List<Point2D> points) {
		double x = 0, y = 0;
		Iterator<Point2D> it = points.iterator();
		while(it.hasNext()) {
			Point2D point = it.next();
			x += point.getX();
			y += point.getY();
		}
		
		x /= points.size();
		y /= points.size();
		
		return new Point2D(x, y);
	}
	
	public static void fillTdoaArray(Point3D source, double[] tdoaArray, List<Point2D> points, double speed) {
		if(tdoaArray.length != points.size()) {
			throw new IllegalArgumentException();
		}
		
		for(int tdoaIndex = 0; tdoaIndex < tdoaArray.length; tdoaIndex++) {
			tdoaArray[tdoaIndex] = getTdoaAt(Point3D.to3D(points.get(tdoaIndex)), Point3D.to3D(points.get((tdoaIndex + 1) % points.size())), source, speed);
		}
	}
	
	public static void fillTdoaArray(Point3D source, List<Double> tdoaArray,  List<Point2D> points, double speed) {
		for(int tdoaIndex = 0; tdoaIndex < points.size(); tdoaIndex++) {
			tdoaArray.add(getTdoaAt(Point3D.to3D(points.get(tdoaIndex)), Point3D.to3D(points.get((tdoaIndex + 1) % points.size())), source, speed));
		}
	}
	
	public static double getTdoaAt(Point2D p1, Point2D p2, Point2D source, double speed) {
		double p1Dist = distance(p1, source);
		double p2Dist = distance(p2, source);
		double distDiff = p2Dist - p1Dist;
		double tdoa = distDiff/speed;
		return tdoa;
	}
	
	public static double getTdoaAt(Point3D p1, Point3D p2, Point3D source, double speed) {
		double p1Dist = distance(p1, source);
		double p2Dist = distance(p2, source);
		double distDiff = p2Dist - p1Dist;
		double tdoa = distDiff/speed;
		return tdoa;
	}
}
