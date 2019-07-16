package simulador;

import eduni.simjava.*;
import eduni.simjava.distributions.*;
/**
 ** BD entity that shall be called by the Get/Post handler
 **/
class Bd extends Sim_entity {
  private Sim_port inputGetPost, outResponse;
  private Sim_normal_obj delay;
  private Sim_random_obj prob;
  Sim_stat stat;

  Bd(String name, double mean, double variance, long seed) {
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

    // Input for events coming from the Get/Post handler
    inputGetPost = new Sim_port("inputGetPost");
    
    // Output port to response entity
    outResponse = new Sim_port("outResponse");
    
    add_port(inputGetPost);
    add_port(outResponse);
  }
}