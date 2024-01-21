package ru.demlovesky;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TopicManager {

    @Autowired
    private JmsTemplate jmsTemplate;

    private final List<String> topics = new ArrayList<>();

    public void createTopic(String topicName) {
        jmsTemplate.setPubSubDomain(true);
        jmsTemplate.convertAndSend("topic:" + topicName, "Новый топик создан: " + topicName);
        topics.add(topicName);
    }

    public void listTopics() {
        System.out.println("Актуальные топики:");
        for (String topic : topics) {
            System.out.println("- " + topic);
        }
    }
}