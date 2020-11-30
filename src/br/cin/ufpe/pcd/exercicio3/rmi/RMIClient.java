package br.cin.ufpe.pcd.exercicio3.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class RMIClient extends Thread {

	private static RemoteInterface look_up;

	private boolean measure;
	private double avgTime;
	private double stdDeviation;

	private CountDownLatch latch;

	public RMIClient() {

	}

	public RMIClient(boolean measure, CountDownLatch latch) {
		this.measure = measure;
		this.latch = latch;
	}

	@Override
	public void run() {
		try {
			Registry registry = LocateRegistry.getRegistry();

			look_up = (RemoteInterface) registry.lookup("RMIServer");
			
			if (this.measure) {
				this.sendMessagesMeasure();
			}
			else {
				this.sendMessages();
			}

			this.latch.countDown();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sendMessagesMeasure() throws RemoteException {
		String msg = "Oi, quer conversar?";		
		int tries = 10000;
		double[] measures = new double[tries];
		long startTime;
		for (int i = 0; i < tries; i++) {
			startTime = System.nanoTime();
			look_up.helloTo(msg);
			measures[i] = (System.nanoTime() - startTime) / 1_000_000.00;
		}

		this.avgTime = Arrays.stream(measures).sum() / measures.length;

		double sum = 0;
		for (int i = 0; i < measures.length; i++) {
			sum += Math.pow((measures[i] - this.avgTime), 2);
		}

		this.stdDeviation = Math.sqrt((sum/measures.length));

	}

	private void sendMessages() throws RemoteException {
		String msg = "Oi, quer conversar?";		
		int tries = 10000;
		for (int i = 0; i < tries; i++) {
			look_up.helloTo(msg);
		}
	}

	public double getAvgTime() {
		return avgTime;
	}

	public double getStdDeviation() {
		return stdDeviation;
	}

}
