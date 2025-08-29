package com.athletesedge.config;

import com.athletesedge.dto.NutritionEventDTO;
import com.athletesedge.dto.WorkoutEventDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, WorkoutEventDTO> workoutKafkaListenerContainerFactory(ConsumerFactory<String, Object> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, WorkoutEventDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
       
        factory.getContainerProperties().getKafkaConsumerProperties().put(JsonDeserializer.VALUE_DEFAULT_TYPE, WorkoutEventDTO.class);
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, NutritionEventDTO> nutritionKafkaListenerContainerFactory(ConsumerFactory<String, Object> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, NutritionEventDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
     
        factory.getContainerProperties().getKafkaConsumerProperties().put(JsonDeserializer.VALUE_DEFAULT_TYPE, NutritionEventDTO.class);
        return factory;
    }
}
