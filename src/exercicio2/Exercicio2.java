package exercicio2;

import java.util.concurrent.CountDownLatch;

import exercicio2.tcp.TCPMain;
import exercicio2.udp.UDPMain;

public class Exercicio2 {
	
	public static void main(String[] args) throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		
		System.out.println("## Getting TCP Results ##");
		TCPMain tcp = new TCPMain(latch);
		Thread tcpThread = new Thread(tcp);
		tcpThread.start();

		latch.await();
		latch = new CountDownLatch(1);

		System.out.println("## Getting UDP Results ##");
		UDPMain udp = new UDPMain(latch);
		Thread udpThread = new Thread(udp);
		udpThread.start();
		
		latch.await();
		
		//TODO plot chart

	}

}
