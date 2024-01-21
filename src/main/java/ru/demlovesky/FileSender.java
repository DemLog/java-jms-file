package ru.demlovesky;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class FileSender {

    private final JmsTemplate jmsTemplate;

    @Autowired
    public FileSender(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendFile(String filePath, String destination, String brokerAddress) {
        ActiveMQConnectionFactory connectionFactory = (ActiveMQConnectionFactory) jmsTemplate.getConnectionFactory();
        assert connectionFactory != null;
        connectionFactory.setBrokerURL(brokerAddress);

        File file = new File(filePath);
        jmsTemplate.convertAndSend(destination, file);
    }
}
