package org.example;

import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12348);
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            String number;

            while (true) {
                System.out.print("Введіть число (або 'exit' для виходу): ");
                number = userInput.readLine();

                // надсилаємо число на сервер
                output.println(number);

                if (number.equalsIgnoreCase("exit")) {
                    break;
                }

                // отримуємо результат від сервера
                String response = input.readLine();
                if (response != null) {
                    System.out.println("Відповідь від сервера: " + response);
                } else {
                    System.out.println("Сервер закрив з'єднання.");
                    break;
                }
            }

        } catch (IOException e) {
            System.err.println("Помилка клієнта: " + e.getMessage());
        }
    }
}