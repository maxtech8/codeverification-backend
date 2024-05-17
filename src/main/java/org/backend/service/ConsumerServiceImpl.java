package org.backend.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

@Service
public class ConsumerServiceImpl implements ConsumerService {

    /**
        The name of the exchange.
     */
    private static final String QUEUE = "messages";

    /**
     *  The function that consumes messages from the broker(RabbitMQ)
     * @param data
     */
    @Override
    @RabbitListener
    public void consumerMessage(byte[] data) {
    	try {
	    	ConnectionFactory factory = new ConnectionFactory();
	    	
	        factory.setUsername("admin");
	        factory.setPassword("admin");
	        factory.setPort(5672);
	        factory.setHost("localhost");
	        factory.setVirtualHost("/");
        
        	Connection connection = factory.newConnection();
        	Channel channel = connection.createChannel();
        	System.out.println(connection.getPort());
            System.out.println(connection.getAddress());
            channel.queueDeclare(QUEUE, false, false, false, null);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println(" [x] Received '" + message + "'");
            };
            channel.basicConsume(QUEUE, true, deliverCallback, consumerTag -> { });
        } catch(IOException | TimeoutException e) {
        	// Handle IOException (e.g., connection failure)
            System.err.println("IOException occurred while connecting to RabbitMQ server: " + e.getMessage());
            e.printStackTrace();

            // Check if the exception is due to authentication failure
            if (e.getMessage().contains("ACCESS_REFUSED") || e.getMessage().contains("PLAIN authentication failure")) {
                System.err.println("Authentication failed. Please verify the provided username and password.");
            }
        }
    }
}

