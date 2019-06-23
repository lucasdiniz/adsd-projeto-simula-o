package entities;

public class LoadBalancer extends Entity {

	public LoadBalancer(String name, double mean, double avg, int numInPorts, double[] probPorts) {
		super(name, mean, avg, numInPorts, probPorts);
	}
}