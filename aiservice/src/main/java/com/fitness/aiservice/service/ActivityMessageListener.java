package com.fitness.aiservice.service;

import com.fitness.aiservice.model.Activity;
import com.fitness.aiservice.model.Recommendation;
import com.fitness.aiservice.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityMessageListener {
    private final ActivityAIService aiService;
    private final RecommendationRepository recommendationRepository;
    private final MongoTemplate mongoTemplate;

    @RabbitListener(queues = "activity.queue")
    public void processActivity(Activity activity) {
        log.info("Received activity for processing: {}", activity.getId());
     //   log.info("Generated Recommendation: {}", aiService.generateRecommendation(activity));
        Recommendation recommendation = aiService.generateRecommendation(activity);
        recommendationRepository.save(recommendation);
        log.info("Saved Recommendation with ID: {}", recommendation.getId());
        String dbName = mongoTemplate.getDb().getName();
        String collectionName = mongoTemplate.getCollectionName(Recommendation.class);

        log.info("MongoDB Database: {}", dbName);
        log.info("MongoDB Collection: {}", collectionName);
    }

}
