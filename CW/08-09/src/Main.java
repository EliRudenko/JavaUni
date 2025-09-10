public class Main {
    public static void main(String[] args) {

        Double ref1 = Double.valueOf(10);
        Double ref2 = Double.valueOf(8.64);
        Double ref3 = Double.valueOf("6.86");

        System.out.println("из int: " + ref1);
        System.out.println("из double: " + ref2);
        System.out.println("из String: " + ref3);

        // === Основные методы Double ===
        System.out.println("\nметоды___________");
        System.out.println("doubleValue(): " + ref2.doubleValue());
        System.out.println("intValue(): " + ref2.intValue());
        System.out.println("compareTo(num1): " + ref2.compareTo(ref1));
        System.out.println("equals(num1): " + ref2.equals(ref1));
        System.out.println("toString(): " + ref2.toString());

        // Константы
        System.out.println("MAX_VALUE: " + Double.MAX_VALUE);
        System.out.println("MIN_VALUE: " + Double.MIN_VALUE);

        // === Ошибка при создании из строки ===
        try {
            Double refBad = Double.valueOf("abc");
            System.out.println("refBad: " + refBad);
        } catch (NumberFormatException e) {
            System.out.println("\nОшибка строки: " + e);
        }

        // === Попытка изменить состояние ===
        Double x = Double.valueOf(5.5);
        System.out.println("\nx= " + x);

        // Double immutable → новое значение создаётся в новой переменной
        Double y = x + 10;
        System.out.println("x+10 =" + x + " не изменилось");
        System.out.println("Новый y: " + y);
    }
}
