package ru.demlovesky;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Component
public class FileReceiver {

    private String destination;
    private String brokerAddress;

//    public FileReceiver(String brokerAddress) {
//        this.brokerAddress = brokerAddress;
//    }

    public void setDestination(String destination, String brokerAddress) {
        this.destination = destination;
        this.brokerAddress = brokerAddress;
    }

    @JmsListener(destination = "#{fileReceiver.destination}")
    public void receiveFile(File file) {
        try {
            saveFile(file);
            System.out.println("Файл получен и сохранен: " + file.getName());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Ошибка при получении файла: " + file.getName());
        }
    }

    private void saveFile(File file) throws IOException {
        String saveDirectory = "C:\\\\download\\";

        File directory = new File(saveDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (OutputStream outputStream = new FileOutputStream(new File(saveDirectory, file.getName()))) {
            byte[] fileBytes = org.apache.commons.io.FileUtils.readFileToByteArray(file);
            outputStream.write(fileBytes);
        }
    }
}