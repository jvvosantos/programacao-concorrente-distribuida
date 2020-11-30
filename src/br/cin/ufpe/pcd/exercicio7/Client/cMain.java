package br.cin.ufpe.pcd.exercicio7.Client;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.CountDownLatch;

public class cMain extends Thread {
	
	private Integer[] numClientArray;
	private Double[] avgTimeArray;
	private Double[] stdDeviationArray;
	
	private CountDownLatch latch;
	
	public cMain() {
		
	}
	
	public cMain(CountDownLatch latch) {
		this.latch = latch;
	}
	
	@Override
	public void run() {
		try {
			Integer[] Num_Clients = {2, 5, 10, 20};
			int ITERATIONS = 10000;

			CountDownLatch latch;
			this.numClientArray = Num_Clients;
			this.avgTimeArray = new Double[Num_Clients.length];
			this.stdDeviationArray = new Double[Num_Clients.length];
			for (int i = 0; i < Num_Clients.length; i++) {
				int n = Num_Clients[i];
				System.out.println("Running with " + n + " clients.");
				
				Cliente[] clients = new Cliente[n];
				latch = new CountDownLatch(n);
				
				clients[0] = new Cliente(ITERATIONS, true, latch);
				clients[0].start();
				for (int j = 1; j < n; j++) {
					clients[j] = new Cliente(ITERATIONS, false, latch);
					clients[j].start();
				}
				
				latch.await();
				
				this.avgTimeArray[i] = new BigDecimal(clients[0].getAvgTime()).setScale(7, RoundingMode.HALF_UP).doubleValue();
				this.stdDeviationArray[i] = new BigDecimal(clients[0].getStdDeviation()).setScale(7, RoundingMode.HALF_UP).doubleValue();
				
				System.out.println("avg time: "+clients[0].getAvgTime()+" ms");
				System.out.printf("std deviation: %.2f", clients[0].getStdDeviation());
				System.out.println();
			}	
			
			if (this.latch != null) {
				this.latch.countDown();
			}
		}
		catch(Exception e) {
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
		(new Thread(new cMain())).start();
	}

}
