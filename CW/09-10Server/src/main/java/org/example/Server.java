package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12348)) {
            System.out.println("Сервер слухає на порту 12348");

            while (true) {
                try (Socket socket = serverSocket.accept();
                     BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     PrintWriter output = new PrintWriter(socket.getOutputStream(), true)) {

                    System.out.println("Клієнт підключився");

                    String received;
                    while ((received = input.readLine()) != null) {
                        if (received.equalsIgnoreCase("exit")) {
                            System.out.println("Клієнт запросив вихід");
                            break;
                        }

                        System.out.println("Отримано: " + received);

                        // збільшуємо число на 1
                        int number = Integer.parseInt(received);
                        int result = number + 1;

                        // надсилаємо назад
                        output.println(result);
                    }
                    System.out.println("Клієнт відключився");

                } catch (IOException e) {
                    System.err.println("Помилка обробки клієнта: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Помилка сервера: " + e.getMessage());
        }
    }
}