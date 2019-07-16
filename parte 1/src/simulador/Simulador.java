package simulador;


import eduni.simjava.*;

/**
 ** Main class that runs the simulation
 **/
public class Simulador {
	public static void main(String[] args) {
      // Initialize Sim_system
      Sim_system.initialise();
      long seed = 666;
      // Create entities
      Source source = new Source("Source", 10, 3, seed);
      ApiManager apiManager = new ApiManager("ApiManager", 5, 2, seed);

      GetPost getPost = new GetPost("GetPost", 5, 1, seed);
      PutDelete putDelete = new PutDelete("PutDelete", 6, 1.5, seed);
      Head head = new Head("Head", 4, 1.3, seed);

      Bd bd = new Bd("Bd", 3, 0.2, seed);
      Disk disk = new Disk("Disk", 4, 0.2, seed);

      Response response = new Response("Response", 5, 1, seed); 
            
      // Link ports
      Sim_system.link_ports("Source", "outSource", "ApiManager", "inputSource");

      Sim_system.link_ports("ApiManager", "outPost", "GetPost", "inputApiManager");
      Sim_system.link_ports("ApiManager", "outDelete", "PutDelete", "inputApiManager");
      Sim_system.link_ports("ApiManager", "outHead", "Head", "inputApiManager");

      Sim_system.link_ports("GetPost", "outBd", "Bd", "inputGetPost");
      Sim_system.link_ports("PutDelete", "outDisk", "Disk", "inputPutDelete");
      Sim_system.link_ports("Head", "outResponse", "Response", "inputHead");

      Sim_system.link_ports("Bd", "outResponse", "Response", "inputBd");
      Sim_system.link_ports("Disk", "outResponse", "Response", "inputDisk");

      Sim_system.set_trace_detail(false, true, false);
      Sim_system.set_report_detail(true, false);
      Sim_system.run();
    }
  }