import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
public class ReceiveLogsToConsole {
    private final static String EXCHANGE_NAME = "ex_log";
    public final static String EXCHANGE_TYPE = "fanout";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建factory
        ConnectionFactory factory = new ConnectionFactory();
        // 创建连接
        Connection connection = factory.newConnection();
        // 创建信道
        Channel channel = connection.createChannel();

        // 创建交换器
        channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);

        //生成随机队列名
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_NAME, "");
        System.out.println("开始等待MQ中间件消息....");
        QueueingConsumer queueConsumer = new QueueingConsumer(channel);

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag,Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" 接收到的消息是： '" + message + "'");
            }
        };
        channel.basicConsume(queueName, true,consumer);
    }
}


