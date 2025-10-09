package org.example.student;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

// клас для управління даними студента
public class Student {
    private String fullName;
    private LocalDate birthDate;
    private final List<Integer> homeworkGrades;
    private final List<Integer> examGrades;
    private static final int MAX_EXAM_GRADES = 5;
    private static final int MIN_GRADE = 1;
    private static final int MAX_GRADE = 12;

    // конструктор для ініціалізації студента
    public Student(String fullName, LocalDate birthDate) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("ПІБ не може бути порожнім");
        }
        if (birthDate == null || birthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Дата народження некоректна");
        }
        this.fullName = fullName.trim();
        this.birthDate = birthDate;
        this.homeworkGrades = new ArrayList<>();
        this.examGrades = new ArrayList<>();
    }

    // отримання ПІБ
    public String getFullName() {
        return fullName;
    }

    // встановлення ПІБ з валідацією
    public void setFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("ПІБ не може бути порожнім");
        }
        this.fullName = fullName.trim();
    }

    // отримання дати народження
    public LocalDate getBirthDate() {
        return birthDate;
    }

    // встановлення дати народження з валідацією
    public void setBirthDate(LocalDate birthDate) {
        if (birthDate == null || birthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Дата народження некоректна");
        }
        this.birthDate = birthDate;
    }

    // отримання незмінного списку оцінок за домашні завдання
    public List<Integer> getHomeworkGrades() {
        return Collections.unmodifiableList(homeworkGrades);
    }

    // додавання оцінки за домашнє завдання
    public void addHomeworkGrade(int grade) {
        if (!isValidGrade(grade)) {
            throw new IllegalArgumentException(
                    "Оцінка за домашнє завдання повинна бути від " + MIN_GRADE + " до " + MAX_GRADE);
        }
        homeworkGrades.add(grade);
    }

    // отримання незмінного списку оцінок за екзамени
    public List<Integer> getExamGrades() {
        return Collections.unmodifiableList(examGrades);
    }

    // додавання оцінки за екзамен
    public void addExamGrade(int grade) {
        if (examGrades.size() >= MAX_EXAM_GRADES) {
            throw new IllegalStateException("Максимальна кількість оцінок за екзамени: " + MAX_EXAM_GRADES);
        }
        if (!isValidGrade(grade)) {
            throw new IllegalArgumentException("Оцінка за екзамен повинна бути від " + MIN_GRADE + " до " + MAX_GRADE);
        }
        examGrades.add(grade);
    }

    // перевірка валідності оцінки
    private boolean isValidGrade(int grade) {
        return grade >= MIN_GRADE && grade <= MAX_GRADE;
    }

    // обчислення середньої оцінки за домашні завдання
    public double getAverageHomeworkGrade() {
        if (homeworkGrades.isEmpty()) {
            return 0.0;
        }
        return homeworkGrades.stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }

    // обчислення середньої оцінки за екзамени
    public double getAverageExamGrade() {
        if (examGrades.isEmpty()) {
            return 0.0;
        }
        return examGrades.stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }

    // порівняння об'єктів Student
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Student))
            return false;
        Student student = (Student) o;
        return fullName.equals(student.fullName) && birthDate.equals(student.birthDate);
    }

    // генерація хеш-коду
    @Override
    public int hashCode() {
        return Objects.hash(fullName, birthDate);
    }

    // форматування рядка для виведення інформації про студента
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Студент: ").append(fullName).append("\nДата народження: ").append(birthDate)
                .append("\nОцінки за домашні завдання: ").append(homeworkGrades.isEmpty() ? "немає" : homeworkGrades)
                .append("\nСередня оцінка за домашні завдання: ")
                .append(String.format("%.2f", getAverageHomeworkGrade())).append("\nОцінки за екзамени: ")
                .append(examGrades.isEmpty() ? "немає" : examGrades).append("\nСередня оцінка за екзамени: ")
                .append(String.format("%.2f", getAverageExamGrade()));
        return sb.toString();
    }
}