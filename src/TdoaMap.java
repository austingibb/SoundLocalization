import java.util.List;

public class TdoaMap {
	private Point2D pos;
	private double width;
	private double individualWidth;
	private int samples;
	private Locations loc;
	private double speed;
	private double assumedHeight;
	private double[][][] tdoaGrid;
	private double[][][] positionGrid;
	
	public TdoaMap(Point2D pos, double width, int samples, Locations loc, double speed, double assumedHeight) {
		if(pos == null || loc == null || width == 0 || samples < 1) {
			throw new IllegalArgumentException();
		}
		this.pos = pos; this.width = width; this.samples = samples; this.loc = loc; this.speed = speed; this.assumedHeight = assumedHeight;
		
		individualWidth = width/(samples - 1);
		generateTdoaGrid();
	}
	
	public void generateTdoaGrid() {	
		double currentX = pos.getX();
		double currentY = pos.getY();
		
		tdoaGrid = new double[samples][samples][loc.size()];
		positionGrid = new double[samples][samples][2];
		Point3D reusedPoint = new Point3D(0, 0, assumedHeight);
		for(int yIndex = 0; yIndex < samples; yIndex++) {
			for(int xIndex = 0; xIndex < samples; xIndex++) {
				reusedPoint.setX(currentX);
				reusedPoint.setY(currentY);
				TdoaUtils.fillTdoaArray(reusedPoint, tdoaGrid[xIndex][yIndex], loc, speed);
				positionGrid[xIndex][yIndex][0] = currentX;
				positionGrid[xIndex][yIndex][1] = currentY;
				currentX += individualWidth;
			}
			currentX = pos.getX();
			currentY -= individualWidth;
		}
	}

	public double[][] getTdoaDiffSumGrid(List<Double> tdoas) {
		if(tdoas.size() != loc.size()) {
			throw new IllegalArgumentException();
		}
		
		double[][] tdoaDiffSumGrid = new double[samples][samples];
		for(int yIndex = 0; yIndex < samples; yIndex++) {
			for(int xIndex = 0; xIndex < samples; xIndex++) {
				double diffSum = 0;
				for(int tdoaIndex = 0; tdoaIndex < loc.size(); tdoaIndex++) {
					diffSum += Math.abs(tdoas.get(tdoaIndex) - tdoaGrid[xIndex][yIndex][tdoaIndex]);
				}
				
				tdoaDiffSumGrid[xIndex][yIndex] = diffSum;
			}
		}
		
		return tdoaDiffSumGrid;
	}
	
	public Point2D calculateEstimatedLocation(List<Double> tdoas) {
		double[][] tdoaDiffSumGrid = getTdoaDiffSumGrid(tdoas);
		double leastVal = tdoaDiffSumGrid[0][0];
		
		int leastIndexX = 0, leastIndexY = 0;
		for(int yIndex = 0; yIndex < samples; yIndex++) {
			for(int xIndex = 0; xIndex < samples; xIndex++) {
				double tdoaDiffSum = tdoaDiffSumGrid[xIndex][yIndex];
				if(tdoaDiffSum < leastVal) {
					leastVal = tdoaDiffSum;
					leastIndexX = xIndex;
					leastIndexY = yIndex;
				}
			}	
		}
		
		System.out.printf("%d, %d\n", leastIndexX, leastIndexY);

		return getPositionFromIndex(leastIndexX, leastIndexY);
	}
	
	public Point2D getPositionFromIndex(int x, int y) {
		return new Point2D(positionGrid[x][y][0], positionGrid[x][y][1]);
	}
	
	public double getIndividualWidth() {
		return individualWidth;
	}

	public double getSpeed() {
		return speed;
	}
	
	public double getAssumedHeight() {
		return assumedHeight;
	}
}
