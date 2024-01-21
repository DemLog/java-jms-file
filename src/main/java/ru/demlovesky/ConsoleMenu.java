package ru.demlovesky;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.File;

@Component
public class ConsoleMenu {

    private final FileSender fileSender;
    private final FileReceiver fileReceiver;
    private final TopicManager topicManager;

    @Autowired
    public ConsoleMenu(FileSender fileSender, FileReceiver fileReceiver, TopicManager topicManager) {
        this.fileSender = fileSender;
        this.fileReceiver = fileReceiver;
        this.topicManager = topicManager;
    }

    public void displayMainMenu() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                System.out.println("1. Отправить файл");
                System.out.println("2. Принять файл");
                System.out.println("3. Создать топик");
                System.out.println("4. Список топиков");
                System.out.println("5. Выход");

                System.out.print("Выберите опцию: ");
                String choice = reader.readLine();

                switch (choice) {
                    case "1":
                        sendFileMenu(reader);
                        break;
                    case "2":
                        receiveFileMenu(reader);
                        break;
                    case "3":
                        createTopicMenu(reader);
                        break;
                    case "4":
                        topicManager.listTopics();
                        break;
                    case "5":
                        System.exit(0);
                    default:
                        System.out.println("Неправильная команда! Попробуйте снова.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendFileMenu(BufferedReader reader) throws IOException {
        System.out.print("Ввелите путь до файла: ");
        String filePath = reader.readLine();

        if (new File(filePath).exists()) {
            System.out.print("Введите адрес JMS брокера (например, tcp://localhost:61616): ");
            String brokerAddress = reader.readLine();

            System.out.print("Введите имя топика: ");
            String topicName = reader.readLine();

            fileSender.sendFile(filePath, topicName, brokerAddress);
            System.out.println("Файл отправлен успешно.");
        } else {
            System.out.println("Файл не был найден! Попробуйте еще раз.");
        }
    }

    private void receiveFileMenu(BufferedReader reader) throws IOException {
        System.out.print("Введите адрес JMS брокера (например, tcp://localhost:61616): ");
        String brokerAddress = reader.readLine();

        System.out.print("Введите имя топика: ");
        String topicName = reader.readLine();

        fileReceiver.setDestination(topicName, brokerAddress);
        System.out.println("Прослушиваем топик: " + topicName);
    }

    private void createTopicMenu(BufferedReader reader) throws IOException {
        System.out.print("Введите имя топика: ");
        String topicName = reader.readLine();
        topicManager.createTopic(topicName);
        System.out.println("Топик был успешно создан.");
    }
}