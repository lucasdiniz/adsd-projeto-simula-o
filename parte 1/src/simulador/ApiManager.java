package simulator;

import eduni.simjava.*;
import eduni.simjava.distributions.*;
/**
 ** Api manager that will handle requests and forward it to one of 3 handlers
 ** (get/post, put/delete and head)
 **/
class ApiManager extends Sim_entity {
  private Sim_port inputSource, outPost, outDelete, outHead;
  private Sim_normal_obj delay;
  private Sim_random_obj prob;
  Sim_stat stat;

  ApiManager(String name, double mean, double variance, long seed) {
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

    // Shall receive events from source entity
    inputSource = new Sim_port("In");
    
    // Output ports to the handlers    
    outPost = new Sim_port("outPost");    
    outDelete = new Sim_port("outDelete");
    outHead = new Sim_port("outHead");
    
    add_port(in);
    add_port(outPost);
    add_port(outDelete);
    add_port(outHead);
  }
}