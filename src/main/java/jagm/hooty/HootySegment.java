package jagm.hooty;

import net.minecraft.util.math.MathHelper;

public class HootySegment {

	private double x;
	private double y;
	private double z;
	private float yaw;
	private float pitch;

	public HootySegment(double x, double y, double z, float yaw, float pitch) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public float getYaw() {
		return yaw;
	}

	public float getPitch() {
		return pitch;
	}

	public double getDistance(double x2, double y2, double z2) {
		double f = this.x - x2;
		double f1 = this.y - y2;
		double f2 = this.z - z2;
		return MathHelper.sqrt(f * f + f1 * f1 + f2 * f2);
	}

}
