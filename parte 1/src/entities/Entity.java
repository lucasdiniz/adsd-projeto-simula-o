package entities;

import eduni.simjava.*;
import eduni.simjava.distributions.Sim_normal_obj;
import eduni.simjava.distributions.Sim_random_obj;

public abstract class Entity extends Sim_entity {

	private Sim_stat stat;
	private Sim_normal_obj delay;

	private double[] probPorts;
	private int numInPorts;

	protected Sim_port[] inPorts;
	protected Sim_port[] outPorts;

	private String entityName;
	
	private Pair<Double, Double>[] probRanges;

	protected final Sim_random_obj randomProb;

	public Entity(String name, double mean, double avg, int numInPorts, double[] probPorts) {
		super(name);

        stat = new Sim_stat();
        stat.add_measure(Sim_stat.UTILISATION);
        stat.add_measure(Sim_stat.SERVICE_TIME);
        stat.add_measure(Sim_stat.WAITING_TIME);
        stat.add_measure(Sim_stat.QUEUE_LENGTH);
        set_stat(stat);

        delay = new Sim_normal_obj(name.concat("_Delay"), mean, avg);
		add_generator(delay);

		randomProb = new Sim_random_obj(name.concat("_RandomProbability"));
		add_generator(delay);

		this.entityName = name;
		this.numInPorts = numInPorts;
		this.probPorts = probPorts;
		
		initializePorts();
		defineProbRanges();
	}

	private Sim_port[] createPorts(String name, int numberOfPorts) {
		Sim_port[] ports = new Sim_port[numberOfPorts];
		for (int i = 0; i < numberOfPorts; i++) {
			ports[i] = new Sim_port(name.concat(Integer.toString(i + 1)));
			System.out.println(name.concat(Integer.toString(i + 1)));
			add_port(ports[i]);
		}
		return ports;
	}

	public double sample() {
		return delay.sample();
	}
	
	public Pair<Double, Double>[] getProbRanges() {
		return probRanges;
	}

	public double[] getProbPorts() {
		return probPorts;
	}

	public void setProbPorts(double[] newProbPorts) {
		this.probPorts = newProbPorts;
	}
	
	private void initializePorts() {
		this.inPorts = createPorts(entityName.concat("In"), numInPorts);
		this.outPorts = createPorts(entityName.concat("Out"), probPorts.length);
	}
	
	protected void defineProbRanges() {
		this.probRanges = (Pair<Double, Double>[]) new Pair[probPorts.length];
		
		double acumulatedProb = 0.0;
		for (int i = 0; i < probPorts.length; i++) {
			Pair<Double, Double> probRange = new Pair<Double, Double>(acumulatedProb, acumulatedProb + probPorts[i]);
			acumulatedProb += probPorts[i];
			probRanges[i] = probRange;
		}
	}

	public void bodyInsideLoop() {
		Sim_event e = new Sim_event();
		sim_get_next(e);
		sim_process(sample());
		sim_completed(e);
		
		double p = randomProb.sample();
		
		Pair<Double, Double>[] probRanges = getProbRanges();
		for (int i = 0; i < probRanges.length; i++) {
			if (p > probRanges[i].f && p <= probRanges[i].s) {
				sim_schedule(outPorts[i], 0.0, 0);
			}
		}
	}
	
	@Override
	public void body() {
		while(Sim_system.running()){
			bodyInsideLoop();
		}
	}
	
	class Pair<X, Y> {
		public final X f;
		public final Y s;

		public Pair(X x, Y y) {
			this.f = x;
			this.s = y;
		}
	}
}