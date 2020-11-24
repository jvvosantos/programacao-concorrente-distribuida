package exercicio2.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer extends Thread {

	private ServerSocket server;

	public TCPServer() {
	}

	@Override
	public void interrupt() {
		try {
			System.out.println("closing");
			this.server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
				PrintWriter writer = new PrintWriter(conn.getOutputStream(), true);
				BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String message = "Sim, seria um prazer!";

				while (reader.readLine() != null){
					writer.println(message);
				}

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
