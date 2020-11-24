package exercicio2.tcp;

import java.util.concurrent.CountDownLatch;

public class TCPMain {
	
	public static void main(String[] args) throws InterruptedException {
		TCPServer server = new TCPServer();
		Thread serverThread = new Thread(server);
		serverThread.start();
		
		int[] numClientsArray = {2, 5, 10 };
        int numClients;
		CountDownLatch latch;
		
		for (int i = 0; i < numClientsArray.length; i++) {
			numClients = numClientsArray[i];
			latch = new CountDownLatch(numClients);
			
			System.out.println("Running with "+numClients+" clients");

	        TCPClient measuringClient = new TCPClient(latch, true);
			Thread measuringClientThread = new Thread(measuringClient);
			measuringClientThread.start();
			
			for (int j = 1; j < numClients; j++) {
		        TCPClient client = new TCPClient(latch, true);
				Thread clientThread = new Thread(client);
				clientThread.start();
			}

			latch.await();

			System.out.println("avg time: "+measuringClient.getAvgTime()+" nanosec");
			System.out.printf("std deviation: %.2f", measuringClient.getStdDeviation());
			System.out.println();
			
		}
		
		System.out.println("Stopping server");
		server.shutdown();
		
		
	}

}
