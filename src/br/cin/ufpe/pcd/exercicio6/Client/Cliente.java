package br.cin.ufpe.pcd.exercicio6.Client;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

import br.cin.ufpe.pcd.exercicio6.MyMiddleware.ClientProxy;
import br.cin.ufpe.pcd.exercicio6.Naming.NamingService;

public class Cliente extends Thread {
	private ClientProxy cp = null;
	private int iterationsNum = 0;
	private boolean measure = false;
	
	private double avgTime;
	private double stdDeviation;
	
	private CountDownLatch latch;
	
	public Cliente(int iterNum, boolean tmeasure, CountDownLatch latch) {
		//this.cp = new ClientProxy();
		this.cp = (ClientProxy)NamingService.Lookup("HelloClient");
		this.iterationsNum = iterNum;
		this.measure = tmeasure;
		this.latch = latch;
	}
	
	@Override
	public void run() {
		double[] measures = new double[iterationsNum];
		long startTime;
		for (int i = 0; i < this.iterationsNum; i++) {
			//System.out.println("Thread " + this.getId() + ": Saying Hello " + i);

			startTime = System.nanoTime();
			String str = cp.SayHello();
			measures[i] = (System.nanoTime() - startTime) / 1_000_000.00;
			
			if (str == null) {
				System.out.println("received null at it: " + (i+1) + " with threads " + Thread.activeCount());
				return;
			}
			
		}
		
		//System.out.println("Finished all " + i + " requests.");
		
		if (this.measure) {
			this.avgTime = Arrays.stream(measures).sum() / measures.length;

			double sum = 0;
			for (int i = 0; i < measures.length; i++) {
				sum += Math.pow((measures[i] - this.avgTime), 2);
			}

			this.stdDeviation = Math.sqrt((sum/measures.length));
		}
		
		latch.countDown();
		
	}

	public double getAvgTime() {
		return avgTime;
	}

	public double getStdDeviation() {
		return stdDeviation;
	}

	
}
