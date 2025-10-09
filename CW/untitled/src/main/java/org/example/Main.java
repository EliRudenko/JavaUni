package org.example;

import java.time.LocalDate;
import org.example.group.Group;
import org.example.student.Student;

public class Main {
    public static void main(String[] args) {
        // створення студентів для першої групи
        Student s1 = new Student("Олена Іванівна Коваль", LocalDate.of(2003, 5, 12));
        s1.addHomeworkGrade(10);
        s1.addHomeworkGrade(11);
        s1.addHomeworkGrade(12);
        s1.addExamGrade(9);
        s1.addExamGrade(10);
        s1.addExamGrade(8);

        Student s2 = new Student("Петро Сергійович Шевченко", LocalDate.of(2002, 8, 22));
        s2.addHomeworkGrade(8);
        s2.addHomeworkGrade(9);
        s2.addExamGrade(7);
        s2.addExamGrade(11);

        Student s3 = new Student("Марія Василівна Грищенко", LocalDate.of(2004, 3, 15));
        s3.addHomeworkGrade(12);
        s3.addHomeworkGrade(10);
        s3.addExamGrade(12);
        s3.addExamGrade(10);

        // створення першої групи та додавання студентів
        Group group1 = new Group("КНП-221");
        group1.addStudent(s1);
        group1.addStudent(s2);
        group1.addStudent(s3);

        // виведення інформації про групу
        System.out.println("=== Інформація про групу КНП-221 ===");
        System.out.println(group1);

        // створення студентів для другої групи
        Student s4 = new Student("Іван Петрович Сидоренко", LocalDate.of(2003, 11, 7));
        s4.addHomeworkGrade(7);
        s4.addHomeworkGrade(8);
        s4.addHomeworkGrade(9);
        s4.addExamGrade(6);
        s4.addExamGrade(8);

        Student s5 = new Student("Анна Олегівна Лисенко", LocalDate.of(2002, 1, 30));
        s5.addHomeworkGrade(11);
        s5.addHomeworkGrade(12);
        s5.addExamGrade(10);
        s5.addExamGrade(11);
        s5.addExamGrade(12);

        // створення другої групи та додавання студентів
        Group group2 = new Group("КНП-222");
        group2.addStudent(s4);
        group2.addStudent(s5);

        // виведення інформації про другу групу
        System.out.println("\n=== Інформація про групу КНП-222 ===");
        System.out.println(group2);

        // тестування сортування за ПІБ
        System.out.println("\n=== Студенти КНП-221, відсортовані за ПІБ ===");
        group1.getStudentsSortedByName().forEach(student -> System.out.println(student.getFullName()
                + ", середня оцінка за екзамени: " + String.format("%.2f", student.getAverageExamGrade())));

        // тестування сортування за середньою оцінкою за домашні завдання
        System.out.println("\n=== Студенти КНП-221, відсортовані за середньою оцінкою за домашні завдання ===");
        group1.getStudentsSortedByAverageHomeworkGrade().forEach(student -> System.out.println(student.getFullName()
                + ", середня оцінка за ДЗ: " + String.format("%.2f", student.getAverageHomeworkGrade())));

        // тестування пошуку студента
        System.out.println("\n=== Пошук студента в КНП-221 ===");
        String searchName = "Олена Іванівна Коваль";
        group1.findStudentByName(searchName).ifPresentOrElse(student -> System.out.println("Знайдено: " + student),
                () -> System.out.println("Студента з ПІБ " + searchName + " не знайдено"));

        // тестування видалення студента
        System.out.println("\n=== Видалення студента з КНП-221 ===");
        group1.removeStudentByName("Петро Сергійович Шевченко");
        System.out.println("Після видалення Петра Шевченка:\n" + group1);

        // тестування студентів з оцінками за екзамени вище порогу
        System.out.println("\n=== Студенти КНП-222 з середньою оцінкою за екзамени >= 10 ===");
        group2.getStudentsWithExamGradeAbove(10.0).forEach(student -> System.out.println(student.getFullName()
                + ", середня оцінка за екзамени: " + String.format("%.2f", student.getAverageExamGrade())));

        // виведення загальної статистики
        System.out.println("\n=== Загальна статистика ===");
        System.out.println(
                "Група КНП-221, середня оцінка за ДЗ: " + String.format("%.2f", group1.getAverageHomeworkGrade()));
        System.out.println(
                "Група КНП-221, середня оцінка за екзамени: " + String.format("%.2f", group1.getAverageExamGrade()));
        System.out.println(
                "Група КНП-222, середня оцінка за ДЗ: " + String.format("%.2f", group2.getAverageHomeworkGrade()));
        System.out.println(
                "Група КНП-222, середня оцінка за екзамени: " + String.format("%.2f", group2.getAverageExamGrade()));
    }
}