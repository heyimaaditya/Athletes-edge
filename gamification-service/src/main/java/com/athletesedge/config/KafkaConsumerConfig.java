package com.athletesedge.config;

import com.athletesedge.dto.GoalCompletedEventDTO;
import com.athletesedge.dto.PrLoggedEventDTO;
import com.athletesedge.dto.WorkoutEventDTO;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
public class KafkaConsumerConfig {

  
    
    @Bean
    public ConsumerFactory<String, Object> consumerFactory(KafkaProperties kafkaProperties) {
   
        kafkaProperties.getConsumer().getProperties().put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return new DefaultKafkaConsumerFactory<>(kafkaProperties.buildConsumerProperties());
    }

   
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, WorkoutEventDTO> workoutKafkaListenerContainerFactory(
            ConsumerFactory<String, Object> consumerFactory) {
        
        ConcurrentKafkaListenerContainerFactory<String, WorkoutEventDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        
     
        factory.getContainerProperties().getKafkaConsumerProperties()
               .put(JsonDeserializer.VALUE_DEFAULT_TYPE, WorkoutEventDTO.class);
               
        return factory;
    }

   
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PrLoggedEventDTO> prKafkaListenerContainerFactory(
            ConsumerFactory<String, Object> consumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, PrLoggedEventDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        
     
        factory.getContainerProperties().getKafkaConsumerProperties()
               .put(JsonDeserializer.VALUE_DEFAULT_TYPE, PrLoggedEventDTO.class);
               
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, GoalCompletedEventDTO> goalKafkaListenerContainerFactory(
            ConsumerFactory<String, Object> consumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, GoalCompletedEventDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        
       
        factory.getContainerProperties().getKafkaConsumerProperties()
               .put(JsonDeserializer.VALUE_DEFAULT_TYPE, GoalCompletedEventDTO.class);
               
        return factory;
    }
}