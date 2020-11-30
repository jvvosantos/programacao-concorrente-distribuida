package br.cin.ufpe.pcd.exercicio3.rmi;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.CountDownLatch;

import br.cin.ufpe.pcd.util.ChartSeries;
import br.cin.ufpe.pcd.util.Exporter;

public class RMIMain extends Thread {
	
	private Integer[] numClientArray;
	private Double[] avgTimeArray;
	private Double[] stdDeviationArray;
	
	private CountDownLatch latch;
	
	public RMIMain() {

	}
	
	public RMIMain(CountDownLatch latch) {
		this.latch = latch;
	}
	
	@Override
	public void run() {
		try {
			RMIServer server = new RMIServer();
			server.register();
			
			Integer[] numClientsArray = {2, 5, 10, 20};
			this.numClientArray = numClientsArray;
			this.avgTimeArray = new Double[numClientsArray.length];
			this.stdDeviationArray = new Double[numClientsArray.length];
			
			CountDownLatch latch;
			int numClients;
			for (int i = 0; i < numClientsArray.length; i++) {
				numClients = numClientsArray[i];
				latch = new CountDownLatch(numClients);

				System.out.println("Running with "+numClients+" clients");
				
				RMIClient measuringClient = new RMIClient(true, latch);
				Thread measuringClientThread = new Thread(measuringClient);
				measuringClientThread.start();
				
				for (int j = 1; j < numClients; j++) {
					RMIClient client = new RMIClient(true, latch);
					Thread clientThread = new Thread(client);
					clientThread.start();
				}
				
				latch.await();
				
				this.avgTimeArray[i] = new BigDecimal(measuringClient.getAvgTime()).setScale(7, RoundingMode.HALF_UP).doubleValue();
				this.stdDeviationArray[i] = new BigDecimal(measuringClient.getStdDeviation()).setScale(7, RoundingMode.HALF_UP).doubleValue();
				
				System.out.println("avg time: "+measuringClient.getAvgTime()+" ms");
				System.out.printf("std deviation: %.2f", measuringClient.getStdDeviation());
				System.out.println();
			}

			System.out.println("rmi client ended");
			server.unregister();
			
			if (this.latch != null) {
				this.latch.countDown();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Integer[] getNumClientArray(){
		return this.numClientArray;
	}
	
	public Double[] getAvgTimeArray(){
		return this.avgTimeArray;
	}
	
	public Double[] getStdDeviationArray(){
		return this.stdDeviationArray;
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		RMIMain main = new RMIMain(latch);
		(new Thread(main)).start();
		latch.await();
		
		ChartSeries avgSeries = new ChartSeries("RMI", main.getNumClientArray(), main.getAvgTimeArray());
		ChartSeries stdSeries = new ChartSeries("RMI", main.getNumClientArray(), main.getStdDeviationArray());
		
		Exporter.exportJson(avgSeries, "rmi_avg.json");
		Exporter.exportJson(stdSeries, "rmi_std.json");
	}

}
