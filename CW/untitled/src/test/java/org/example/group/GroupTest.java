package org.example.group;

import org.example.student.Student;
import org.junit.jupiter.api.*;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для класу Group")
class GroupTest
{
    private Group group;
    private Student student1;

    @BeforeEach
    void setUp()
    {
        group = new Group("КНП-221");
        student1 = new Student("Елена Руденко", LocalDate.of(2005, 7, 30));
    }

    @Test
    @DisplayName("Создание группы с названием")
    void testCreateGroup()
    {
        assertAll(
                () -> assertEquals("КНП-221", group.getGroupName(), "название"),
                () -> assertTrue(group.isEmpty(), "пустая")
        );
    }

    @Test
    @DisplayName("Добаление")
    void testAddStudent()
    {
        group.addStudent(student1);
        assertAll(
                () -> assertEquals(1, group.getStudentCount(), "есть 1 студент"),
                () -> assertTrue(group.getStudents().contains(student1), "студент естьв группе")
        );
    }

    @Test
    @DisplayName("Удаление из группы")
    void testRemoveStudent()
    {
        group.addStudent(student1);
        assertTrue(group.removeStudent(student1), "удален");
        assertTrue(group.isEmpty(), "группа пустая");
    }

    @Test
    @DisplayName("Удаление по имени")
    void testRemoveStudentByName()
    {
        group.addStudent(student1);
        assertTrue(group.removeStudentByName("Елена Руденко"), "удален");
        assertEquals(0, group.getStudentCount(), "группа пустая");
    }
}
