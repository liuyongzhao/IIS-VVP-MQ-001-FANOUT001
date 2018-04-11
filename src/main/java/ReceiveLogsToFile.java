import com.rabbitmq.client.*;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeoutException;

public class ReceiveLogsToFile {
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
                print2File(message);

            }
        };
        channel.basicConsume(queueName, true,consumer);
    }

    private static void print2File(String message){
    try{
        String dir = ReceiveLogsToFile.class.getClassLoader().getResource("").getPath();
        String fileName = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        File file = new File(dir,fileName+".txt");
        FileOutputStream fos = new FileOutputStream(file,true);
        fos.write((message+"\r\n").getBytes());
        System.out.println("将文件"+fileName+"保存至"+dir );
        System.out.println("记录到文件夹的日志内容是： '" + message + "'");
        fos.flush();
        fos.close();
    }catch(Exception e){
        e.printStackTrace();
        }
    }
}


