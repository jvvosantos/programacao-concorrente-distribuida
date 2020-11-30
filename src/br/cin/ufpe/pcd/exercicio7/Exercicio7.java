package br.cin.ufpe.pcd.exercicio7;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

import br.cin.ufpe.pcd.exercicio2.tcp.TCPMain;
import br.cin.ufpe.pcd.exercicio2.udp.UDPMain;
import br.cin.ufpe.pcd.exercicio3.rmi.RMIMain;
import br.cin.ufpe.pcd.exercicio4.mq.RabbitMQMain;
import br.cin.ufpe.pcd.exercicio6.Client.cMain;
import br.cin.ufpe.pcd.exercicio6.Naming.NamingService;
import br.cin.ufpe.pcd.exercicio6.Server.sMain;
import br.cin.ufpe.pcd.util.ChartSeries;
import br.cin.ufpe.pcd.util.GenericBarChart;

public class Exercicio7 {
	
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

		System.out.println("## Getting RMI Results ##");
		RMIMain rmi = new RMIMain(latch);
		Thread rmiThread = new Thread(rmi);
		rmiThread.start();

		latch.await();
		latch = new CountDownLatch(1);

		System.out.println("## Getting RABBIT Results ##");
		RabbitMQMain rabbit = new RabbitMQMain(latch);
		Thread rabbitThread = new Thread(rabbit);
		rabbitThread.start();

		latch.await();
		latch = new CountDownLatch(1);

		System.out.println("## Getting Ex6 Results ##");
		new Thread(new Runnable() {
			public void run() {
				NamingService.main(null);           
			}
		}).start();

		Thread.sleep(100);

		new Thread(new Runnable() {
			public void run() {
				sMain.main(null);  
			}
		}).start();

		Thread.sleep(100);

		cMain ex6 = new cMain(latch);
		Thread ex6Thread = new Thread(ex6);
		ex6Thread.start();

		latch.await();
		latch = new CountDownLatch(1);
		
		System.out.println("## Getting Ex7 Results ##");
		new Thread(new Runnable() {
			public void run() {
				br.cin.ufpe.pcd.exercicio7.Naming.NamingService.main(null);           
			}
		}).start();

		Thread.sleep(100);

		new Thread(new Runnable() {
			public void run() {
				br.cin.ufpe.pcd.exercicio7.Server.sMain.main(null);  
			}
		}).start();

		Thread.sleep(100);

		br.cin.ufpe.pcd.exercicio7.Client.cMain ex7 = new br.cin.ufpe.pcd.exercicio7.Client.cMain(latch);
		Thread ex7Thread = new Thread(ex7);
		ex7Thread.start();
		
		latch.await();

		GenericBarChart avgComparison = new GenericBarChart("Comparação socket TCP x UDP (média tempo)", "Num clients", "Tempo (ns)");
		ChartSeries udpSeries = new ChartSeries("UDP", udp.getNumClientArray(), udp.getAvgTimeArray());
		ChartSeries tcpSeries = new ChartSeries("TCP", tcp.getNumClientArray(), tcp.getAvgTimeArray());
		ChartSeries rmiSeries = new ChartSeries("RMI", rmi.getNumClientArray(), rmi.getAvgTimeArray());
		ChartSeries rabbitSeries = new ChartSeries("RABBIT", rabbit.getNumClientArray(), rabbit.getAvgTimeArray());
		ChartSeries ex6Series = new ChartSeries("Our RPC", ex6.getNumClientArray(), ex6.getAvgTimeArray());
		ChartSeries ex7Series = new ChartSeries("Our Pooling RPC", ex7.getNumClientArray(), ex7.getAvgTimeArray());
		avgComparison.setData(Arrays.asList(udpSeries, tcpSeries, rmiSeries, rabbitSeries, ex6Series, ex7Series));
		avgComparison.show();

		GenericBarChart stdDeviationComparison = new GenericBarChart("Comparação socket TCP x UDP (desvio padrão)", "Num clients", "Desvio padrão");
		udpSeries = new ChartSeries("UDP", udp.getNumClientArray(), udp.getStdDeviationArray());
		tcpSeries = new ChartSeries("TCP", tcp.getNumClientArray(), tcp.getStdDeviationArray());
		rmiSeries = new ChartSeries("RMI", rmi.getNumClientArray(), rmi.getStdDeviationArray());
		rabbitSeries = new ChartSeries("RABBIT", rabbit.getNumClientArray(), rabbit.getStdDeviationArray());
		ex6Series = new ChartSeries("Our RPC", ex6.getNumClientArray(), ex6.getStdDeviationArray());
		ex7Series = new ChartSeries("Our Pooling RPC", ex7.getNumClientArray(), ex7.getStdDeviationArray());
		stdDeviationComparison.setData(Arrays.asList(udpSeries, tcpSeries, rmiSeries, rabbitSeries, ex6Series, ex7Series));
		stdDeviationComparison.show();
	}

}
