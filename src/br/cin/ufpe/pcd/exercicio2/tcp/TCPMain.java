package br.cin.ufpe.pcd.exercicio2.tcp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.CountDownLatch;

public class TCPMain extends Thread {
	
	private Integer[] numClientArray;
	private Double[] avgTimeArray;
	private Double[] stdDeviationArray;
	
	private CountDownLatch latch;
	
	public TCPMain() {

	}
	
	public TCPMain(CountDownLatch latch) {
		this.latch = latch;
	}

	@Override
	public void run() {
		try {
			TCPServer server = new TCPServer();
			Thread serverThread = new Thread(server);
			serverThread.start();
			
			Integer[] numClientsArray = {2, 5, 10};
			this.numClientArray = numClientsArray;
			this.avgTimeArray = new Double[numClientsArray.length];
			this.stdDeviationArray = new Double[numClientsArray.length];
			
			CountDownLatch latch;
			int numClients;
			for (int i = 0; i < numClientsArray.length; i++) {
				numClients = numClientsArray[i];
				latch = new CountDownLatch(numClients);
				
				System.out.println("Running with "+numClients+" clients");

		        TCPClient measuringClient = new TCPClient(latch, true);
				Thread measuringClientThread = new Thread(measuringClient);
				measuringClientThread.start();
				
				for (int j = 1; j < numClients; j++) {
			        TCPClient client = new TCPClient(latch, false);
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
			
			server.shutdown();
			
			if (this.latch != null){
				this.latch.countDown();
			}
		}
		catch (InterruptedException e){
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
	
	public static void main(String[] args) throws InterruptedException {
		(new Thread(new TCPMain())).start();
	}

}
