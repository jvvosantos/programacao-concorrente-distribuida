package exercicio2.udp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class UDPServer extends Thread {
	
	private ServerSocket server;

	public UDPServer() {
	}

	@Override
	public void run() {
		try {
			System.out.println("Starting server");
			this.server = new ServerSocket(12345);
			while(true){	
				Socket conn = this.server.accept();
				Thread serverOperation = new Thread(new ServerOperation(conn));
				serverOperation.start();
			}
		} 
		catch (IOException e) {
			System.out.println("Server stopped");
		}
	}

	private class ServerOperation implements Runnable {

		private Socket conn;

		public ServerOperation(Socket conn) {
			this.conn = conn;
		}

		@Override
		public void run() {
			try {				
				DatagramSocket udpSocket = new DatagramSocket();
				int udpSocketPort = udpSocket.getLocalPort();
				
				PrintWriter writer = new PrintWriter(conn.getOutputStream(), true);
				writer.println(udpSocketPort);
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String clientUdpPortStr = reader.readLine();
				int clientUdpPort = Integer.parseInt(clientUdpPortStr);

				String message = "Sim, seria um prazer!";
				
				byte[] buf = new byte[19];
				DatagramPacket sendPacket = new DatagramPacket (message.getBytes(), message.getBytes().length,
																InetAddress.getByName("localhost"), clientUdpPort);
				
				boolean exchanging = true;
				while (exchanging){
		        	DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);
		        	udpSocket.receive(receivePacket);
		        	udpSocket.send(sendPacket);
					if (new String(receivePacket.getData()).trim().startsWith("end")){
						exchanging = false;
					}
				}
				
				udpSocket.close();
				conn.close();
			}
			catch (IOException e){
				e.printStackTrace();
			} 
		}

	}

	public void shutdown() {
		try {
			this.server.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

}
