package simulador;
import eduni.simjava.*;        
import eduni.simjava.distributions.*;
/**
 ** Class responsible for creating events
 ** and sending them to the API MANAGER
 **/
class Source extends Sim_entity {
	private Sim_port outSource;
  private Sim_normal_obj delay;
  private final int NUMBER_EVENTS = 250;

  Source(String name, double mean, double variance, long seed) {
    super(name);
    this.delay = new Sim_normal_obj("Delay", mean, variance, seed);
    // All events received from the api manager will come from this port
    outSource = new Sim_port("outSource");
    add_port(outSource);
  }

  /**
   ** Creates events for the api manager
   **/
  public void body() {
    for (int i=0; i < NUMBER_EVENTS; i++) {
      sim_schedule(outSource, 0.0, 0);
      double delaySample = delay.sample();
      sim_trace(1, "Created new event for API Manager with delay -> " + delaySample);        
      sim_pause(delaySample);
    }
  }
}