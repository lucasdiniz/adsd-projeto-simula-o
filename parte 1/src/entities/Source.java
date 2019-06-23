package entities;
import eduni.simjava.Sim_system;
import eduni.simjava.distributions.Sim_uniform_obj;

public class Source extends Entity { // The class for the source

	private int previousSimClock;

	public Source(String name, double mean, double avg) {
		super(name, mean, avg, 0, new double[] { 1.0 });
		this.previousSimClock = -1;
	}


	@Override
	public void body() {
		for (int i = 0; i < 1000; i++) {
			sim_schedule(outPorts[0], 0.0, 0);
			sim_pause(sample());
		}
	}
}