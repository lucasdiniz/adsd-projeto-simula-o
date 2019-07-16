package simulador;

import eduni.simjava.*;
import eduni.simjava.distributions.*;
/**
 ** BD entity that shall be called by the Get/Post handler
 **/
class Response extends Sim_entity {
  private Sim_port inputBd, inputDisco, inputHead;
  private Sim_normal_obj delay;
  private Sim_random_obj prob;
  Sim_stat stat;

  Response(String name, double mean, double variance, long seed) {
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

    // Input for events coming from the bd/disk/head
    inputBd = new Sim_port("inputBd");
    inputDisco = new Sim_port("inputDisk");
    inputHead = new Sim_port("inputHead");
    
    add_port(inputBd);
    add_port(inputDisco);
    add_port(inputHead);
  }

 /**
  ** Creates a response and finishes
  **/
  public void body() {
    while (Sim_system.running()) {
      Sim_event e = new Sim_event();
      // get next event
      sim_get_next(e);
      // handle event
      double delaySample = delay.sample();
      sim_trace(1, "HEAD handler started with delay -> " + delaySample);
      sim_process(delaySample);
      // finished
      sim_completed(e);
      sim_trace(1, "Response created. FINISHED!");
  }
}
}