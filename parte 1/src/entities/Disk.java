package entities;

public class Disk extends Entity {
	public Disk(String name, double mean, double avg, int numInPorts) {
		super(name, mean, avg, numInPorts, new double[]{1.0});
	}
}