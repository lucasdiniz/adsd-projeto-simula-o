import eduni.simjava.Sim_system;
import eduni.simjava.distributions.Sim_random_obj;
import entities.*;

public class Main {
	
	public static void main(String[] args) {
		Sim_system.initialise();
		
		// lambda = 5 req/sec = each 0.2 sec
		Source source = new Source("Source", 0.2, 0.00001);

		CPU webServerCpu = new CPU("WebServerCpu", 0.5, 0.00002, 2, new double[]{0.7, 0.3});
		Disk webServerDisk = new Disk("WebServerDisk", 0.85, 0.0002, 1);
		
		CPU appServerCpu = new CPU("AppServerCpu", 0.5, 0.00002, 2, new double[]{0.7, 0.3});
		Disk appServerDisk = new Disk("AppServerDisk", 0.85, 0.0002, 1);
		

		LoadBalancer loadBalancer =  new LoadBalancer("LoadBalancer", 0.5, 0.00002, 1, new double[]{0.5, 0.5});

		CPU cpuDatabaseServer1 = new CPU("DatabaseServer1Cpu", 0.5, 0.00002, 1, new double[] {}); 
		CPU cpuDatabaseServer2 = new CPU("DatabaseServer2Cpu", 0.5, 0.00002, 1, new double[] {});
		
		//SOURCE
		Sim_system.link_ports("Source", "SourceOut1", "WebServerCpu", "WebServerCpuIn1");

		// WEB SERVER
		Sim_system.link_ports("WebServerCpu", "WebServerCpuOut1", "AppServerCpu", "AppServerCpuIn1");
		Sim_system.link_ports("WebServerCpu", "WebServerCpuOut2", "WebServerDisk", "WebServerDiskIn1");

		Sim_system.link_ports("WebServerDisk", "WebServerDiskOut1", "WebServerCpu", "WebServerCpuIn2");

		// APP SERVER
		Sim_system.link_ports("AppServerCpu", "AppServerCpuOut1", "LoadBalancer", "LoadBalancerIn1");
		Sim_system.link_ports("AppServerCpu", "AppServerCpuOut2", "AppServerDisk", "AppServerDiskIn1");

		Sim_system.link_ports("AppServerDisk", "AppServerDiskOut1", "AppServerCpu", "AppServerCpuIn2");

		// LOAD BALANCER
		Sim_system.link_ports("LoadBalancer", "LoadBalancerOut1", "DatabaseServer1Cpu", "DatabaseServer1CpuIn1");
		Sim_system.link_ports("LoadBalancer", "LoadBalancerOut2", "DatabaseServer2Cpu", "DatabaseServer2CpuIn1");
		
		Sim_system.run();
	}

}