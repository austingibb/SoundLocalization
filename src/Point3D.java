
public class Point3D {
	private double x, y, z;
	public Point3D() {}
	public Point3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public static Point3D to3D(Point2D point, double z) {
		return new Point3D(point.getX(), point.getY(), z);
	}
	public static Point3D to3D(Point2D point) {
		return to3D(point, 0);
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getZ() {
		return z;
	}
	public void setZ(double z) {
		this.z = z;
	}
}
