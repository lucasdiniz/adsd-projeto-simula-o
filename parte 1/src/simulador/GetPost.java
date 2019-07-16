package simulador;

import eduni.simjava.*;
import eduni.simjava.distributions.*;
/**
 ** Handler for the Get/Post that might be called by the 
 ** API Manager based on a probability distribution.
 **/
class GetPost extends Sim_entity {
  private Sim_port inputApiManager, outBd;
  private Sim_normal_obj delay;
  private Sim_random_obj prob;
  Sim_stat stat;

  GetPost(String name, double mean, double variance, long seed) {
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
    
    // Output port to the bd entity 
    outBd = new Sim_port("outBd");
    
    add_port(inputApiManager);
    add_port(outBd);
  }
}