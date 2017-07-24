import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TdoaTest {
	@Test
	public void distanceTest() {
		Point2D p1 = new Point2D(0.2, 0.3);
		Point2D p2 = new Point2D(0.6, 0.7);
		double distance = Tdoa.distance(p1, p2);
		assertEquals(0.5656, distance, .0001);
	}
	
	@Test
	public void getTdoaAtTest() {
		Point2D p1 = new Point2D(0, 0);
		Point2D p2 = new Point2D(1.0, 0);
		Point2D source = new Point2D(0.2, 0.3);
		double tdoa = Tdoa.getTdoaAt(p1, p2, source, 747.9);
		assertEquals(6.60309e-4, tdoa, 0.0001);
	}
	
	@Test
	public void perimeterTest() {
		Point2D p1 = new Point2D(-1.0, 0);
		Point2D p2 = new Point2D(0.0, 1.0);
		Point2D p3 = new Point2D(0.5, 0);
		List<Point2D> points = new ArrayList<Point2D>();
		points.add(p1);
		points.add(p2);
		points.add(p3);
		double perimeter = Tdoa.perimeter(points);
		assertEquals(4.0322, perimeter, .0001);
	}
	
//	@Test
//	public void getTdoaDiffSumZeroTest() {
//		Point2D p1 = new Point2D(-1.0, 0);
//		Point2D p2 = new Point2D(1.0, 0);
//		Point2D p3 = new Point2D(0, 1.0);
//		Point2D source = new Point2D(0.1, 0.5);
//		double tdoaDiffSum = Localization.getTdoaDiffSum(p1, p2, p3, source, -2.389912e-4, -6.948269e-4, -9.338182e-4, 747.9);
//		assertEquals(0.0, tdoaDiffSum, 0.0001);
//	}
//
//	@Test
//	public void getTdoaDiffSumTest() {
//		Point2D p1 = new Point2D(-1.0, 0);
//		Point2D p2 = new Point2D(1.0, 0);
//		Point2D p3 = new Point2D(0, 1.0);
//		Point2D source = new Point2D(-0.2, 0.3);
//		double tdoaDiffSum = Localization.getTdoaDiffSum(p1, p2, p3, source, -2.389912e-4, -6.948269e-4, -9.338182e-4, 747.9);
//		assertEquals(1.5296515e-3, tdoaDiffSum, 0.00001);
//	}
}
