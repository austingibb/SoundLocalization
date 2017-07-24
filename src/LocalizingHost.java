import java.util.ArrayList;
import java.util.List;

public class LocalizingHost {
	private List<Point2D> calculatedPoints;
	private TdoaMap largeMap;
	private double assumedHeight;
	private double searchAreaScalar;
	private int largeMapSamples;
	private int zoomSamples;
	private int zoomTimes;
	private double speed;
	private Locations loc;
	
	public LocalizingHost(double assumedHeight, double searchAreaScalar, double speed, int largeMapSamples, int zoomSamples, int zoomTimes, Locations loc) {
		this.assumedHeight = assumedHeight; this.searchAreaScalar = searchAreaScalar; this.speed = speed;
		this.largeMapSamples = largeMapSamples; this.zoomSamples = zoomSamples; this.zoomTimes = zoomTimes; this.loc = loc;
	
		calculatedPoints = new ArrayList<>();
		createLargeMap();
	}
	
	public void createLargeMap() {
		Point2D center = TdoaUtils.centerOfPoints(loc);
		double width = (loc.getPerimeter()) * searchAreaScalar;
		Point2D topLeftCorner = new Point2D(center.getX() - (width/2), center.getY() + (width/2));
		
		largeMap = new TdoaMap(topLeftCorner, width, largeMapSamples, loc, speed, assumedHeight);
	}
	
	public void addCalculatedPoint(List<Double> tdoas) {
		Localizer localizer = new Localizer(loc, largeMap, tdoas, zoomSamples, zoomTimes);
		Point2D estPoint = localizer.getEstimatedPoint();
		System.out.printf("\n(%5.5f, %5.5f)\n\n", estPoint.getX(), estPoint.getY());
		calculatedPoints.add(localizer.getEstimatedPoint());
	}
	
	public static void main(String[] args) {
		Locations loc = new Locations(new Point2D(-1, 0), new Point2D(0, 1), new Point2D(1, 0));
		LocalizingHost lHost = new LocalizingHost(0.0, 1.035533906, 747.9, 1000, 20, 2, loc);
		
		List<Double> tdoaArray = new ArrayList<Double>();
		TdoaUtils.fillTdoaArray(new Point3D(2, -1, 0), tdoaArray, loc, 747.9);
		
		lHost.addCalculatedPoint(tdoaArray);
	}
}
