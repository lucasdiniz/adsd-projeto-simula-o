package simulador;

import eduni.simjava.*;
import eduni.simjava.distributions.*;
/**
 ** Handler for the Put/Delete that might be called by the 
 ** API Manager based on a probability distribution.
 **/
class PutDelete extends Sim_entity {
  private Sim_port inputApiManager, outDisk;
  private Sim_normal_obj delay;
  private Sim_random_obj prob;
  Sim_stat stat;

  PutDelete(String name, double mean, double variance, long seed) {
    super(name);
    this.delay = new Sim_normal_obj("Delay", mean, variance, seed);
    this.prob = new Sim_random_obj("Probability", seed);
    
    // Required stats = Utilization, service_time(tempo de repsosta)
    // waiting_time e queue_length (tamanho das filas)
    stat = new Sim_stat();        
    stat.add_measure(Sim_stat.QUEUE_LENGTH);    
    stat.add_measure(Sim_stat.SERVICE_TIME);   
    stat.add_measure(Sim_stat.UTILISATION);
    stat.add_measure(Sim_stat.WAITING_TIME);    
    set_stat(stat);    

    // Input for events coming from the api manager
    inputApiManager = new Sim_port("inputApiManager");
    
    // Output port to the disk enty
    outDisk = new Sim_port("outDisk");
    
    add_port(inputApiManager);
    add_port(outDisk);
  }

 /**
  ** Processes the event and calls the disk entity
  **/
  public void body() {
    while (Sim_system.running()) {
      Sim_event e = new Sim_event();
      // get next event
      sim_get_next(e);
      // handle event
      double delaySample = delay.sample();
      sim_trace(1, "PUT/DELETE handler started with delay -> " + delaySample);
      sim_process(delaySample);
      // finished
      sim_completed(e);
      
      sim_trace(1, "Calling the Disk...");
      sim_schedule(outDisk, 0.0, 1);
  }
}