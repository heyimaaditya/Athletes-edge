package com.athletesedge.config;

import com.athletesedge.dto.InjuryRiskAlertDTO;
import com.athletesedge.dto.GamificationUpdateDTO;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    private <T> ConsumerFactory<String, T> buildFactory(KafkaProperties props, Class<T> targetType) {
        Map<String, Object> cfg = props.buildConsumerProperties();

        
        JsonDeserializer<T> value = new JsonDeserializer<>(targetType, false);
      

        return new DefaultKafkaConsumerFactory<>(
                cfg,
                new StringDeserializer(),
                value
        );
    }

    // ---- Injury Alerts ----
    @Bean
    public ConsumerFactory<String, InjuryRiskAlertDTO> injuryAlertConsumerFactory(KafkaProperties props) {
        return buildFactory(props, InjuryRiskAlertDTO.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, InjuryRiskAlertDTO>
    injuryAlertKafkaListenerContainerFactory(ConsumerFactory<String, InjuryRiskAlertDTO> cf) {
        ConcurrentKafkaListenerContainerFactory<String, InjuryRiskAlertDTO> f = new ConcurrentKafkaListenerContainerFactory<>();
        f.setConsumerFactory(cf);
        return f;
    }

    // ---- Gamification Updates ----
    @Bean
    public ConsumerFactory<String, GamificationUpdateDTO> gamificationUpdateConsumerFactory(KafkaProperties props) {
        return buildFactory(props, GamificationUpdateDTO.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, GamificationUpdateDTO>
    gamificationUpdateKafkaListenerContainerFactory(ConsumerFactory<String, GamificationUpdateDTO> cf) {
        ConcurrentKafkaListenerContainerFactory<String, GamificationUpdateDTO> f = new ConcurrentKafkaListenerContainerFactory<>();
        f.setConsumerFactory(cf);
        return f;
    }
}


