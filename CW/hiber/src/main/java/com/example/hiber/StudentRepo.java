package com.example.hiber;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentRepo extends JpaRepository<Student, Long> {

    @Query("SELECT s FROM Student s JOIN s.courses c WHERE c.title = :courseTitle")
    List<Student> findStudentsByCourseTitle(String courseTitle);

    @Query("SELECT c.title, COUNT(s.id) FROM Student s JOIN s.courses c GROUP BY c.title")
    List<Object[]> countStudentsByCourse();

    // Пагинация и поиск по имени
    Page<Student> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
