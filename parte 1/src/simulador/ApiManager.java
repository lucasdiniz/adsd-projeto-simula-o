package simulador;

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
    inputSource = new Sim_port("inputSource");
    
    // Output ports to the handlers    
    outPost = new Sim_port("outPost");    
    outDelete = new Sim_port("outDelete");
    outHead = new Sim_port("outHead");
    
    add_port(inputSource);
    add_port(outPost);
    add_port(outDelete);
    add_port(outHead);
  }

 /**
  ** Calls the handlers based on a probability distribution
  **/
  public void body() {
    while (Sim_system.running()) {
      Sim_event e = new Sim_event();
      // get next event
      sim_get_next(e);
      // handle event
      double delaySample = delay.sample();
      sim_trace(1, "API manager service started with delay -> " + delaySample);
      sim_process(delaySample);
      // finished
      sim_completed(e);
      
      double probSample = prob.sample();
      if (probSample < 0.333) {
        sim_trace(1, "Calling the GET/POST Handler...");
        sim_schedule(outPost, 0.0, 1);
      } else if (probSample < 0.666) {
        sim_trace(1, "Calling the PUT/DELETE Handler...");
        sim_schedule(outDelete, 0.0, 1);
      } else if (probSample < 0.999) {
        sim_trace(1, "Calling the HEAD Handler...");
        sim_schedule(outHead, 0.0, 1);
      }      
    }
  }
}