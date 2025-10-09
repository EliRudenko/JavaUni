package org.example.student;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

// клас для тестування функціональності класу Student
@DisplayName("Тести для класу Student")
class StudentTest {
    private Student student;
    private static final LocalDate VALID_DATE = LocalDate.of(2000, 1, 1);

    // ініціалізація перед кожним тестом
    @BeforeEach
    void setUp() {
        student = new Student("Тестовий Студент", VALID_DATE);
    }

    // тестування створення об'єкта Student
    @Test
    @DisplayName("Перевірка створення студента з коректними даними")
    void testCreateStudent() {
        assertAll("Перевірка ініціалізації студента",
                () -> assertEquals("Тестовий Студент", student.getFullName(), "ПІБ має відповідати введеному"),
                () -> assertEquals(VALID_DATE, student.getBirthDate(), "Дата народження має відповідати"),
                () -> assertTrue(student.getHomeworkGrades().isEmpty(), "Список оцінок за ДЗ має бути порожнім"),
                () -> assertTrue(student.getExamGrades().isEmpty(), "Список оцінок за екзамени має бути порожнім"));
    }

    // тестування винятків при створенні студента
    @Test
    @DisplayName("Перевірка винятків при некоректному ПІБ або даті")
    void testInvalidStudentCreation() {
        assertAll("Перевірка винятків при створенні",
                () -> assertThrows(IllegalArgumentException.class, () -> new Student(null, VALID_DATE),
                        "Порожнє ПІБ має викликати виняток"),
                () -> assertThrows(IllegalArgumentException.class, () -> new Student("", VALID_DATE),
                        "Порожній рядок ПІБ має викликати виняток"),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Student("Тест", LocalDate.now().plusDays(1)), "Майбутня дата має викликати виняток"),
                () -> assertThrows(IllegalArgumentException.class, () -> new Student("Тест", null),
                        "Нульова дата має викликати виняток"));
    }

    // тестування встановлення ПІБ
    @Test
    @DisplayName("Перевірка встановлення коректного ПІБ")
    void testSetFullName() {
        student.setFullName("Новий Студент");
        //assertEquals("Йой", student.getFullName(), "ПІБ має бути оновлено");
        assertEquals("Новий Студент", student.getFullName(), "ПІБ має бути оновлено");

    }

    // тестування винятків при встановленні некоректного ПІБ
    @Test
    @DisplayName("Перевірка винятків при некоректному ПІБ")
    void testSetInvalidFullName() {
        assertAll("Перевірка винятків для setFullName",
                () -> assertThrows(IllegalArgumentException.class, () -> student.setFullName(null),
                        "Нульове ПІБ має викликати виняток"),
                () -> assertThrows(IllegalArgumentException.class, () -> student.setFullName(""),
                        "Порожнє ПІБ має викликати виняток"),
                () -> assertThrows(IllegalArgumentException.class, () -> student.setFullName("   "),
                        "ПІБ з пробілів має викликати виняток"));
    }

    // тестування встановлення дати народження
    @Test
    @DisplayName("Перевірка встановлення коректної дати народження")
    void testSetBirthDate() {
        LocalDate newDate = LocalDate.of(1999, 12, 31);
        student.setBirthDate(newDate);
        assertEquals(newDate, student.getBirthDate(), "Дата народження має бути оновлена");
    }

    // тестування винятків при встановленні некоректної дати
    @Test
    @DisplayName("Перевірка винятків при некоректній даті народження")
    void testSetInvalidBirthDate() {
        assertAll("Перевірка винятків для setBirthDate",
                () -> assertThrows(IllegalArgumentException.class, () -> student.setBirthDate(null),
                        "Нульова дата має викликати виняток"),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> student.setBirthDate(LocalDate.now().plusDays(1)),
                        "Майбутня дата має викликати виняток"));
    }

    // параметризований тест для додавання оцінок за домашні завдання
    @ParameterizedTest
    @ValueSource(ints = { 1, 6, 12 })
    @DisplayName("Перевірка додавання коректних оцінок за ДЗ")
    void testAddHomeworkGrade(int grade) {
        student.addHomeworkGrade(grade);
        List<Integer> grades = student.getHomeworkGrades();
        assertAll("Перевірка додавання оцінки за ДЗ",
                () -> assertEquals(1, grades.size(), "Список оцінок має містити одну оцінку"),
                () -> assertTrue(grades.contains(grade), "Оцінка має бути в списку"));
    }

    // тестування винятків для некоректних оцінок за ДЗ
    @ParameterizedTest
    @ValueSource(ints = { 0, 13, -1 })
    @DisplayName("Перевірка винятків при додаванні некоректних оцінок за ДЗ")
    void testInvalidHomeworkGrade(int invalidGrade) {
        assertThrows(IllegalArgumentException.class, () -> student.addHomeworkGrade(invalidGrade),
                "Некоректна оцінка за ДЗ має викликати виняток");
        assertTrue(student.getHomeworkGrades().isEmpty(), "Список оцінок за ДЗ має залишатися порожнім");
    }

    // тестування додавання оцінок за екзамени
    @Test
    @DisplayName("Перевірка додавання коректних оцінок за екзамени")
    void testAddExamGrade() {
        student.addExamGrade(6);
        student.addExamGrade(7);
        List<Integer> grades = student.getExamGrades();
        assertAll("Перевірка списку оцінок за екзамени",
                () -> assertEquals(2, grades.size(), "Список має містити дві оцінки"),
                () -> assertTrue(grades.contains(6), "Оцінка 6 має бути в списку"),
                () -> assertTrue(grades.contains(7), "Оцінка 7 має бути в списку"));
    }

    // тестування перевищення ліміту оцінок за екзамени
    @Test
    @DisplayName("Перевірка перевищення максимальної кількості оцінок за екзамени")
    void testAddTooManyExamGrades() {
        for (int i = 0; i < 5; i++) {
            student.addExamGrade(10);
        }
        assertThrows(IllegalStateException.class, () -> student.addExamGrade(10),
                "Додавання більше 5 оцінок має викликати виняток");
        assertEquals(5, student.getExamGrades().size(), "Список оцінок має містити лише 5 оцінок");
    }

    // тестування винятків для некоректних оцінок за екзамени
    @ParameterizedTest
    @ValueSource(ints = { 0, 13, -1 })
    @DisplayName("Перевірка винятків при додаванні некоректних оцінок за екзамени")
    void testInvalidExamGrade(int invalidGrade) {
        assertThrows(IllegalArgumentException.class, () -> student.addExamGrade(invalidGrade),
                "Некоректна оцінка за екзамен має викликати виняток");
        assertTrue(student.getExamGrades().isEmpty(), "Список оцінок за екзамени має залишатися порожнім");
    }

    // тестування середньої оцінки за домашні завдання
    @Test
    @DisplayName("Перевірка обчислення середньої оцінки за ДЗ")
    void testAverageHomeworkGrade() {
        student.addHomeworkGrade(8);
        student.addHomeworkGrade(10);
        student.addHomeworkGrade(12);
        assertEquals(10.0, student.getAverageHomeworkGrade(), 0.01, "Середня оцінка за ДЗ має бути 10.0");
        assertEquals(0.0, new Student("Порожній", VALID_DATE).getAverageHomeworkGrade(), 0.01,
                "Середня оцінка за порожній список має бути 0.0");
    }

    // тестування середньої оцінки за екзамени
    @Test
    @DisplayName("Перевірка обчислення середньої оцінки за екзамени")
    void testAverageExamGrade() {
        student.addExamGrade(7);
        student.addExamGrade(9);
        assertEquals(8.0, student.getAverageExamGrade(), 0.01, "Середня оцінка за екзамени має бути 8.0");
        assertEquals(0.0, new Student("Порожній", VALID_DATE).getAverageExamGrade(), 0.01,
                "Середня оцінка за порожній список має бути 0.0");
    }

    // тестування незмінності списків оцінок
    @Test
    @DisplayName("Перевірка незмінності списків оцінок")
    void testImmutableGradesLists() {
        List<Integer> homeworkGrades = student.getHomeworkGrades();
        List<Integer> examGrades = student.getExamGrades();
        assertThrows(UnsupportedOperationException.class, () -> homeworkGrades.add(10),
                "Список оцінок за ДЗ має бути незмінним");
        assertThrows(UnsupportedOperationException.class, () -> examGrades.add(10),
                "Список оцінок за екзамени має бути незмінним");
    }

    // параметризований тест для перевірки equals
    @ParameterizedTest
    @CsvSource({ "Тестовий Студент, 2000-01-01, true", "Інший Студент, 2000-01-01, false",
            "Тестовий Студент, 1999-12-31, false" })
    @DisplayName("Перевірка методу equals")
    void testEquals(String fullName, String date, boolean expected) {
        Student other = new Student(fullName, LocalDate.parse(date));
        assertEquals(expected, student.equals(other), "Результати порівняння мають відповідати очікуваному");
    }

    // тестування hashCode
    @Test
    @DisplayName("Перевірка консистентності hashCode")
    void testHashCode() {
        Student other = new Student("Тестовий Студент", VALID_DATE);
        assertEquals(student.hashCode(), other.hashCode(), "Хеш-коди однакових студентів мають збігатися");
    }

    // вкладені тести для перевірки поведінки зі старими датами
    @Nested
    @DisplayName("Тести для студентів зі старими датами народження")
    class OldBirthDateTests {
        private Student oldStudent;

        @BeforeEach
        void setUp() {
            oldStudent = new Student("Старий Студент", LocalDate.of(1980, 1, 1));
        }

        @Test
        @DisplayName("Перевірка коректності старої дати")
        void testOldBirthDate() {
            assertEquals(LocalDate.of(1980, 1, 1), oldStudent.getBirthDate(),
                    "Стара дата народження має бути коректною");
        }

        @Test
        @DisplayName("Перевірка віку студента")
        void testStudentAge() {
            long age = ChronoUnit.YEARS.between(oldStudent.getBirthDate(), LocalDate.now());
            assertTrue(age >= 45, "Вік студента має бути >= 45 років");
        }
    }

    // умовний тест, який виконується лише за певної системної властивості
    @Test
    @EnabledIfSystemProperty(named = "test.env", matches = "dev")
    @DisplayName("Умовний тест для dev-середовища")
    void testOnlyInDevEnvironment() {
        assumeTrue(System.getProperty("test.env", "").equals("dev"), "Тест виконується лише в dev-середовищі");
        student.addHomeworkGrade(10);
        assertEquals(10.0, student.getAverageHomeworkGrade(), 0.01, "Середня оцінка за ДЗ має бути 10.0");
    }

    // тестування часу виконання
    @Test
    @DisplayName("Перевірка часу виконання додавання оцінок")
    void testAddGradePerformance() {
        assertTimeout(java.time.Duration.ofSeconds(1), () -> {
            for (int i = 0; i < 1000; i++) {
                student.addHomeworkGrade(10);
            }
        }, "Додавання 1000 оцінок має бути швидким");
    }
}