package br.cin.ufpe.pcd.exercicio2.tcp;

import java.util.concurrent.CountDownLatch;

public class TCPMain extends Thread {
	
	private int[] numClientArray;
	private long[] avgTimeArray;
	private double[] stdDeviationArray;
	
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
			
			int[] numClientsArray = {2, 5, 10};
			this.numClientArray = numClientsArray;
			this.avgTimeArray = new long[numClientsArray.length];
			this.stdDeviationArray = new double[numClientsArray.length];
			
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
				this.avgTimeArray[i] = measuringClient.getAvgTime();
				this.stdDeviationArray[i] = measuringClient.getStdDeviation();
				
				System.out.println("avg time: "+measuringClient.getAvgTime()+" nanosec");
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
	
	public int[] getNumClientArray(){
		return this.numClientArray;
	}
	
	public long[] getAvgTimeArray(){
		return this.avgTimeArray;
	}
	
	public double[] getStdDeviationArray(){
		return this.stdDeviationArray;
	}
	
	public static void main(String[] args) throws InterruptedException {
		(new Thread(new TCPMain())).start();
	}

}
