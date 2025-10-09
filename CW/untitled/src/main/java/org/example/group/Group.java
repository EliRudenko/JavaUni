package org.example.group;

import org.example.student.Student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

// клас для управління групою студентів
public class Group {
    private String groupName;
    private List<Student> students;
    private static final int MAX_STUDENTS = 30;

    // конструктор для ініціалізації групи з назвою
    public Group(String groupName) {
        setGroupName(groupName);
        this.students = new ArrayList<>();
    }

    // встановлення назви групи з валідацією
    public void setGroupName(String groupName) {
        if (groupName == null || groupName.trim().isEmpty()) {
            throw new IllegalArgumentException("Назва групи не може бути порожньою");
        }
        this.groupName = groupName.trim();
    }

    // додавання студента до групи
    public void addStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Студент не може бути null");
        }
        if (students.size() >= MAX_STUDENTS) {
            throw new IllegalStateException("Досягнуто максимальної кількості студентів: " + MAX_STUDENTS);
        }
        if (students.contains(student)) {
            throw new IllegalArgumentException("Студент " + student.getFullName() + " уже є в групі");
        }
        students.add(student);
    }

    // видалення студента з групи
    public boolean removeStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Студент не може бути null");
        }
        return students.remove(student);
    }

    // видалення студента за ПІБ
    public boolean removeStudentByName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("ПІБ не може бути порожнім");
        }
        return students.removeIf(student -> student.getFullName().equalsIgnoreCase(fullName.trim()));
    }

    // отримання незмінного списку студентів
    public List<Student> getStudents() {
        return Collections.unmodifiableList(students);
    }

    // отримання назви групи
    public String getGroupName() {
        return groupName;
    }

    // пошук студента за ПІБ
    public Optional<Student> findStudentByName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("ПІБ не може бути порожнім");
        }
        return students.stream().filter(student -> student.getFullName().equalsIgnoreCase(fullName.trim())).findFirst();
    }

    // сортування студентів за ПІБ
    public List<Student> getStudentsSortedByName() {
        return students.stream().sorted(Comparator.comparing(Student::getFullName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    // сортування студентів за середньою оцінкою за домашні завдання
    public List<Student> getStudentsSortedByAverageHomeworkGrade() {
        return students.stream().sorted(Comparator.comparingDouble(Student::getAverageHomeworkGrade).reversed())
                .collect(Collectors.toList());
    }

    // сортування студентів за середньою оцінкою за екзамени
    public List<Student> getStudentsSortedByAverageExamGrade() {
        return students.stream().sorted(Comparator.comparingDouble(Student::getAverageExamGrade).reversed())
                .collect(Collectors.toList());
    }

    // отримання середньої оцінки за домашні завдання по групі
    public double getAverageHomeworkGrade() {
        if (students.isEmpty()) {
            return 0.0;
        }
        return students.stream().mapToDouble(Student::getAverageHomeworkGrade).filter(grade -> grade > 0).average()
                .orElse(0.0);
    }

    // отримання середньої оцінки за екзамени по групі
    public double getAverageExamGrade() {
        if (students.isEmpty()) {
            return 0.0;
        }
        return students.stream().mapToDouble(Student::getAverageExamGrade).filter(grade -> grade > 0).average()
                .orElse(0.0);
    }

    // отримання студентів з середньою оцінкою за екзамени вище заданого порогу
    public List<Student> getStudentsWithExamGradeAbove(double threshold) {
        return students.stream().filter(student -> student.getAverageExamGrade() >= threshold)
                .collect(Collectors.toList());
    }

    // перевірка, чи група порожня
    public boolean isEmpty() {
        return students.isEmpty();
    }

    // отримання кількості студентів у групі
    public int getStudentCount() {
        return students.size();
    }

    // порівняння об'єктів Group
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Group))
            return false;
        Group group = (Group) o;
        return groupName.equals(group.groupName) && students.equals(group.students);
    }

    // генерація хеш-коду
    @Override
    public int hashCode() {
        return Objects.hash(groupName, students);
    }

    // форматування рядка для виведення інформації про групу
    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append("Група: ").append(groupName).append("\nКількість студентів: ").append(students.size())
                .append("\nСередня оцінка за домашні завдання по групі: ")
                .append(String.format("%.2f", getAverageHomeworkGrade()))
                .append("\nСередня оцінка за екзамени по групі: ").append(String.format("%.2f", getAverageExamGrade()))
                .append("\n\nСписок студентів:\n");

        if (students.isEmpty()) {
            sb.append("Група порожня\n");
        } else {
            for (Student student : getStudentsSortedByName()) {
                sb.append(student).append("\n");
            }
        }
        return sb.toString();
    }
}