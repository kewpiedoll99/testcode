package com.barclayadunn.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;

public class NewTask {

    private static final String TASK_QUEUE_NAME = "application_jobs.order_delivered";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("rabbit-lb.q.rtrdc.net");
        factory.setPort(5672);
        factory.setUsername("notification_wedge");
        factory.setPassword("nomorebombs!");
        try (Connection connection = factory.newConnection();
                Channel channel = connection.createChannel()) {
//            channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
//            String message = String.join(" ", argv);
//            channel.basicPublish("", TASK_QUEUE_NAME,
//                    MessageProperties.PERSISTENT_TEXT_PLAIN,
//                    message.getBytes("UTF-8"));
//            System.out.println(" [x] Sent '" + message + "'");

            final boolean ackMode = true;
            DefaultConsumer consumer = new DefaultConsumer(channel);
            channel.basicConsume(TASK_QUEUE_NAME, ackMode, consumer);
            consumer.handleConsumeOk("");
        }
    }
}
