package com.mycompany.ecommerce.consumer.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerCustomer {    

    @KafkaListener(topics = "clientes", groupId = "consumer-group-customer")
    public void consume(ConsumerRecord<String, String> record) {
        String topic = record.topic();
        String key = record.key();
        String message = record.value();
        long offset = record.offset();
        int partition = record.partition();
        
        log.info("*********************************");
        log.info("Mensagem recebida:");
        log.info("topic: {}", topic);
        log.info("Key: {}", key);
        log.info("Mensagem: {}", message);
        log.info("Offset: {}", offset);
        log.info("Partition: {}", partition);
        log.info("*********************************");
    }
}