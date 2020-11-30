package br.cin.ufpe.pcd.util;

import java.util.Arrays;

public class Analysis {
	
	public static void main(String[] args) throws Exception {
		
		GenericBarChart avgComparison = new GenericBarChart("Comparação socket TCP x UDP (média tempo)", "Num clients", "Tempo (ns)");
		ChartSeries udpAvgSeries = Exporter.readJson("udp_avg.json");
		ChartSeries tcpAvgSeries = Exporter.readJson("tcp_avg.json");
		ChartSeries rmiAvgSeries = Exporter.readJson("rmi_avg.json");
		ChartSeries rabbitAvgSeries = Exporter.readJson("rbt_avg.json");
		ChartSeries rpcAvgSeries = Exporter.readJson("rpc_avg.json");
		ChartSeries poolingRpcAvgSeries = Exporter.readJson("prpc_avg.json");
		avgComparison.setData(Arrays.asList(
											udpAvgSeries, 
											tcpAvgSeries, 
											rmiAvgSeries, 
											rabbitAvgSeries, 
											rpcAvgSeries, 
											poolingRpcAvgSeries));
		avgComparison.show();
		
		GenericBarChart stdComparison = new GenericBarChart("Comparação socket TCP x UDP (desvio padrão)", "Num clients", "Desvio padrão");
		ChartSeries udpStdSeries = Exporter.readJson("udp_std.json");
		ChartSeries tcpStdSeries = Exporter.readJson("tcp_std.json");
		ChartSeries rmiStdSeries = Exporter.readJson("rmi_std.json");
		ChartSeries rabbitStdSeries = Exporter.readJson("rbt_std.json");
		ChartSeries rpcStdSeries = Exporter.readJson("rpc_std.json");
		ChartSeries poolingRpcStdSeries = Exporter.readJson("prpc_std.json");
		stdComparison.setData(Arrays.asList(
											udpStdSeries, 
											tcpStdSeries, 
											rmiStdSeries, 
											rabbitStdSeries, 
											rpcStdSeries, 
											poolingRpcStdSeries));
		stdComparison.show();
	}

}
