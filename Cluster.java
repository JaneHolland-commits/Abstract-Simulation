

import java.util.ArrayList;

public class Cluster {
	
	private int ClusterNumber;
	
	private int NAgents;///In Cluster
	private double[] Center = new double[2];
	private double AvDist;
	
	
	ArrayList<Agent> Agents = new ArrayList<Agent>();
	
	public Cluster(int Number){
		ClusterNumber = Number;
	}
	
	
	public void addAgent(Agent NewAgent){
		Agents.add(NewAgent);
		NAgents++;
	}
	
	public int getClusterNumber(){
		return ClusterNumber;
	}
	
	public void displayAgents(){
		System.out.print("\n Cluster: "+ClusterNumber+"Size: "+NAgents+". Agents: ");
		for(Agent CurrentAgent : Agents){
			System.out.print(CurrentAgent.getAgentNumber()+",");
		}
		System.out.println("");
	}
	
	public ArrayList<Agent> getAgents(){
		return Agents;
	}
	
	public double getAverageDistanceFromNeighbours(){
		double[] AgentAverages =new double[NAgents];
		
		int Agent =0;
		for(Agent CurrentAgent1:Agents){
			for(Agent CurrentAgent2:Agents){////ReturnsZero for self so no need to ignore
				AgentAverages[Agent] += MyFunctions.calcDistance(CurrentAgent1.getXLocation(), CurrentAgent1.getYLocation(), CurrentAgent2.getXLocation(), CurrentAgent2.getYLocation());
			}
			AgentAverages[Agent]=AgentAverages[Agent]/(double)(NAgents-1);
			Agent++;
		}
		double Average=0;
		for(Agent=0; Agent<NAgents;Agent++){
			Average += AgentAverages[Agent];
		}
		
		//System.out.println();
		
		Average = Average/(double)NAgents;
		//System.out.println("Average:"+Average);
		////In GraphUnits
		///Convet to CM
		Average = Average;// * Parameters.SpacingDistance;
		return Average;
	}
	
	private void findCenter(){
		
		double XAvg=0;
		double YAvg=0;
		
		for(Agent CurrentAgent : Agents){
			XAvg += CurrentAgent.getXLocation();
			YAvg += CurrentAgent.getYLocation(); 
		}
		
		XAvg = XAvg/NAgents;
		YAvg = YAvg/NAgents;
		Center[0] = XAvg;
		Center[1] = YAvg;
		
	}
	
	
	private double distFromCenter(double X1, double Y1){
		double Xp2;
		double Yp2;
		double Dist;
		
		Xp2 = (X1 - Center[0])*(X1 - Center[0]);
		Yp2 = (Y1 - Center[1])*(Y1 - Center[1]);
		
		Dist = Math.sqrt(Xp2+Yp2);
		return Dist;
		
	}///End distFromCent
	
	public double getAvgDist(){
		double AvgDist=0;
		findCenter();
		
		for(Agent CurrentAgent : Agents){
			AvgDist += distFromCenter(CurrentAgent.getXLocation(),CurrentAgent.getYLocation());							
		}
		
		
		
		AvgDist = AvgDist / NAgents;
		///convert to meters;
		AvgDist = AvgDist*(Parameters.SpacingDistance);
		
		AvDist = AvgDist; ///Bad form.
		
		
		return AvgDist;
		
	}
	
	public void displayInfo(){
		System.out.println("Cluster: "+ ClusterNumber+". Agents: "+NAgents+". Center ("+Center[0]+","+Center[1]+"). AvDist: "+AvDist+".");
	}
	
	public int getSize(){
		return NAgents;
	}
	
	public double[] getCenter(){
		return Center;
	}
	
	
	
}