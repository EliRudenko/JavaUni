package com.example.hiber;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.*;




@Service
public class StudentService {

    @Autowired
    private StudentRepo studentRepository;

    @PersistenceContext
    private EntityManager entityManager;



    // пошук студента
    @Transactional(readOnly = true)
    public Student findById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Студента з ID " + id + " не знайдено"));
    }

    // всі студенти
    @Transactional(readOnly = true)
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    // створення
    @Transactional
    public void saveStudent(String name, String email) {
        Student student = new Student(name, email);
        studentRepository.save(student);
    }

    // оновлення
    @Transactional
    public void updateStudent(Long id, String name, String email) {
        Student student = findById(id);
        student.setName(name);
        student.setEmail(email);
        studentRepository.save(student);
    }

    // видалення
    @Transactional
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id))
            throw new EntityNotFoundException("Студента не знайдено");

        studentRepository.deleteById(id);
    }

    // курси студента
    @Transactional(readOnly = true)
    public List<Course> findCoursesByStudentId(Long studentId) {
        String hql = "SELECT s.courses FROM Student s WHERE s.id = :id";
        return entityManager.createQuery(hql, Course.class)
                .setParameter("id", studentId)
                .getResultList();
    }

    // додання курсу студенту по назві
    @Transactional
    public void addCourseToStudent(Long studentId, String title) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Студента не знайдено"));

        // пошук курсу по назві
        String hql = "FROM Course c WHERE c.title = :title";
        Course course = entityManager.createQuery(hql, Course.class)
                .setParameter("title", title)
                .getResultStream()
                .findFirst()
                .orElse(null);

        // якщо курсу нема → створюємо
        if (course == null) {
            course = new Course(title);
            entityManager.persist(course);
        }

        if (student.getCourses() == null) {
            student.setCourses(new ArrayList<>());
        }

        student.getCourses().add(course);

        studentRepository.save(student);
    }

    // студенти за назвою курсу
    @Transactional(readOnly = true)
    public List<Student> findStudentsByCourseTitle(String courseTitle) {
        String hql = "SELECT s FROM Student s JOIN s.courses c WHERE c.title = :title";
        return entityManager.createQuery(hql, Student.class)
                .setParameter("title", courseTitle)
                .getResultList();
    }





    // Подсчет студентов по курсу
    @Transactional(readOnly = true)
    public Map<String, Integer> getStudentCountByCourse() {
        List<Object[]> results = studentRepository.countStudentsByCourse();
        Map<String, Integer> map = new HashMap<>();
        for (Object[] row : results) {
            String courseTitle = (String) row[0];
            Long count = (Long) row[1];
            map.put(courseTitle, count.intValue());
        }
        return map;
    }

    // Пагинация и сортировка
    @Transactional(readOnly = true)
    public Page<Student> searchStudentsPaginated(String name, int page, int size, String sortBy) {
        Sort sort = Sort.by(Sort.Direction.ASC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return studentRepository.findByNameContainingIgnoreCase(name, pageable);
    }
}
