package br.cin.ufpe.pcd.exercicio3;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

import br.cin.ufpe.pcd.exercicio2.tcp.TCPMain;
import br.cin.ufpe.pcd.exercicio2.udp.UDPMain;
import br.cin.ufpe.pcd.exercicio3.rmi.RMIMain;
import br.cin.ufpe.pcd.util.ChartSeries;
import br.cin.ufpe.pcd.util.GenericBarChart;

public class Exercicio3 {
	
	public static void main(String[] args) throws RemoteException, AlreadyBoundException, InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		
		System.out.println("## Getting TCP Results ##");
		TCPMain tcp = new TCPMain(latch);
		Thread tcpThread = new Thread(tcp);
		tcpThread.start();

		latch.await();
		latch = new CountDownLatch(1);

		System.out.println("## Getting UDP Results ##");
		UDPMain udp = new UDPMain(latch);
		Thread udpThread = new Thread(udp);
		udpThread.start();
		
		latch.await();
		latch = new CountDownLatch(1);
		
		System.out.println("## Getting RMI Results ##");
		RMIMain rmi = new RMIMain(latch);
		Thread rmiThread = new Thread(rmi);
		rmiThread.start();
		
		latch.await();
		
		GenericBarChart avgComparison = new GenericBarChart("Comparação socket TCP x UDP (média tempo)", "Num clients", "Tempo (ns)");
		ChartSeries udpSeries = new ChartSeries("UDP", udp.getNumClientArray(), udp.getAvgTimeArray());
		ChartSeries tcpSeries = new ChartSeries("TCP", tcp.getNumClientArray(), tcp.getAvgTimeArray());
		ChartSeries rmiSeries = new ChartSeries("RMI", rmi.getNumClientArray(), rmi.getAvgTimeArray());
		avgComparison.setData(Arrays.asList(udpSeries, tcpSeries, rmiSeries));
		avgComparison.show();
		
		GenericBarChart stdDeviationComparison = new GenericBarChart("Comparação socket TCP x UDP (desvio padrão)", "Num clients", "Desvio padrão");
		udpSeries = new ChartSeries("UDP", udp.getNumClientArray(), udp.getStdDeviationArray());
		tcpSeries = new ChartSeries("TCP", tcp.getNumClientArray(), tcp.getStdDeviationArray());
		rmiSeries = new ChartSeries("RMI", rmi.getNumClientArray(), rmi.getStdDeviationArray());
		stdDeviationComparison.setData(Arrays.asList(udpSeries, tcpSeries, rmiSeries));
		stdDeviationComparison.show();
	}

}
