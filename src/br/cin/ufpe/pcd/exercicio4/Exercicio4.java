package br.cin.ufpe.pcd.exercicio4;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

import br.cin.ufpe.pcd.exercicio2.tcp.TCPMain;
import br.cin.ufpe.pcd.exercicio2.udp.UDPMain;
import br.cin.ufpe.pcd.exercicio4.mq.RabbitMQMain;
import br.cin.ufpe.pcd.util.ChartSeries;
import br.cin.ufpe.pcd.util.GenericBarChart;

public class Exercicio4 {
	
	public static void main(String[] args) throws InterruptedException {
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
		
		RabbitMQMain rabbit = new RabbitMQMain(latch);
		Thread rabbitThread = new Thread(rabbit);
		rabbitThread.start();
		
		latch.await();
		
		GenericBarChart avgComparison = new GenericBarChart("Comparação socket TCP x UDP (média tempo)", "Num clients", "Tempo (ns)");
		ChartSeries udpSeries = new ChartSeries("UDP", udp.getNumClientArray(), udp.getAvgTimeArray());
		ChartSeries tcpSeries = new ChartSeries("TCP", tcp.getNumClientArray(), tcp.getAvgTimeArray());
		ChartSeries rabbitSeries = new ChartSeries("RABBIT", rabbit.getNumClientArray(), rabbit.getAvgTimeArray());
		avgComparison.setData(Arrays.asList(udpSeries, tcpSeries, rabbitSeries));
		avgComparison.show();
		
		GenericBarChart stdDeviationComparison = new GenericBarChart("Comparação socket TCP x UDP (desvio padrão)", "Num clients", "Desvio padrão");
		udpSeries = new ChartSeries("UDP", udp.getNumClientArray(), udp.getStdDeviationArray());
		tcpSeries = new ChartSeries("TCP", tcp.getNumClientArray(), tcp.getStdDeviationArray());
		rabbitSeries = new ChartSeries("RABBIT", rabbit.getNumClientArray(), rabbit.getStdDeviationArray());
		stdDeviationComparison.setData(Arrays.asList(udpSeries, tcpSeries, rabbitSeries));
		stdDeviationComparison.show();
	}

}
