package org.example;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        ClassLoader original = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(null);
        Properties props=new Properties();
        props.setProperty("bootstrap.servers","localhost:9092");
        props.setProperty("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer","org.apache.kafka.common.serialization.IntegerDeserializer");
        props.setProperty("group.id","org.apache.kafka.common.serialization.IntegerDeserializer");
        Thread.currentThread().setContextClassLoader(original);

        KafkaConsumer<String,Integer> consumer=new KafkaConsumer<String,Integer>(props);
        consumer.subscribe(Collections.singleton("navneetTopic1"));
       ConsumerRecords<String,Integer> records= consumer.poll(Duration.ofSeconds(20));
        for (ConsumerRecord<String, Integer> record:records) {
            System.out.println(record.key());
            System.out.println(record.value());
        }
        consumer.close();

       // consumer.poll(Duration.ZERO);//means consumer see if the topic has some message it will return otherwiswe it will immediately return , will not wait for any incomming data
    }
}