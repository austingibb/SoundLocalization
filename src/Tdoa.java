import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Tdoa {	
	public static final int INITIAL_SAMPLES = 2000;
	
	private List<Point2D> points;
	private double speed;
	private double searchAreaScalar;
	private double height;
	
	private double[][][] tdoaGrid;
	private double[][][] positionGrid;
	
	public Tdoa(List<Point2D> points, double speed, double searchAreaScalar, double height) {
		if(points.size() < 3 || speed <= 0 || searchAreaScalar <= 0) {
			throw new IllegalArgumentException();
		}
		
		this.points = points; this.speed = speed; this.searchAreaScalar = searchAreaScalar; this.height = height;
		generateTdoaGrid();
	}
	
	public void generateTdoaGrid() {
		Point2D center = centerOfPoints(points);
	
		double width = (perimeter(points)) * searchAreaScalar;
		double shiftAmount = width/(INITIAL_SAMPLES - 1);
		double startingX = center.getX() - (width/2);
		double currentX = startingX;
		double currentY = center.getY() + (width/2);
		
		tdoaGrid = new double[INITIAL_SAMPLES][INITIAL_SAMPLES][points.size()];
		positionGrid = new double[INITIAL_SAMPLES][INITIAL_SAMPLES][2];
		Point3D reusedPoint = new Point3D(0, 0, height);
		for(int yIndex = 0; yIndex < INITIAL_SAMPLES; yIndex++) {
			for(int xIndex = 0; xIndex < INITIAL_SAMPLES; xIndex++) {
				reusedPoint.setX(currentX);
				reusedPoint.setY(currentY);
				fillTdoaArray(reusedPoint, tdoaGrid[xIndex][yIndex]);
				positionGrid[xIndex][yIndex][0] = currentX;
				positionGrid[xIndex][yIndex][1] = currentY;
				currentX += shiftAmount;
			}
			currentX = startingX;
			currentY -= shiftAmount;
		}
	}
	
	public void fillTdoaArray(Point3D source, double[] tdoaArray) {
		if(tdoaArray.length != points.size()) {
			throw new IllegalArgumentException();
		}
		
		for(int tdoaIndex = 0; tdoaIndex < tdoaArray.length; tdoaIndex++) {
			tdoaArray[tdoaIndex] = getTdoaAt(Point3D.to3D(points.get(tdoaIndex)), Point3D.to3D(points.get((tdoaIndex + 1) % points.size())), source, speed);
		}
	}
	
	public void fillTdoaArray(Point3D source, List<Double> tdoaArray) {
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
	
	public double[][] getTdoaDiffSumGrid(List<Double> tdoas) {
		if(tdoas.size() != points.size()) {
			throw new IllegalArgumentException();
		}
		
		double[][] tdoaDiffSumGrid = new double[INITIAL_SAMPLES][INITIAL_SAMPLES];
		for(int yIndex = 0; yIndex < INITIAL_SAMPLES; yIndex++) {
			for(int xIndex = 0; xIndex < INITIAL_SAMPLES; xIndex++) {
				double diffSum = 0;
				for(int tdoaIndex = 0; tdoaIndex < points.size(); tdoaIndex++) {
					diffSum += Math.abs(tdoas.get(tdoaIndex) - tdoaGrid[xIndex][yIndex][tdoaIndex]);
				}
				
				tdoaDiffSumGrid[xIndex][yIndex] = diffSum;
			}
		}
		
		return tdoaDiffSumGrid;
	}
	
	public int size() {
		return points.size();
	}
	
	public Point2D getPositionFromIndex(int x, int y) {
		return new Point2D(positionGrid[x][y][0], positionGrid[x][y][1]);
	}
	
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
	
	public static double perimeter(List<Point2D> points) {
		if(points.size() < 3) {
			throw new IllegalArgumentException();
		}
		
		double perimeter = 0;
		for(int pointIndex = 0; pointIndex < points.size(); pointIndex++){
			perimeter += distance(points.get(pointIndex), points.get((pointIndex + 1) % points.size()));
		}
		
		return perimeter;
	}
	
	public static void main(String args[]) {
		for(int i = 1; i <= 40; i++) {
			System.out.println(i);
		}
		List<Point2D> points = new ArrayList<Point2D>();
		points.add(new Point2D(-1, 0));
		points.add(new Point2D(0, 1));
		points.add(new Point2D(1, 0));
		Tdoa tdoa = new Tdoa(points, 747.9, 1.0, 0.07575);
		
		List<Double> tdoaArray = new ArrayList<Double>();
		tdoa.fillTdoaArray(new Point3D(0.2, -1.1, 0.1), tdoaArray);
				
		Localization loc = new Localization(tdoa, tdoaArray);
		Point2D estimatedLoc = loc.getEstimatedLocation();
		
		System.out.printf("Est Loc: (%5.5f, %5.5f)\n", estimatedLoc.getX(), estimatedLoc.getY());
		
//		double[][] diffGrid = tdoa.getTdoaDiffSumGrid(tdoaArray);
//
//		for(int yIndex = 0; yIndex < INITIAL_SAMPLES; yIndex++) {
//			for(int xIndex = 0; xIndex < INITIAL_SAMPLES; xIndex++) {
//				System.out.printf("%5.5f\t", diffGrid[xIndex][yIndex]);
//			}
//			System.out.println();
//		}
	}
}
