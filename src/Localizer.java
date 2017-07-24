import java.util.List;

public class Localizer {
	private Locations loc;
	private TdoaMap largeMap;
	private List<Double> specificTdoas;
	private int zoomSamples;
	private int zoomTimes;
	private Point2D estimatedPoint;
	public Localizer(Locations loc, TdoaMap largeMap, List<Double> specificTdoas, int zoomSamples, int zoomTimes) {
		if(loc == null || largeMap == null || specificTdoas.size() < 3 || zoomSamples < 2 || zoomTimes < 0) {
			throw new IllegalArgumentException();
		}
		this.loc = loc; this.largeMap = largeMap; this.specificTdoas = specificTdoas; this.zoomSamples = zoomSamples; this.zoomTimes = zoomTimes;
		localize();
	}
	
	public void localize() {
		estimatedPoint = largeMap.calculateEstimatedLocation(specificTdoas);
		TdoaMap lastMap = largeMap;
		for(int zoomIndex = 0; zoomIndex < zoomTimes; zoomIndex++) {
			Point2D topLeftPoint = new Point2D(estimatedPoint.getX() - (lastMap.getIndividualWidth()/2),
					estimatedPoint.getY() + (lastMap.getIndividualWidth()/2));
			lastMap = new TdoaMap(topLeftPoint, lastMap.getIndividualWidth(), zoomSamples, loc, lastMap.getSpeed(), lastMap.getAssumedHeight());
			estimatedPoint = lastMap.calculateEstimatedLocation(specificTdoas);
			System.out.printf("(%5.5f, %5.5f)\n", estimatedPoint.getX(), estimatedPoint.getY());
		}
	}
	
	public Point2D getEstimatedPoint() {
		return estimatedPoint;
	}
}
