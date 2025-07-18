package com.fitness.activityservice.service;

import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.model.Activity;
import com.fitness.activityservice.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class Activityservice {

    private final ActivityRepository activityRepository;
    private final UserValidationService userValidationService;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;
    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    public ActivityResponse trackActivityOld(ActivityRequest request) {

        boolean isValidUser = userValidationService.validateUserold(request.getUserId());
        if(!isValidUser) {
            throw new RuntimeException("Invalid User: " + request.getUserId());
        }
        Activity activity = Activity.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .duration(request.getDuration())
                .caloriesBurned(request.getCaloriesBurned())
                .startTime(request.getStartTime())
                .additionalMetrics(request.getAdditionalMetrics())
                .build();

        Activity saveActivity = activityRepository.save(activity);

        // Publish to RabbitMQ for AI Processing
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, saveActivity);
        } catch (Exception e) {
            log.error("Failed to publish activity to RabbitMQ: ", e);

        }
        return mapToResponse(saveActivity);

    }
    public Mono<ActivityResponse> trackActivity(ActivityRequest request) {
        return userValidationService.validateUser(request.getUserId())
                .flatMap(isValidUser -> {
                    if (!isValidUser) {
                        return Mono.error(new RuntimeException("Invalid User: " + request.getUserId()));
                    }

                    Activity activity = Activity.builder()
                            .userId(request.getUserId())
                            .type(request.getType())
                            .duration(request.getDuration())
                            .caloriesBurned(request.getCaloriesBurned())
                            .startTime(request.getStartTime())
                            .additionalMetrics(request.getAdditionalMetrics())
                            .build();

                    Activity savedActivity = activityRepository.save(activity);

                    try {
                        rabbitTemplate.convertAndSend(exchange, routingKey, savedActivity);
                    } catch (Exception e) {
                        log.error("Failed to publish activity to RabbitMQ: ", e);
                    }

                    return Mono.just(mapToResponse(savedActivity));
                });
    }

    private ActivityResponse mapToResponse(Activity activity) {
        ActivityResponse response = new ActivityResponse();
        response.setId(activity.getId());
        response.setType(activity.getType());
        response.setUserId(activity.getUserId());
        response.setDuration(activity.getDuration());
        response.setCaloriesBurned(activity.getCaloriesBurned());
        response.setStartTime(activity.getStartTime());
        response.setAdditionalMetrics(activity.getAdditionalMetrics());
        response.setCreatedAt(activity.getCreatedAt());
        response.setUpdatedAt(activity.getUpdatedAt());

        return response;
    }

    public List<ActivityResponse> getUserActivities(String userId) {
        List<Activity> activities = activityRepository.findByUserId(userId);
        return  activities.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ActivityResponse getActivityById(String activityId) {

        return activityRepository.findById(activityId)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Activity not found with id: "+ activityId));
    }

}
