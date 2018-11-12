

public class Results {
	
	public static double[][] NAgentsClusterdArray;////Generation Iteration
	public static double[][] AvgNNeighboursArray;
	public static double[][] AvgNNeighboursClusteredArray;
	public static double[][] NClustersArray;
	public static double[][] AvgClusterSizeArray;
	
	public static double[][] Genomes;
	public static double[] GroupFitness;
	
	public static double[][][][] ClusterInfo; ///[Gen][Iter][ClusterNumber][Value] Value(0-CNumber, 1-CZize, 2-CentX, 3-CentY, 4-Dist, 5-Avg NeighbourDist)
	public static double[][][][]  AgentsinCluster; ///[Gen][Iter][ClusterNumber][Agent]
	
	public static double[][] RunResults; //[Run][Value](0-Largest, 1-NClusters)
	
	public static double[][][] RunningIterAverage;//[Gen][Iter][Value](0-NC)(1-AvgSize)(2-AvgDist)
	public static double[][] RunningGenAverage; //[Gen][Value](0-Score)(1-NC)(2-AvgSize)(3-AvgDist)
	
}