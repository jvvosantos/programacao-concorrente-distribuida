package br.cin.ufpe.pcd.exercicio2.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class TCPClient implements Runnable {

	private CountDownLatch latch;

	private boolean measure;
	private long avgTime;
	private double stdDeviation;

	public TCPClient(CountDownLatch latch, boolean measure) {
		this.latch = latch;
		this.measure = measure;
	}

	@Override
	public void run() {
		try {
			Socket conn = new Socket("localhost", 12345);

			PrintWriter writer = new PrintWriter(conn.getOutputStream(), true);
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String msg = "Oi, quer conversar?";

			if (measure){
				this.sendMessagesMeasure(writer, reader, msg);
			}
			else {
				this.sendMessages(writer, reader, msg);
			}

			conn.close();

			this.latch.countDown();
		} 
		catch (IOException e) {
			e.printStackTrace();
		} 
	}


	private void sendMessagesMeasure(PrintWriter writer, BufferedReader reader, String msg) throws IOException {
		long startTime = 0;
		int tries = 10000;
		long[] measures = new long[tries];
		for (int i = 0; i < tries; i++) {			
			startTime = System.nanoTime();
			writer.println(msg);
			reader.readLine();
			measures[i] = System.nanoTime() - startTime;
		}

		this.avgTime = Arrays.stream(measures).sum() / measures.length;

		long sum = 0;
		for (int i = 0; i < measures.length; i++) {
			sum += Math.pow((measures[i] - this.avgTime), 2);
		}

		this.stdDeviation = Math.sqrt((sum/measures.length));
	}

	private void sendMessages(PrintWriter writer, BufferedReader reader, String msg) throws IOException {
		int tries = 10000;
		for (int i = 0; i < tries; i++) {				
			writer.println(msg);
			reader.readLine();
		}
	}

	public long getAvgTime(){
		return this.avgTime;
	}

	public double getStdDeviation(){
		return this.stdDeviation;
	}

}
