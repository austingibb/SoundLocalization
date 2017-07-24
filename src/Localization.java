import java.util.List;

public class Localization {
	private Tdoa calculatedTdoas;
	private List<Double> dataTdoas;
	private double[][] tdoaDiffSumGrid;
	private int leastIndexX = -1, leastIndexY = -1;
	private Point2D estimatedLocation;
	public Localization(Tdoa calculatedTdoas, List<Double> dataTdoas) {
		if(calculatedTdoas.size() != dataTdoas.size()) {
			throw new IllegalArgumentException();
		}
		this.calculatedTdoas = calculatedTdoas; this.dataTdoas = dataTdoas;
		tdoaDiffSumGrid = calculatedTdoas.getTdoaDiffSumGrid(dataTdoas);
		calculateEstimatedLocation();
	}
	
	public void calculateEstimatedLocation() {
		double leastVal = tdoaDiffSumGrid[0][0];
		
		for(int yIndex = 0; yIndex < Tdoa.INITIAL_SAMPLES; yIndex++) {
			for(int xIndex = 0; xIndex < Tdoa.INITIAL_SAMPLES; xIndex++) {
				double tdoaDiffSum = tdoaDiffSumGrid[xIndex][yIndex];
				if(tdoaDiffSum < leastVal) {
					leastVal = tdoaDiffSum;
					leastIndexX = xIndex;
					leastIndexY = yIndex;
				}
			}	
		}
		
		estimatedLocation = calculatedTdoas.getPositionFromIndex(leastIndexX, leastIndexY);
	}
	
	public Point2D getEstimatedLocation() {
		return estimatedLocation;
	}
}
