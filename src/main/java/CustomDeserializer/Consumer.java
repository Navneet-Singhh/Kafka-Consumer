package CustomDeserializer;

import CustomDeserializer.pojo.FeDevice;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.*;

public class Consumer {

    public static void consumer()
    {
        {
            ClassLoader original = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(null);
            Properties props = new Properties();
            props.setProperty("bootstrap.servers", "localhost:9092");
            props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            props.setProperty("value.deserializer", DeviceDersializer.class.getName());
            props.setProperty("group.id", "org.apache.kafka.common.serialization.IntegerDeserializer");
            props.setProperty(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, "123432");
            props.setProperty(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, "200");//-->min time till server wait to respond the request
            props.setProperty(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, "1000");//-->Time in between consumer has sent to heartBeat.
            props.setProperty(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "3000");//->After how many times the consumer is considered dead if consumer is not sending the heartbeat.
            // props.setProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,"2000");
            props.setProperty(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, "1MB");
            props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
            props.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, "OrderedList");
            props.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "100");
            Thread.currentThread().setContextClassLoader(original);

            Map<TopicPartition, OffsetAndMetadata> currentOffset = new HashMap<>();
            KafkaConsumer<String, FeDevice> consumer = new KafkaConsumer<String, FeDevice>(props);
            class consumerRebalancer implements ConsumerRebalanceListener {

                @Override
                public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                    consumer.commitSync(currentOffset);
                }

                @Override
                public void onPartitionsAssigned(Collection<TopicPartition> partitions) {

                }
            }
            consumer.subscribe(Collections.singleton("newTopicc"), new consumerRebalancer());
            try {
                ConsumerRecords<String, FeDevice> records = consumer.poll(Duration.ofSeconds(40));
                int count = 0;
                for (ConsumerRecord<String, FeDevice> record : records) {
                    count++;
                    System.out.println("DeviceId :->" + record.value().getDeviceId());
                    System.out.println("DeviceName :->" + record.value().getDeviceName());
                    System.out.println("Here we can see the partition of the data" + record.partition());//Here we are getting the partition info through the record means from which record it is coming
                    currentOffset.put(new TopicPartition(record.topic(), record.partition()), new OffsetAndMetadata(record.offset() + 1));
                    //-->Use of cusTomcommit-->This makes Sure that the commit will happen after every 10 records and this commit async will provide the information
                    //of Topic Parition and the offset from where consumer has to start reading next.
                    if (count % 10 == 0) {
                        consumer.commitAsync(currentOffset, new OffsetCommitCallback() {
                            @Override
                            public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
                                if (exception != null) {
                                    exception.printStackTrace();
                                }
                            }
                        });
                    }
                }
                //consumer.commitSync();//-->commit the whole batch of record at once makes the application a bit slower,but did not solve the duplicacy issue.
//                consumer.commitAsync(new OffsetCommitCallback() {
//                    @Override
//                    public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
//                        if(exception!=null){
//                            System.out.println("Exception Occured ");
//                        }
//                    }
//                });-->commitAsync also did not solved the problem of commitSync.
            } finally {
                consumer.close();
            }


            // consumer.poll(Duration.ZERO);//means consumer see if the topic has some message it will return otherwiswe it will immediately return , will not wait for any incomming data
        }
    }

    public static void main(String[] args) {
        consumer();
    }

}
