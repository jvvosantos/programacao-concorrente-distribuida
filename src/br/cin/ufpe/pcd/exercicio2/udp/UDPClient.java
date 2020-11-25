package br.cin.ufpe.pcd.exercicio2.udp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class UDPClient implements Runnable {

	private CountDownLatch latch;

	private boolean measure;
	private double avgTime;
	private double stdDeviation;

	public UDPClient(CountDownLatch latch, boolean measure) {
		this.latch = latch;
		this.measure = measure;
	}

	@Override
	public void run() {
		try {
			Socket conn = new Socket("localhost", 12345);
			
			DatagramSocket udpSocket = new DatagramSocket();

			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String serverUdpPortStr = reader.readLine();
			int serverUdpPort = Integer.parseInt(serverUdpPortStr);
			
			PrintWriter writer = new PrintWriter(conn.getOutputStream(), true);
			writer.println(udpSocket.getLocalPort());
			
			String message = "Oi, quer conversar?";
			
			if (measure){
				this.sendMessagesMeasure(udpSocket, serverUdpPort, message);
			}
			else {
				this.sendMessages(udpSocket, serverUdpPort, message);
			}

			conn.close();

			this.latch.countDown();
		} 
		catch (IOException e) {
			e.printStackTrace();
		} 
	}


	private void sendMessagesMeasure(DatagramSocket udpSocket, int serverUdpPort, String message) throws IOException {
		long startTime = 0;
		int tries = 10000;
		double[] measures = new double[tries];

		byte[] buf = new byte[21];
		DatagramPacket sendPacket = new DatagramPacket (message.getBytes(), message.getBytes().length,
				InetAddress.getByName("localhost"), serverUdpPort);
		
		for (int i = 0; i < tries; i++) {		
        	DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);	
			startTime = System.nanoTime();
			udpSocket.send(sendPacket);
			udpSocket.receive(receivePacket);
			measures[i] = (System.nanoTime() - startTime) / 1_000_000.00;
		}
		
		String endMessage = "end";
		DatagramPacket endPacket = new DatagramPacket (endMessage.getBytes(), endMessage.getBytes().length,
				InetAddress.getByName("localhost"), serverUdpPort);
		udpSocket.send(endPacket);
		udpSocket.close();

		this.avgTime = Arrays.stream(measures).sum() / measures.length;

		double sum = 0;
		for (int i = 0; i < measures.length; i++) {
			sum += Math.pow((measures[i] - this.avgTime), 2);
		}

		this.stdDeviation = Math.sqrt((sum/measures.length));
	}

	private void sendMessages(DatagramSocket udpSocket, int serverUdpPort, String message) throws IOException {
		byte[] buf = new byte[21];
		DatagramPacket sendPacket = new DatagramPacket (message.getBytes(), message.getBytes().length,
				InetAddress.getByName("localhost"), serverUdpPort);

		int tries = 10000;
		for (int i = 0; i < tries; i++) {		
        	DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);
			udpSocket.send(sendPacket);
			udpSocket.receive(receivePacket);
		}
		
		String endMessage = "end";
		DatagramPacket endPacket = new DatagramPacket (endMessage.getBytes(), endMessage.getBytes().length,
				InetAddress.getByName("localhost"), serverUdpPort);
		udpSocket.send(endPacket);
		udpSocket.close();
		
	}

	public double getAvgTime(){
		return this.avgTime;
	}

	public double getStdDeviation(){
		return this.stdDeviation;
	}

}
