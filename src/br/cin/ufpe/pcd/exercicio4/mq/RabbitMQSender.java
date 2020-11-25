package br.cin.ufpe.pcd.exercicio4.mq;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQSender extends Thread {

	private String queueName;

	public RabbitMQSender() {

	}

	public RabbitMQSender(String queueName) {
		this.queueName = queueName;
	}

	@Override
	public void run() {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		try (Connection connection = factory.newConnection();
				Channel channel = connection.createChannel()) {

			channel.queueDeclare(queueName, false, false, false, null);
			String message;
			int tries = 10000;

			for (int i = 0; i < tries; i++) {
				message = "Oi, quer conversar?"+System.nanoTime();
				channel.basicPublish("", queueName, null, message.getBytes(StandardCharsets.UTF_8));
			}

			message = "end?"+System.nanoTime();
			channel.basicPublish("", queueName, null, message.getBytes(StandardCharsets.UTF_8));
		}
		catch (IOException e) {
			e.printStackTrace();
		} 
		catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		(new Thread(new RabbitMQSender("test"))).start();
	}

}
