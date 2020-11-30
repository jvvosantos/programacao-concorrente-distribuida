package br.cin.ufpe.pcd.exercicio2;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

import br.cin.ufpe.pcd.exercicio2.tcp.TCPMain;
import br.cin.ufpe.pcd.exercicio2.udp.UDPMain;
import br.cin.ufpe.pcd.util.ChartSeries;
import br.cin.ufpe.pcd.util.GenericBarChart;

public class Exercicio2 {
	
	public static void main(String[] args) throws InterruptedException, IOException {
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
		
		GenericBarChart avgComparison = new GenericBarChart("Comparação socket TCP x UDP (média tempo)", "Num clients", "Tempo (ns)");
		ChartSeries udpSeries = new ChartSeries("UDP", udp.getNumClientArray(), udp.getAvgTimeArray());
		ChartSeries tcpSeries = new ChartSeries("TCP", tcp.getNumClientArray(), tcp.getAvgTimeArray());
		avgComparison.setData(Arrays.asList(udpSeries, tcpSeries));
		avgComparison.show();
		
		GenericBarChart stdDeviationComparison = new GenericBarChart("Comparação socket TCP x UDP (desvio padrão)", "Num clients", "Desvio padrão");
		udpSeries = new ChartSeries("UDP", udp.getNumClientArray(), udp.getStdDeviationArray());
		tcpSeries = new ChartSeries("TCP", tcp.getNumClientArray(), tcp.getStdDeviationArray());
		stdDeviationComparison.setData(Arrays.asList(udpSeries, tcpSeries));
		stdDeviationComparison.show();
	}

}
