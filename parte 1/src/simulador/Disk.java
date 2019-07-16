package simulador;

import eduni.simjava.*;
import eduni.simjava.distributions.*;
/**
 ** Disk entity that shall be called by the Put/Delete handler
 **/
class Disk extends Sim_entity {
  private Sim_port inputPutDelete, outResponse;
  private Sim_normal_obj delay;
  private Sim_random_obj prob;
  private final double PUT_TIME = 0.1;
  private final double DELETE_TIME = 0.1;
  Sim_stat stat;

  Disk(String name, double mean, double variance, long seed) {
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

    // Input for events coming from the Put/Delete handler
    inputPutDelete = new Sim_port("inputPutDelete");
    
    // Output port to response entity
    outResponse = new Sim_port("outResponse");
    
    add_port(inputPutDelete);
    add_port(outResponse);
  }

 /**
  ** Performes a put/delete based on a probability distribution
  **/
  public void body() {
    while (Sim_system.running()) {
      Sim_event e = new Sim_event();
      // get next event
      sim_get_next(e);
      // handle event
      double delaySample = delay.sample();
      sim_trace(1, "Disk started with delay -> " + delaySample);
      sim_process(delaySample);
      // finished
      sim_completed(e);
      
      double probSample = prob.sample();
      if (probSample < 0.5) {
        sim_trace(1, "DISK -> Put");
        sim_schedule(outResponse, PUT_TIME, 1);
      } else {
        sim_trace(1, "DISK -> Delete");
        sim_schedule(outResponse, DELETE_TIME, 1);
      }       
    }
  }
}