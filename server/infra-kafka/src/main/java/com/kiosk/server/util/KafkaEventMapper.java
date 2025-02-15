package com.kiosk.server.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class KafkaEventMapper {

    public static String toEvent(Object eventObject) {
        ObjectMapper objectMapper = new ObjectMapper();

        String jsonEvent;
        try {
            jsonEvent = objectMapper.writeValueAsString(eventObject);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("카프카 json 변환 실패");
        }

        return jsonEvent;
    }

    public static <T> T toObject(String event, Class<T> targetObject) {
        ObjectMapper objectMapper = new ObjectMapper();

        T object;
        try {
            object = objectMapper.readValue(event, targetObject);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("kafka 객체 변환 실패");
        }

        return object;
    }
}
