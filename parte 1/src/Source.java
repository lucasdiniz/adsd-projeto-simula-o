package simulador;
import eduni.simjava.*;        
import eduni.simjava.distributions.*;

class Source extends Sim_entity {
	private Sim_port outSource;
  private Sim_normal_obj delay;

  Source(String name, double mean, double variance, long seed) {
    super(name);
    this.delay = new Sim_normal_obj("Delay", mean, variance, seed);
    // All events received from the api manager will come from this port
    outSource = new Sim_port("outSource");
    add_port(out);
  }
}