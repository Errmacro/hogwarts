package ru.hogwarts.school.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/student")
@RestController
public class StudentController {
    private final StudentService studentService;
    private final StudentRepository studentRepository;

    public StudentController(StudentService studentService, StudentRepository studentRepository) {
        this.studentService = studentService;
        this.studentRepository = studentRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentInfo(@PathVariable Long id) {
        Student student = studentService.getStudentByID(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @GetMapping("/byName")
    public ResponseEntity findStudentByName(@RequestParam String name) {
        if (name != null && !name.isBlank()) {
            return ResponseEntity.ok(studentService.findByName(name));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student foundStudent = studentService.updateStudent(student);
        if (foundStudent == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(foundStudent);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }

    @GetMapping
    public ResponseEntity<Collection<Student>> findStudents(@RequestParam(required = false) int age) {
        if (age > 0) {
            return ResponseEntity.ok(studentService.getStudentsByAge(age));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping("/byAgeBetween")
    public ResponseEntity findStudentByName(@RequestParam int min, @RequestParam int max) {
        if (min < 0 || max < 0) {
            return ResponseEntity.badRequest().build();
        }
        if (min > max) {
            return ResponseEntity.badRequest().build();
        }
        Collection<Student> students = studentService.findByAgeBetween(min, max);
        if (students.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(students);
    }

    @GetMapping("/by-name/{name}/faculty")
    public ResponseEntity<Faculty> getStudentFacultyByName(@PathVariable String name) {
        Student student = studentService.findByName(name);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        Faculty faculty = student.getFaculty();
        if (faculty == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @GetMapping("/count")
    public Integer getTotalStudentCount() {
        return studentService.getTotalCountOfStudents();
    }

    @GetMapping("/average-age")
    public double getStudentAverageAge() {
        return studentService.getAverageAgeOfStudents();
    }

    @GetMapping("/last-five")
    public Collection<Student> getLastStudents() {
        return studentService.getLastFiveStudents();
    }

    @GetMapping("/names-starting-with-Г")
    public Collection<String> getNamesStartingWithG() {
        Collection<Student> allStudents = studentRepository.findAll();
        return allStudents.stream()
                .map(Student::getName)
                .filter(name -> name.toLowerCase().startsWith("г"))
                .map(String::toUpperCase)
                .sorted()
                .collect(Collectors.toList());
    }

    @GetMapping("/average-age-streamed")
    public Double getAverageAge() {
        Collection<Student> students = studentRepository.findAll();
        if (students.isEmpty()) {
            return 0.0;
        }
        return students.stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0.0);
    }
}
