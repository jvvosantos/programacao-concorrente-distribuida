package br.cin.ufpe.pcd.exercicio4.mq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class RabbitMQReceiver extends Thread {

	private String queueName;
	
	private List<Double> measures;
	private long avgTime;
	private double stdDeviation;
	
	private CountDownLatch latch;

	public RabbitMQReceiver() {

	}

	public RabbitMQReceiver(String queueName, CountDownLatch latch) {
		this.queueName = queueName;
		this.measures = new ArrayList<>();
		this.latch = latch;
	}

	@Override
	public void run() {
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();

			channel.queueDeclare(queueName, false, false, false, null);

			DeliverCallback measureCallback = (consumerTag, delivery) -> {
				long arrivalTime = System.nanoTime();
				String message = new String(delivery.getBody(), "UTF-8");
				long sentTime = Long.parseLong(message.substring(message.lastIndexOf("?")+1));
				double delay = (arrivalTime - sentTime) / 1_000_000.00;
				if (message.startsWith("end")) {
					this.calcResults();
					latch.countDown();
					connection.close();
				}
				else {
					measures.add(delay);
				}
			};
			
			DeliverCallback deliverCallback = (consumerTag, delivery) -> {
				String message = new String(delivery.getBody(), "UTF-8");
				if (message.startsWith("end")) {
					latch.countDown();
					connection.close();
				}
			};

			if (queueName.equals("measure")) {				
				channel.basicConsume(queueName, true, measureCallback, consumerTag -> { });
			}
			else {
				channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		} 
		catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

	private void calcResults() {
		this.avgTime = measures.stream().mapToLong(Double::longValue).sum() / measures.size();

		long sum = 0;
		for (int i = 0; i < measures.size(); i++) {
			sum += Math.pow((measures.get(i) - this.avgTime), 2);
		}

		this.stdDeviation = Math.sqrt((sum/measures.size()));
	}

	public long getAvgTime() {
		return avgTime;
	}

	public void setAvgTime(long avgTime) {
		this.avgTime = avgTime;
	}

	public double getStdDeviation() {
		return stdDeviation;
	}

	public void setStdDeviation(double stdDeviation) {
		this.stdDeviation = stdDeviation;
	}
	
}
