
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Cat cat = new Cat("Мурчик");

        System.out.println("🐾 Вітаємо у симуляторі кота!");
        System.out.println("Ваш улюбленець: " + cat.getNick() + " 🐱\n");

        while (true) {
            cat.printStatus();

            System.out.println("========== МЕНЮ ==========");
            System.out.println("1 - Пограти 🧶");
            System.out.println("2 - Погодувати 🍖");
            System.out.println("3 - Укласти спати 😴");
            System.out.println("4 - Миється 🧼");
            System.out.println("0 - Вихід");
            System.out.print("👉 Виберіть дію: ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Будь ласка, введіть число від 0 до 4.");
                continue;
            }

            switch (choice) {
                case 1 -> cat.play();
                case 2 -> cat.eat();
                case 3 -> cat.sleep();
                case 4 -> cat.wash();
                case 0 -> {
                    System.out.println("Дякуємо за гру!");
                    return;
                }
                default -> System.out.println("Невірна команда. Введіть число від 0 до 4.");
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
