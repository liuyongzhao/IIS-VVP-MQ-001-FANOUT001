import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.util.Date;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
public class ProduceLogs {
    // 队列名称
    private final static String EXCHANGE_NAME = "ex_log";
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException  {
        // 创建连接和频道
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        // 声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        String message = new Date().toString()+":log something";
        channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
        System.out.println(" 发送的消息是:"+message);
        // 关闭频道和连接
        channel.close();
        connection.close();

    }
}
