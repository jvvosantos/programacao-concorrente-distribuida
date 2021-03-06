package br.cin.ufpe.pcd.exercicio4.mq;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import br.cin.ufpe.pcd.util.ChartSeries;
import br.cin.ufpe.pcd.util.Exporter;

public class RabbitMQMain extends Thread {

	private Integer[] numClientArray;
	private Long[] avgTimeArray;
	private Double[] stdDeviationArray;

	private CountDownLatch latch;
	
	public RabbitMQMain() {
		
	}
	
	public RabbitMQMain(CountDownLatch latch) {
		this.latch = latch;
	}

	@Override
	public void run() {
		try {
			Integer[] numClientsArray = {2, 5, 10, 20};
			this.numClientArray = numClientsArray;
			this.avgTimeArray = new Long[numClientsArray.length];
			this.stdDeviationArray = new Double[numClientsArray.length];

			CountDownLatch latch;
			int numClients;
			String queueName;
			for (int i = 0; i < numClientsArray.length; i++) {
				numClients = numClientsArray[i];
				latch = new CountDownLatch(numClients);
				

				System.out.println("- Running with "+numClients+" clients -");

				System.out.println("Starting receivers");
				RabbitMQReceiver measuringReceiver = new RabbitMQReceiver("measure", latch);
				Thread measuringReceiverThread = new Thread(measuringReceiver);
				measuringReceiverThread.start();

				for (int j = 1; j < numClients; j++) {
					queueName = "q"+numClients+j;

					RabbitMQReceiver receiver = new RabbitMQReceiver(queueName, latch);
					Thread receiverThread = new Thread(receiver);
					receiverThread.start();
				}

				System.out.println("Starting senders");

				RabbitMQSender measuringSender = new RabbitMQSender("measure");
				Thread measuringSenderThread = new Thread(measuringSender);
				measuringSenderThread.start();
				for (int j = 1; j < numClients; j++) {
					queueName = "q"+numClients+j;
					RabbitMQSender sender = new RabbitMQSender(queueName);
					Thread senderThread = new Thread(sender);
					senderThread.start();
				}
				
				latch.await();
				this.avgTimeArray[i] = measuringReceiver.getAvgTime();
				this.stdDeviationArray[i] = measuringReceiver.getStdDeviation();
				
				System.out.println("avg time: "+measuringReceiver.getAvgTime()+" ms");
				System.out.printf("std deviation: %.2f", measuringReceiver.getStdDeviation());
				System.out.println();
			}
			
			if (this.latch != null) {
				this.latch.countDown();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Integer[] getNumClientArray() {
		return numClientArray;
	}

	public Long[] getAvgTimeArray() {
		return avgTimeArray;
	}

	public Double[] getStdDeviationArray() {
		return stdDeviationArray;
	}

	public static void main(String[] args)throws IOException, InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		RabbitMQMain main = new RabbitMQMain(latch);
		(new Thread(main)).start();
		latch.await();
		
		ChartSeries avgSeries = new ChartSeries("RABBIT", main.getNumClientArray(), main.getAvgTimeArray());
		ChartSeries stdSeries = new ChartSeries("RABBIT", main.getNumClientArray(), main.getStdDeviationArray());
		
		Exporter.exportJson(avgSeries, "rbt_avg.json");
		Exporter.exportJson(stdSeries, "rbt_std.json");
	}

}
