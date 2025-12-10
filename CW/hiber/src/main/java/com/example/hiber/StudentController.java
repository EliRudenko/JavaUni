package com.example.hiber;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    // отримати всіх студентів
    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.findAll();
    }

    // отримати студента по ID
    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Long id) {
        return studentService.findById(id);
    }

    // пошук за курсом
    @GetMapping("/by-course")
    public List<Student> getStudentsByCourse(@RequestParam String title) {
        return studentService.findStudentsByCourseTitle(title);
    }

    // створити студента
    @PostMapping
    public ResponseEntity<String> createStudent(
            @RequestParam String name,
            @RequestParam String email
    ) {
        studentService.saveStudent(name, email);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Студента успішно створено.");
    }

    // оновити студента
    @PutMapping("/{id}")
    public ResponseEntity<String> updateStudent(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String email
    ) {
        studentService.updateStudent(id, name, email);
        return ResponseEntity.ok("Студента оновлено.");
    }

    // видалити студента
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok("Студента успішно видалено.");
    }

    // отримати всі курси студента
    @GetMapping("/{id}/courses")
    public List<Course> getStudentCourses(@PathVariable Long id) {
        return studentService.findCoursesByStudentId(id);
    }

    // ДОДАТИ КУРС СТУДЕНТУ ПО НАЗВІ КУРСУ
    @PostMapping("/{studentId}/courses")
    public ResponseEntity<String> addCourseToStudent(
            @PathVariable Long studentId,
            @RequestParam String title
    ) {
        studentService.addCourseToStudent(studentId, title);
        return ResponseEntity.ok("Курс успішно додано студенту.");
    }





    // Эндпоинт для подсчета студентов по курсам
    @GetMapping("/count-by-course")
    public Map<String, Integer> getStudentCountByCourse() {
        return studentService.getStudentCountByCourse();
    }

    // Эндпоинт для поиска с пагинацией и сортировкой
    @GetMapping("/search-paginated")
    public Page<Student> searchStudentsPaginated(
            @RequestParam String name,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(defaultValue = "name") String sort
    ) {
        return studentService.searchStudentsPaginated(name, page, size, sort);
    }
}
